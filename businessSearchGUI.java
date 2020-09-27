import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A class to contain all the needed components to build the GUI and display and update it.
 */
public class businessSearchGUI {

    private JFrame mainFame = new JFrame();

    private JLabel headerLabel = new JLabel("Filter for Business IDs");
    private JLabel separatorLabel = new JLabel("-----------------------------------");
    private JLabel businessNameLabel = new JLabel("Business Name:");
    private JLabel starsLabel = new JLabel("Stars:");
    private JLabel stateLabel = new JLabel("State:");
    private JLabel cityNameLabel = new JLabel("City Name:");
    private JLabel postalCodeLabel = new JLabel("Postal Code:");
    private JLabel streetLabel = new JLabel("Street:");
    private JLabel isOpenLabel = new JLabel("Is open:");
    private JLabel restaurantLabel = new JLabel("Restaurant:");
    private JLabel starsRangeLabel = new JLabel("to");
    private JLabel selectAllLabel = new JLabel("Select all");
    private JLabel saveToFileLabel = new JLabel("Save to file");

    private JTextField businessNameText = new JTextField();
    private JTextField stateText = new JTextField();
    private JTextField cityNameText = new JTextField();
    private JTextField postalCodeText = new JTextField();
    private JTextField streetText = new JTextField();

    private JCheckBox restaurantCheckBox = new JCheckBox();
    private JCheckBox isOpenCheckBox = new JCheckBox();
    private JCheckBox selectAllCheckBox = new JCheckBox();
    private JCheckBox saveToFileCheckBox = new JCheckBox();

    private JButton searchButton = new JButton("Search for IDs");

    private JComboBox lowerRangeStarsList = new JComboBox();
    private JComboBox upperRangeStarsList = new JComboBox();

    private JTable initialSearchResultTable = new JTable();

    private JPanel starsPanel = new JPanel();

    public boolean isStarted = false;

    private final int MAX_RESULT_DISPLAY_VALUE = 10;
    private final int MAX_RESULT_DISPLAY_WIDTH = 400;
    private final int MAX_RESULT_DISPLAY_LENGTH = 250;
    private final int TEXT_BOX_WIDTH = 125;

    private final String[] TABLE_BASIC_COLUMN_ITEMS = {"Business", "Business_ID"};

    private DefaultTableModel initialSearchResultsModel = new DefaultTableModel(TABLE_BASIC_COLUMN_ITEMS, 0);

