import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class to contain the creation of the shortest chain search GUI.
 */
public class shortestChainGUI {
    private JLabel shortestChainLabel = new JLabel("Shortest Chain");
    private JLabel restaurant1Label = new JLabel("Restaurant One:");
    private JLabel restaurant2Label = new JLabel("Restaurant Two:");
    private JLabel separatorLabel = new JLabel("-----------------------------------------------------------------------------------------------");

    private JTextField restaurant1Test = new JTextField();
    private JTextField restaurant2Text = new JTextField();
    private JTextArea resultArea = new JTextArea();

    private JButton shortestChainButton = new JButton("Find Shortest Chain"); // need to link to action listened

    private final int TEXT_BOX_WIDTH = 100;

    /**
     * Builds and starts the shortest chain GUI.
     */
    public void start(){
        shortestChainButton.addActionListener(new shortestChainAction());

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
        resultArea.setColumns(40);
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

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
//        gridBagConstraints.ipadx = 25;
//        gridBagConstraints.ipady = 25;
        gridBagConstraints.gridwidth = 2;
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
                // do something
                String s = findShortestChain(id1,id2);
                System.out.println(s);
            }
        }
    }

    private static String buildUserIdGetter(String businessId){
        return "SELECT user_id " + "From reviews" + " Where (business_id = '" + businessId + "') and (stars LIKE '3.%' OR stars LIKE '4.%' or stars LIKE '5.%');";
    }

    private static String buildBusinessIdGetter(String userID){
        return "SELECT business_id " + "From reviews" + " Where (user_id = '"+ userID +"') and (stars LIKE '3.%' OR stars LIKE '4.%' or stars LIKE '5.%');";
    }

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

    private LinkedList<String> bfs(String startID, String targetID) throws SQLException {
        Queue<node> toVisit = new LinkedList<>();
        Set<String> visitedBusinesses = new HashSet<>();
        Set<String> visitedCustomers = new HashSet<>();

        visitedBusinesses.add(startID);
        toVisit.add(new node(startID));

        while(!toVisit.isEmpty()){
            node v = toVisit.remove();

            if (v.id.compareTo(targetID) == 0){
                return v.path;
            }
            // get the users that have been here
            ResultSet userIDTable = SQLClient.client.queryFor(buildUserIdGetter(v.id));
            // 5YvcrqwD4irC_-j-vNC5TA
            // uYn9um4e0ymbhneJ2VLoYg

            while(userIDTable.next()){
                String userID = userIDTable.getString(1);

                if (!visitedCustomers.contains(userID)){

                    visitedCustomers.add(userID);

                    ResultSet businessIDTable = SQLClient.client.queryFor(buildBusinessIdGetter(userID));

                    while (businessIDTable.next()){
                        String businessId = businessIDTable.getString(1);

                        if (!visitedBusinesses.contains(businessId)){

                            visitedBusinesses.add(businessId);
                            LinkedList<String> temp = new LinkedList<>();
                            temp.addAll(v.path);
                            temp.add(userID);
                            toVisit.add(new node(businessId,temp));
                        }
                    }
                }
            }
        }
        return null;
    }

    private String findShortestChain(String id1, String id2){
        LinkedList<String> pathlist = null;
        try {
            pathlist = bfs(id1, id2);
        } catch (SQLException e){
            e.printStackTrace();
        }
        String result = "";

        for(String i : pathlist){
            result += i;
            result += " -> ";
        }

        return result;
    }
}
