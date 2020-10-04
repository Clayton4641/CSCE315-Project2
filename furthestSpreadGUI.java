import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            String cityName = stateNameText.getText().strip();

            if (cityName.isEmpty()){
                resultsText.setText("A state must be entered.");
            } else {
                //do something
            }
        }
    }
}
