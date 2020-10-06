import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.ArrayList;

/**
 * Class to contain the creation of the furthest spread search GUI.
 */
public class furthestSpreadGUI {

    private JLabel furthestSpreadLabel = new JLabel("Furthest Spread");
    private JLabel separatorLabel = new JLabel("-----------------------------------------------------------------------------------------------");
    private JLabel stateNameLabel = new JLabel("State Name:");

    private JTextField stateNameText = new JTextField();

    private JButton searchStateButton = new JButton("Find Furthest Spread");

    private JTextArea resultsText = new JTextArea();

	private final int TEXT_BOX_WIDTH = 100;
	
    /**
     * Builds and starts the furthest spread GUI.
     */
    public void start(){

        searchStateButton.addActionListener(new furthestHandler());

        JFrame mainFrame = new JFrame();

        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainFrame.add(makeFurthestPanel());

        mainFrame.setTitle("Furthest Spread Search");

        mainFrame.pack(); // this sets the dimensions of the GUI automatically

        mainFrame.setVisible(true);
    }

    /**
     * Here the furthest spread panel is made; the panel is based on a grid bag layout.
     * @return A JPanel for the user summary GUI
     */
    private JPanel makeFurthestPanel(){
        resultsText.setColumns(40);
        resultsText.setRows(5);

        JPanel furthest = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.anchor = GridBagConstraints.WEST;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        furthest.add(furthestSpreadLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        furthest.add(separatorLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        furthest.add(stateNameLabel,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = TEXT_BOX_WIDTH;
        furthest.add(stateNameText,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 0;
        furthest.add(searchStateButton,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        furthest.add(resultsText,gridBagConstraints);

        return furthest;
    }

    /**
     * Action listener to trigger when the user hits the search button.
     */
    private class furthestHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
			
            String stateName = stateNameText.getText().strip();

            if (stateName.isEmpty()){
                resultsText.setText("A state must be entered.");
            } else {
				String command = "SELECT businesses.name, businesses. FROM businesses WHERE businesses.business_id IN (SELECT business_id FROM address WHERE state = \'" + 
					stateNameText.getText().strip() + "\')'";
				
				try {
					HashMap<String, ArrayList<GeoLocation>> businessMap = new HashMap<String, ArrayList<GeoLocation>>();
					HashMap<String, Double> businessSpread = new HashMap<String, Double>();

					ResultSet result = client.queryFor(command);

					// Process data into a hash map
					while (result.next()) {
						String businessName = result.getString("name");
						double longitude = Double.parseDouble(result.getString("longitude"));
						double latitude = Double.parseDouble(result.getString("latitude"));

						GeoLocation location = new GeoLocation(longitude, latitude);

						if (businessMap.containsKey(businessName)) {
							businessMap.get(businessName).add(location);
						} else {
							businessMap.put(businessName, new ArrayList<GeoLocation>());
							businessMap.get(businessName).add(location);
						}
					}

					// Process hash map to find franchises.
					for (Map.Entry<String, ArrayList<GeoLocation>> entry : businessMap.entrySet()) {
						String businessName = entry.getKey();

						double distance = DistanceFinder.longestDistance(entry.getValue().toArray(new GeoLocation[entry.getValue().size()]));

						if (distance != 0.0) {
							if (businessSpread.containsKey(businessName)) {
								if (businessSpread.get(businessName).doubleValue() < distance) {
									businessSpread.put(businessName, distance);
								}
							} else {
								businessSpread.put(entry.getKey(), distance);
							}
						}
					}

					// Find the top 5 franchises
					Stream<Map.Entry<String, Double>> sortedSet = businessSpread.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(5);

					sortedSet.forEach(System.out::println);

				} catch (Exception sqlException) {
					sqlException.printStackTrace();
				}
				
				
            }
        }
    }
}
