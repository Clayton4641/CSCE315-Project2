import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.AccessibleObject;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Class to contain the creation of the user summary GUI.
 */
public class userSummaryGUI {
    private JLabel userSummaryLabel = new JLabel("User Summary");
    private JLabel separatorLabel4 = new JLabel("-----------------------------------------------------------------------------------------------");
    private JLabel userIDLabel = new JLabel("User Name:");
    
    private JTextField userNameText = new JTextField();
    private JTextArea resultsText = new JTextArea();


    private JButton userSearchButton = new JButton("Get User Summary");

    private final int TEXT_BOX_WIDTH = 100;

    /**
     * Builds and starts the user summary GUI.
     */
    public void start(){

        userSearchButton.addActionListener(new searchHandler());

        JFrame mainFrame = new JFrame();

        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainFrame.add(makeUserSummaryPanel());

        mainFrame.setTitle("User Summary Search");

        mainFrame.pack(); // this sets the dimensions of the GUI automatically

        mainFrame.setVisible(true);
    }

    /**
     * Here the summary panel is made; the panel is based on a grid bag layout.
     * @return A JPanel for the user summary GUI
     */
    private JPanel makeUserSummaryPanel(){

        JPanel userSum = new JPanel(new GridBagLayout());
        resultsText.setColumns(40); 
        resultsText.setRows(5);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.anchor = GridBagConstraints.WEST;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        userSum.add(userSummaryLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        userSum.add(separatorLabel4,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        userSum.add(userIDLabel,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = TEXT_BOX_WIDTH;
        userSum.add(userNameText,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        userSum.add(userSearchButton,gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        userSum.add(resultsText,gridBagConstraints);


        return userSum;
    }
    /**
     * Action listener to trigger when the user hits the search button.
     */
    private class searchHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String userName = userNameText.getText().strip();
            String userSumQuery = "SELECT user_id, average_stars, compliment_funny, compliment_cool, compliment_writer FROM users "
            		+ "WHERE name ='" + userName+ "' AND Cast ( review_count as INTEGER ) > 5 "
            				+ "ORDER BY user_id ASC Limit 1"; 
            
            if (userName.isEmpty()){
                resultsText.setText("Must enter a valid username");
            } else {
					try {
					ResultSet result = SQLClient.client.queryFor(userSumQuery);
					result.next();
					
					double stars = Double.parseDouble(result.getString("average_stars")); 
					int writer = Integer.parseInt(result.getString("compliment_writer")); 
					int funny = Integer.parseInt(result.getString("compliment_funny"));
					int cool = Integer.parseInt(result.getString("compliment_cool")); 
											
					String queryResult = "Username: " + userNameText.getText() + "\nAverage stars: " + stars + "\nWriter received: "
		            		+ writer + "\nFunny received: " + funny + "\nCool received: " + cool; 
		            resultsText.setText(queryResult); 

				} catch (Exception sqlException) {
					sqlException.printStackTrace();
				}
            }
        }
    }
}
