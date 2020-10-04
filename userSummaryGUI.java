import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.AccessibleObject;

/**
 * Class to contain the creation of the user summary GUI.
 */
public class userSummaryGUI {
    private JLabel userSummaryLabel = new JLabel("User Summary");
    private JLabel separatorLabel4 = new JLabel("-----------------------------------------------------------------------------------------------");
    private JLabel userIDLabel = new JLabel("User ID:");
    private JLabel funnyAverageLabel = new JLabel("Average funny rating:");
    private JLabel coolAverageLabel = new JLabel("Average cool rating:");
    private JLabel usefulAverageLabel = new JLabel("Average useful rating:");
    private JLabel funnyAverageResultsLabel = new JLabel();
    private JLabel coolAverageResultsLabel = new JLabel();
    private JLabel usefulAverageResultsLabel = new JLabel();

    private JTextField userIDText = new JTextField();

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
        userSum.add(userIDText,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 0;
        userSum.add(usefulAverageLabel,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        userSum.add(usefulAverageResultsLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        userSum.add(funnyAverageLabel,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        userSum.add(funnyAverageResultsLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        userSum.add(coolAverageLabel,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        userSum.add(coolAverageResultsLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        userSum.add(userSearchButton,gridBagConstraints);

        return userSum;
    }

    /**
     * Action listener to trigger when the user hits the search button.
     */
    private class searchHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String userID = userIDText.getText().strip();

            if (userID.isEmpty()){
                usefulAverageResultsLabel.setText("Must enter an user ID.");
            } else {
                // do something
            }
        }
    }
}
