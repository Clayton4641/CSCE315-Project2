import javax.swing.*;
import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class businessSearchGUI {

    private static JFrame mainFame = new JFrame();

    private static JLabel headerLabel = new JLabel("Filter for Business IDs");
    private static JLabel separatorLabel = new JLabel("-----------------------------------");
    private static JLabel businessNameLabel = new JLabel("Business Name:");
    private static JLabel starsLabel = new JLabel("Stars:");
    private static JLabel stateLabel = new JLabel("State:");
    private static JLabel cityNameLabel = new JLabel("City Name:");
    private static JLabel postalCodeLabel = new JLabel("Postal Code:");
    private static JLabel streetLabel = new JLabel("Street:");
    private static JLabel isOpenLabel = new JLabel("Is open:");
    private static JLabel restaurantLabel = new JLabel("Restaurant:");
    private static JLabel starsRangeLabel = new JLabel("to");
    private static JLabel selectAllLabel = new JLabel("Select all");

    private static JTextField businessNameText = new JTextField();
    private static JTextField stateText = new JTextField();
    private static JTextField cityNameText = new JTextField();
    private static JTextField postalCodeText = new JTextField();
    private static JTextField streetText = new JTextField();

    private static JCheckBox restaurantCheckBox = new JCheckBox();
    private static JCheckBox isOpenCheckBox = new JCheckBox();
    private static JCheckBox selectAllCheckBox = new JCheckBox();

    private static JButton searchButton = new JButton("Search for IDs");

    private static JComboBox lowerRangeStarsList = new JComboBox();
    private static JComboBox upperRangeStarsList = new JComboBox();

    private static JTable searchResultTable = new JTable();

    private static JPanel starsPanel = new JPanel();
    private static JPanel searchResultsPanel = new JPanel();

    public static boolean isStarted = false;

    private static final int MAX_RESULT_DISPLAY_VALUE = 10;
    private static final int MAX_RESULT_DISPLAY_WIDTH = 100;
    private static final int MAX_RESULT_DISPLAY_LENGTH = 100;

    private static final String [] TABLE_BASIC_COLUMN_ITEMS = {"Business","Business_ID"};

    /**
     * Method to initialize any components that are not ready for use from initialization when the class was made
     * This includes the following:
     * lowerRangeStarsList
     * upperRangeStarsList
     * starsPanel
     * searchButton
     *
     * Then initializes and fills the main business frame and displays it.
     */
    public static void start() {
        //first set up the starsPanel, lowerRangeStarsList, upperRangeStarsList
        ArrayList<String> rangeElements = new ArrayList<>();

        int minRangeValue = 1;
        int maxRangeValue = 5;

        for (int i = minRangeValue; i <= maxRangeValue; i++) {
            rangeElements.add(Integer.toString(i));
        }

        lowerRangeStarsList = new JComboBox(rangeElements.toArray());
        upperRangeStarsList = new JComboBox(rangeElements.toArray());

        lowerRangeStarsList.setSelectedIndex(0);
        upperRangeStarsList.setSelectedIndex(maxRangeValue - 1);

        starsPanel = new JPanel(new FlowLayout());

        starsPanel.add(lowerRangeStarsList);
        starsPanel.add(starsRangeLabel);
        starsPanel.add(upperRangeStarsList);

        //last we finish setting up the searchButton
//        searchButton.addActionListener(new searchDataPuller());


        mainFame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFame.setLayout(new GridLayout());

        mainFame.add(makeGUI());

        mainFame.pack(); // this sets the dimensions of the GUI automatically

        mainFame.setVisible(true);

        isStarted = true;
    }

    /**
     * @return A JPanel that would be used for the filter for businesses section of the GUI
     */
    private static JPanel makeGUI() {


        JPanel businessPanel = new JPanel(new GridBagLayout());

        JPanel searchInfoPanel = basicInformationInputGrid();

        JPanel searchResultPanel = basicSearchResultsPanel();

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        businessPanel.add(headerLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        businessPanel.add(separatorLabel, gridBagConstraints);


        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        businessPanel.add(searchInfoPanel, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        businessPanel.add(searchResultPanel,gridBagConstraints);

        return businessPanel;
    }

    /**
     * This method makes the panel for the input area to find businesses.
     * The panel is in a grid layout (8 rows x 3 cols).
     * Due to limitations of grid layout some empty containers are added to the panel to get correct placement.
     *
     * @return A JPanel with the input boxes to search for a business
     */
    private static JPanel basicInformationInputGrid() {
        final int ROWS = 8;
        final int COLUMNS = 4;
        final int MAX_POSITIONS = ROWS * COLUMNS;
        final int TEXT_BOX_WIDTH = 75;

        JPanel inputGrid = new JPanel();

        inputGrid.setLayout(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();

        for (int i = 0; i < MAX_POSITIONS; i++) {
            int[] gridPositions = indexConverter(i, COLUMNS);
            gridConstraints.gridx = gridPositions[0];
            gridConstraints.gridy = gridPositions[1];
            switch (i) {
                case 0:
                    inputGrid.add(businessNameLabel, gridConstraints);
                    break;
                case 1:
                    gridConstraints.ipadx = TEXT_BOX_WIDTH;
                    inputGrid.add(businessNameText, gridConstraints);
                    break;
                case 2:
                    inputGrid.add(restaurantLabel, gridConstraints);
                    break;
                case 3:
                    inputGrid.add(restaurantCheckBox, gridConstraints);
                    break;
                case 4:
                    inputGrid.add(starsLabel, gridConstraints);
                    break;
                case 5:
                    inputGrid.add(starsPanel, gridConstraints);
                    break;
                case 8:
                    inputGrid.add(stateLabel, gridConstraints);
                    break;
                case 9:
                    gridConstraints.ipadx = TEXT_BOX_WIDTH;
                    inputGrid.add(stateText, gridConstraints);
                    break;
                case 12:
                    inputGrid.add(cityNameLabel, gridConstraints);
                    break;
                case 13:
                    gridConstraints.ipadx = TEXT_BOX_WIDTH;
                    inputGrid.add(cityNameText, gridConstraints);
                    break;
                case 16:
                    inputGrid.add(postalCodeLabel, gridConstraints);
                    break;
                case 17:
                    gridConstraints.ipadx = TEXT_BOX_WIDTH;
                    inputGrid.add(postalCodeText, gridConstraints);
                    break;
                case 20:
                    inputGrid.add(streetLabel, gridConstraints);
                    break;
                case 21:
                    gridConstraints.ipadx = TEXT_BOX_WIDTH;
                    inputGrid.add(streetText, gridConstraints);
                    break;
                case 24:
                    inputGrid.add(isOpenLabel, gridConstraints);
                    break;
                case 25:
                    inputGrid.add(isOpenCheckBox, gridConstraints);
                    break;
                case 28:
                    inputGrid.add(searchButton, gridConstraints);
                    break;
                default:
                    inputGrid.add(new JLabel(), gridConstraints);
                    break;
            }
            gridConstraints.ipadx = 0;
        }

        return inputGrid;
    }

    /**
     * Creates a panel to be used for displaying and selecting items from our initial search.
     * @return A panel where search results can be displayed and selected.
     */
    private static JPanel basicSearchResultsPanel() {
        JPanel resultsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

//        DefaultListModel listModel = new DefaultListModel();
//
//        searchResultList = new JList(listModel);
//
//        searchResultList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//        searchResultList.setVisibleRowCount(MAX_RESULT_DISPLAY_VALUE);

        // This table model might need to be global
        DefaultTableModel tableModel = new DefaultTableModel(TABLE_BASIC_COLUMN_ITEMS,0);

        searchResultTable = new JTable(tableModel);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        resultsPanel.add(selectAllLabel,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        resultsPanel.add(selectAllCheckBox,gridBagConstraints);

        /**
         * @TODO the searchResultTable needs to go into its own scroll pane and be configured some so it doesn't take up the whole box area
         */

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth=3;
        gridBagConstraints.ipadx = MAX_RESULT_DISPLAY_WIDTH;
        gridBagConstraints.ipady = MAX_RESULT_DISPLAY_LENGTH;
        resultsPanel.add(new JScrollPane(searchResultTable),gridBagConstraints);

        return resultsPanel;
    }

    public static void updateSearchPanel(Iterable<DataFrame> resultData){
        if(!isStarted){
            throw new NullPointerException();
        }
        /**
         * @TODO This will add all components from a given container and display them in the search panel.
         * Maybe record search data in another container for faster return?
         */
        ArrayList<String> header = new ArrayList<>();
        ArrayList<String[]> tableData = new ArrayList<>();

        for(DataFrame frame : resultData){
            ArrayList<String> frameData = new ArrayList<>();

            String businessName = frame.getBusinessName();
            String businessID = frame.getBusinessID();

            frameData.add(businessName);
            frameData.add(businessID);

            String [] arrayData = tableData.toArray(new String[0]);

            tableData.add(arrayData);

        }


    }

    /**
     * Helper method to map a given 1d index in a grid layout to a 2d index for a grid bag layout.
     *
     * @param index   the 1d index to convert.
     * @param columns the number of columns in the 2d grid we are mapping to.
     * @return An array of ints in the form [row,column]
     */
    private static int[] indexConverter(int index, int columns) {
        int row = index % columns;
        int col = index / columns;

        int[] indexes = {row, col};

        return indexes;
    }
}
