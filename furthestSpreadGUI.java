import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
				String command = "SELECT businesses.name, businesses.longitude, businesses.latitude, businesses.stars FROM businesses WHERE businesses.business_id IN (SELECT business_id FROM address WHERE state = \'" + 
					stateNameText.getText().toUpperCase().strip() + "\')";

				long start = 0;
				long end = 0;

				try {

					HashMap<String, ArrayList<GeoLocation>> businessMap = new HashMap<String, ArrayList<GeoLocation>>();
					HashMap<String, ArrayList<Double>> businessRating = new HashMap<String, ArrayList<Double>>();
					HashMap<String, Double> businessSpread = new HashMap<String, Double>();

					ResultSet result = SQLClient.client.queryFor(command);

					start = System.currentTimeMillis();

					// Process data into a hash map
					while (result.next()) {
						String businessName = result.getString("name");
						double longitude = Double.parseDouble(result.getString("longitude"));
						double latitude = Double.parseDouble(result.getString("latitude"));
						double stars = Double.parseDouble(result.getString("stars"));

						GeoLocation location = new GeoLocation(longitude, latitude);

						if (businessMap.containsKey(businessName)) {
							businessMap.get(businessName).add(location);
							businessRating.get(businessName).add(stars);
						} else {
							businessMap.put(businessName, new ArrayList<GeoLocation>());
							businessMap.get(businessName).add(location);

							businessRating.put(businessName, new ArrayList<Double>());
							businessRating.get(businessName).add(stars);
						}
					}

					HashMap<String, Double> businessAverageRating = new HashMap<String, Double>();

					for (Map.Entry<String, ArrayList<Double>> entry : businessRating.entrySet()) {

						double avgRating = 0.0;

						for (Double star : entry.getValue()) {
							avgRating += star.doubleValue();
						}

						avgRating = avgRating / entry.getValue().size();

						//System.out.println(entry.getKey() + " has a size of " + entry.getValue().size() + " and a avg rating of " + avgRating);

						businessAverageRating.put(entry.getKey(), avgRating);
					}

					// Process hash map to find franchises with a 3.5 rating or higher.
					for (Map.Entry<String, ArrayList<GeoLocation>> entry : businessMap.entrySet()) {
						String businessName = entry.getKey();

						if (businessAverageRating.get(businessName).doubleValue() < 3.5) {
							continue;
						}

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

					Stream<Map.Entry<String, Double>> sortedSet = businessSpread.entrySet().stream().sorted(Map.Entry.comparingByValue());

					String output = "";

					Iterator<Entry<String, Double>> iter = sortedSet.iterator();
					ArrayList<Entry<String, Double>> array = new ArrayList<Entry<String, Double>>();

					while (iter.hasNext()) { 
						array.add(iter.next());
					}

					System.out.println(array.size() + " franchises with an average rating of 3.5 or higher found.");

					if (array.size() >= 5)
					{
						for (int i = array.size() - 1; i > array.size() - 6; --i) {
							output += 
							array.get(i).getKey() + 
							"\n        Distance: " + array.get(i).getValue() + " km" +
							"\n        Average Rating: " + businessAverageRating.get(array.get(i).getKey()) + "\n";
						}
					} else {
						for (int i = array.size() - 1; i >= 0; --i) {
							output += 
							array.get(i).getKey() + 
							"\n        Distance: " + array.get(i).getValue() + " km" +
							"\n        Average Rating: " + businessAverageRating.get(array.get(i).getKey()) + "\n";
						}
					}

					if (output != "") {
						resultsText.setText(output);
					} else {
						resultsText.setText("There are no franchises with an average rating of 3.5 or higher in " + stateNameText.getText().strip());
					}

					end = System.currentTimeMillis();

					System.out.println("Operation took " + Long.toString(end - start) + " ms after the SQL query finished.");

				} catch (Exception sqlException) {
					sqlException.printStackTrace();
				}
            }
        }
    }
}