    /**
     * Method to initialize any components that are not ready for use from initialization when the instance was made
     * This includes the following:
     * lowerRangeStarsList
     * upperRangeStarsList
     * starsPanel
     * searchButton
     * <p>
     * Then initializes and fills the main business frame and displays it.
     */
    public void start() {
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
        searchButton.addActionListener(new dataPuller());


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
    private JPanel makeGUI() {

        JPanel businessPanel = new JPanel(new GridBagLayout());

        JPanel searchInfoPanel = basicInformationInputGrid();

        JPanel searchResultPanel = basicSearchResultsPanel();

        JPanel filterDataPanel = new JPanel();

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        filterDataPanel.setLayout(new GridBagLayout());

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        filterDataPanel.add(headerLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        filterDataPanel.add(separatorLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        filterDataPanel.add(searchInfoPanel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        businessPanel.add(filterDataPanel, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        businessPanel.add(searchResultPanel, gridBagConstraints);

        return businessPanel;
    }

    /**
     * This method makes the panel for the input area to find businesses.
     * The panel is in a grid layout (8 rows x 3 cols).
     * Due to limitations of grid layout some empty containers are added to the panel to get correct placement.
     *
     * @return A JPanel with the input boxes to search for a business
     */
    private JPanel basicInformationInputGrid() {
        final int ROWS = 8;
        final int COLUMNS = 4;
        final int MAX_POSITIONS = ROWS * COLUMNS;

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
                case 6:
                    inputGrid.add(saveToFileLabel, gridConstraints);
                    break;
                case 7:
                    inputGrid.add(saveToFileCheckBox, gridConstraints);
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
     *
     * @return A panel where search results can be displayed and selected.
     */
    private JPanel basicSearchResultsPanel() {
        JPanel resultsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        initialSearchResultTable = new JTable(initialSearchResultsModel);

        JScrollPane tablePane = new JScrollPane(initialSearchResultTable);

        // It would be nice if the results table didn't have to be a fixed size,
        // however table panes don't seem to be easily configured this way
        tablePane.setPreferredSize(new Dimension(MAX_RESULT_DISPLAY_WIDTH, MAX_RESULT_DISPLAY_LENGTH));

        JPanel selectAllPanel = new JPanel();

        selectAllPanel.setLayout(new FlowLayout());

        selectAllPanel.add(selectAllLabel);
        selectAllPanel.add(selectAllCheckBox);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        resultsPanel.add(selectAllPanel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        resultsPanel.add(tablePane, gridBagConstraints);

        return resultsPanel;
    }

    /**
     * Method to take results from an initial filter search and place them into the initial search table.
     *
     * @param resultData The data from the initial search packed into basicBusinessDataFrames.
     */
    public void updateSearchPanel(Iterable<basicBusinessDataFrame> resultData) {
        if (!isStarted) {
            throw new NullPointerException();
        }
        ArrayList<Object[]> tableData = new ArrayList<>();

        for (basicBusinessDataFrame frame : resultData) {
            ArrayList<Object> frameData = new ArrayList<>();

            String businessName = frame.getBusinessName();
            String businessID = frame.getBusinessID();

//            frameData.add(Boolean.FALSE);
            frameData.add(businessName);
            frameData.add(businessID);

            Object[] arrayData = frameData.toArray();

            tableData.add(arrayData);
        }

        for (Object[] i : tableData) {
            initialSearchResultsModel.addRow(i);
        }

        initialSearchResultsModel.fireTableDataChanged();
    }

    /**
     * Method to pull all the selected rows out of the initial search table.
     * @return An Iterable of basicBusinessDataFrames
     */
    public Iterable<basicBusinessDataFrame> getSelectedInitialTableItems(){
        List<basicBusinessDataFrame> dataFrames = new LinkedList<>();

        int [] rows = initialSearchResultTable.getSelectedRows();
        int businessNameIndex = 0;
        int businessIDIndex = 1;

        for(int i : rows){
            String name = initialSearchResultTable.getValueAt(i,businessNameIndex).toString();
            String ID = initialSearchResultsModel.getValueAt(i,businessIDIndex).toString();

            dataFrames.add(new basicBusinessDataFrame(name,ID));
        }

        return dataFrames;
    }

    /**
     * Helper method to map a given 1d index in a grid layout to a 2d index for a grid bag layout.
     *
     * @param index   the 1d index to convert.
     * @param columns the number of columns in the 2d grid we are mapping to.
     * @return An array of ints in the form [row,column]
     */
    private int[] indexConverter(int index, int columns) {
        int row = index % columns;
        int col = index / columns;

        int[] indexes = {row, col};

        return indexes;
    }

    /**
     * ActionListener that is linked to the search button, pulls data for a basic search and is intended to pass a
     * basicFilterDataFrame to the filter search
     */
    private class dataPuller implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String businessName = businessNameText.getText();
            String state = stateText.getText();
            String city = cityNameText.getText();
            String postal = cityNameText.getText();
            String street = streetText.getText();

            int lowerStarsIndex = lowerRangeStarsList.getSelectedIndex();
            int upperStarsIndex = upperRangeStarsList.getSelectedIndex();

            int lowerStars = lowerStarsIndex + 1; //since the index is one lower than the real value, add one to the index
            int upperStars = upperStarsIndex + 1;

            boolean isOpen = isOpenCheckBox.isSelected();
            boolean isRestaurant = restaurantCheckBox.isSelected();
            boolean saveToFile = saveToFileCheckBox.isSelected();

//            if (lowerStarsIndex > upperStarsIndex) {
//                throw new IndexOutOfBoundsException("Selected lower stars value is larger than upper stars");
//            }

            basicFilterDataFrame packedData = new basicFilterDataFrame(businessName, state, city, postal, street, lowerStars
                    , upperStars, isOpen, isRestaurant, saveToFile);

            //@TODO add the call to complete the search given the packed data frame.
        }
    }
}
