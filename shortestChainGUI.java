import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            String city1 = restaurant1Test.getText().strip();
            String city2 = restaurant2Text.getText().strip();

            if (city1.isEmpty() || city2.isEmpty()){
                resultArea.setText("Both restaurants must be entered.");
            }
            else {
                // do something
            }
        }
    }
}
