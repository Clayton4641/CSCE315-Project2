import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class to contain the creation of the shortest chain search GUI.
 */
public class shortestChainGUI {
    private JLabel shortestChainLabel = new JLabel("Shortest Chain");
    private JLabel restaurant1Label = new JLabel("Restaurant One ID:");
    private JLabel restaurant2Label = new JLabel("Restaurant Two ID:");
    private JLabel separatorLabel = new JLabel("-----------------------------------------------------------------------------------------------");

    private JTextField restaurant1Test = new JTextField();
    private JTextField restaurant2Text = new JTextField();
    private JTextArea resultArea = new JTextArea();

    private JButton shortestChainButton = new JButton("Find Shortest Chain"); // need to link to action listened
    private JButton updateLocalData = new JButton("Refresh Review Data");

    private final int TEXT_BOX_WIDTH = 150;

    private final String DATA_FILE_NAME = "IDs.csv";

    private Dictionary<String,LinkedList<String>> busToUser = new Hashtable<>();
    private Dictionary<String,LinkedList<String>> userToBus = new Hashtable<>();

    /**
     * Builds and starts the shortest chain GUI.
     */
    public void start(){

        try {
            mapDataFromFile(userToBus,busToUser); // init the dictionaries for searching
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        shortestChainButton.addActionListener(new shortestChainAction());
        updateLocalData.addActionListener(new updateDataAction());

        JFrame mainFrame = new JFrame();

        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainFrame.add(makeShortestChainPanel());

        mainFrame.setTitle("Shortest Chain Search");

        mainFrame.pack(); // this sets the dimensions of the GUI automatically

        mainFrame.setVisible(true);
    }

    /**
     * Here the shortest chain panel is made; the panel is based on a grid bag layout.
     * @return A JPanel for the user summary GUI
     */
    private JPanel makeShortestChainPanel(){

        resultArea.setLineWrap(true);
        resultArea.setColumns(48);
        resultArea.setRows(5);

        JPanel shortestPathPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.anchor = GridBagConstraints.WEST;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        shortestPathPanel.add(shortestChainLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        shortestPathPanel.add(separatorLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        shortestPathPanel.add(restaurant1Label,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = TEXT_BOX_WIDTH;
        shortestPathPanel.add(restaurant1Test,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 0;
        shortestPathPanel.add(restaurant2Label,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = TEXT_BOX_WIDTH;
        shortestPathPanel.add(restaurant2Text,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 0;
        shortestPathPanel.add(shortestChainButton,gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 0;
        shortestPathPanel.add(updateLocalData,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        shortestPathPanel.add(resultArea,gridBagConstraints);

        return shortestPathPanel;
    }

    /**
     * Action listener to trigger when the user hits the search button.
     */
    private class shortestChainAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String id1 = restaurant1Test.getText().strip();
            String id2 = restaurant2Text.getText().strip();

            if (id1.isEmpty() || id2.isEmpty()){
                resultArea.setText("Both restaurants must be entered.");
            }
            else {
                String s = null;
                try {

                    File tempFile = new File(DATA_FILE_NAME); // if the local id data is not here download it
                    if(!tempFile.exists()){
                        pullerUserAndReviewData();
                    }

                    s = findShortestChain(id1,id2); // run the search

                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }

                resultArea.setText(s); // display results
            }
        }
    }

    /**
     * Action listener to update local id data from the data base.
     */
    private class updateDataAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                pullerUserAndReviewData();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * Inner class to help store chain information from user to user linked to a business id.
     */
    private class node{
        public String id;
        public LinkedList<String> path = new LinkedList<>();
        public node(String id) {
            this.id = id;
        }
        public node(String id, LinkedList<String> list){
            this.id = id;
            this.path = list; // shallow copy
        }
    }

    /**
     * Breadth First Search to find the chain between two given businesses.
     * @param startID The business Id to start the search off with
     * @param targetID The business Id to find the chain for
     * @return A Linked list that contains the user IDs that make the chain
     * @throws SQLException
     */
    private LinkedList<String> bfs(String startID, String targetID) throws SQLException {

        Queue<node> toVisit = new LinkedList<>();
        Set<String> visitedBusinesses = new HashSet<>();
        Set<String> visitedCustomers = new HashSet<>();

        visitedBusinesses.add(startID);
        toVisit.add(new node(startID));

        while(!toVisit.isEmpty()){
            node v = toVisit.remove();

            if (v.id.compareTo(targetID) == 0){ // if goal stop
                return v.path;
            }

            // get the users that have been here
            LinkedList<String> users = busToUser.get(v.id);
            // 5YvcrqwD4irC_-j-vNC5TA
            // uYn9um4e0ymbhneJ2VLoYg

            for(String userID : users){

                if (!visitedCustomers.contains(userID)){

                    visitedCustomers.add(userID);

                    LinkedList<String> businesses = userToBus.get(userID);

                    for(String businessId : businesses){

                        if (!visitedBusinesses.contains(businessId)){

                            visitedBusinesses.add(businessId);
                            LinkedList<String> temp = new LinkedList<>();
                            temp.addAll(v.path); // deep copy of the list
                            temp.add(userID); // add new action

                            if (businessId.compareTo(targetID) == 0){ // if goal return
                                return temp;
                            }

                            toVisit.add(new node(businessId,temp));
                        }
                    }
                }
            }
        }
        return null; // fail
    }

    /**
     * method to download the needed id data to run the search in a reasonable amount of time
     * @throws SQLException
     * @throws IOException
     */
    private void pullerUserAndReviewData() throws SQLException, IOException {
        String command = "SELECT user_id, business_id, review_id " +
                "From reviews " +
                "Where (stars LIKE '3.%' OR stars LIKE '4.%' or stars LIKE '5.%');";
        ResultSet res = SQLClient.client.queryFor(command);

        FileWriter myFile = new FileWriter(DATA_FILE_NAME);

        while (res.next()){
            String line = "";
            line = res.getString(1) + "," + res.getString(2)+","+res.getString(3)+",\n"; // userID,busID,reviewid

            myFile.write(line);
        }
        myFile.close();
    }

    /**
     * Method to map the downloaded file data to dictionaries.
     * @param userToBus The dictionary that will take a userID and return a list of businesses
     * @param BusToUser The dictionary that will take a BusinessID and return a list of users
     * @throws FileNotFoundException
     */
    private void mapDataFromFile(Dictionary<String, LinkedList<String>> userToBus, Dictionary<String, LinkedList<String>> BusToUser) throws FileNotFoundException {
        Scanner myFile = new Scanner(new File(DATA_FILE_NAME));

        while(myFile.hasNextLine()){
            String line = myFile.nextLine();
            String [] parts = line.split(",");

            LinkedList<String> t = userToBus.get(parts[0]);
            if(t != null){
                t.add(parts[1]);
            } else {
                t = new LinkedList<>();
                t.add(parts[1]);

                userToBus.put(parts[0], t);
            }

            t = BusToUser.get(parts[1]);
            if(t != null){
                t.add(parts[0]);
            } else {
                t = new LinkedList<>();
                t.add(parts[0]);

                BusToUser.put(parts[1], t);
            }
        }
    }

    /**
     * Method to find the shortest chain between 2 restaurants
     * @param id1 The id of the business to start the search at
     * @param id2 The id of the business to be the goal in the search
     * @return A string of usernames that form the chain
     * @throws SQLException
     */
    private String findShortestChain(String id1, String id2) throws SQLException {
        LinkedList<String> pathlist = null;
        try {
            pathlist = bfs(id1, id2);
        } catch (SQLException e){
            e.printStackTrace();
        }
        String result = "";

        for(String i : pathlist){
            ResultSet res = SQLClient.client.queryFor("Select name From users Where user_id = '"+i+"';");
            res.next();
            result += res.getString(1)+ ", ";
        }

        return result.substring(0,result.length()-2);
    }
}
