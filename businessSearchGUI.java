import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.sql.*;

/**
 * A class to contain all the needed components to build the GUI and display and update it.
 */
public class businessSearchGUI {

    //Main Search GUI Items/////////////////////////////////////////////////////////////////////////////////////////////

    private JFrame mainFrame = new JFrame();

    private JLabel headerLabel = new JLabel("Filter for Business IDs");
    private JLabel separatorLabel = new JLabel("-----------------------------------------------------------------------------------------------");
    private JLabel separatorLabel2 = new JLabel("-----------------------------------------------------------------------------------------------");
    private JLabel separatorLabel3 = new JLabel("-----------------------------------------------------------------------------------------------");
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
    private JLabel specialSearchLabel = new JLabel("Special Searches");

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
    private JButton shortestChainButton = new JButton("Shortest Chain Search");
    private JButton userSummaryButton = new JButton("User Summary");
    private JButton furthestSpreadButton = new JButton("Furthest Spread Search");
    private JButton bestLocalButton = new JButton("Best Local Restaurant Search");

    private JComboBox lowerRangeStarsList = new JComboBox();
    private JComboBox upperRangeStarsList = new JComboBox();

    private JTable initialSearchResultTable = new JTable();

    private JPanel starsPanel = new JPanel();

    private JTabbedPane tabsPane = new JTabbedPane();

    public boolean isStarted = false;

    private final int MAX_RESULT_DISPLAY_VALUE = 10;
    private final int MAX_RESULT_DISPLAY_WIDTH = 500;
    private final int MAX_RESULT_DISPLAY_LENGTH = 500;
    private final int TEXT_BOX_WIDTH = 100;

    
    private JLabel additionalInfoLabel = new JLabel("Display Additional Information");
    private JLabel businessIDsLabel = new JLabel("Business ids:");
    private JLabel hoursLabel = new JLabel("Hours:");
    private JLabel parkingLabel = new JLabel("Parking:");
    private JLabel addressLabel = new JLabel("Address:");
    private JLabel reviewsLabel = new JLabel("Reviews:");
    private JLabel tipsLabel = new JLabel("Tips:");
    private JLabel restaurantInfoLabel = new JLabel("Restaurant Info:");
    private JLabel goodForMealLabel = new JLabel("Good For Meal");
    private JLabel ambienceLabel = new JLabel("Ambience");
    private JLabel dietaryRestrictionsLabel = new JLabel("Dietary Restrictions");

    private JCheckBox businessIdsCheckBox = new JCheckBox();

    private JCheckBox hoursCheckBox = new JCheckBox();
    private JCheckBox parkingCheckBox = new JCheckBox();
    private JCheckBox reviewsCheckBox = new JCheckBox();
    private JCheckBox addressCheckBox = new JCheckBox();
    private JCheckBox tipsCheckBox = new JCheckBox();
    private JCheckBox restaurantInfoCheckBox = new JCheckBox();

    private JButton queryButton = new JButton("Query");
    
    String[] goodForMeal = 	{"None","lunch", "dinner", "brunch", "breakfast", "latenight", "desert"};
    String[] ambience = {"None","touristy", "hipster", "romantic", "intimate", "upscale", "classy", "casual", "divey"};
    String[] dietaryRestrictions = {"None","dairy-free", "gluten-free","vegan", "halal", "kosher", "soy-free", "vegetarian"};

    private JComboBox<String> goodForMealList = new JComboBox<String>(goodForMeal);
    private JComboBox<String> ambienceList = new JComboBox<String>(ambience);
    private JComboBox<String> dietaryRestrictionsList = new JComboBox<String>(dietaryRestrictions);
    private final String[] TABLE_BASIC_COLUMN_ITEMS = {"Business", "Business_ID"};
	private DefaultTableModel initialSearchResultsModel = new DefaultTableModel(TABLE_BASIC_COLUMN_ITEMS, 0);

	//Main GUI Items End////////////////////////////////////////////////////////////////////////////////////////////////

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
		
		// Connect to database.
		SQLClient.client = new JDBCpostgreSQLClient("jdbc:postgresql://csce-315-db.engr.tamu.edu/Team912_D16_DB", "username", "password");
		
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

        ambienceList.setSelectedIndex(0);
        goodForMealList.setSelectedIndex(0);
        dietaryRestrictionsList.setSelectedIndex(0);

        starsPanel = new JPanel(new FlowLayout());

        starsPanel.add(lowerRangeStarsList);
        starsPanel.add(starsRangeLabel);
        starsPanel.add(upperRangeStarsList);

//        tabsPane.add("Main search",makeGUI());
//        tabsPane.add(shortestPathTabName,makeShortestChainPanel());

        //last we finish setting up the searchButton
        searchButton.addActionListener(new dataPuller());

        shortestChainButton.addActionListener(new shortestChainHandler());
        userSummaryButton.addActionListener(new userSummaryHandler());
        furthestSpreadButton.addActionListener(new furthestSpreadHandler());
        bestLocalButton.addActionListener(new bestLocalHandler());

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.add(makeGUI());

        mainFrame.setTitle("Main Search");

        mainFrame.pack(); // this sets the dimensions of the GUI automatically

        mainFrame.setVisible(true);

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

        JPanel specialSearchPanel = specialSearchPanel();

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        filterDataPanel.setLayout(new GridBagLayout());

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        filterDataPanel.add(headerLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        filterDataPanel.add(separatorLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
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

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
//        gridBagConstraints.gridwidth = 10;
        filterDataPanel.add(specialSearchPanel, gridBagConstraints);

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
        final int ROWS = 18;
        final int COLUMNS = 4;
        final int MAX_POSITIONS = ROWS * COLUMNS;

        JPanel inputGrid = new JPanel();

        inputGrid.setLayout(new GridBagLayout());

//        inputGrid.setBorder(BorderFactory.createLineBorder(Color.black));

        GridBagConstraints gridConstraints = new GridBagConstraints();

        for (int i = 0; i < MAX_POSITIONS; i++) {
            int[] gridPositions = indexConverter(i, COLUMNS);
            gridConstraints.gridx = gridPositions[0];
            gridConstraints.gridy = gridPositions[1];
            gridConstraints.ipadx = 5;
            gridConstraints.ipady = 5;
            gridConstraints.anchor = GridBagConstraints.WEST;
            gridConstraints.gridwidth = 1;
            switch (i) {
                case 0:
                    gridConstraints.anchor = GridBagConstraints.WEST;
                    inputGrid.add(businessNameLabel, gridConstraints);
                    break;
                case 1:
                    gridConstraints.ipadx = TEXT_BOX_WIDTH;
                    inputGrid.add(businessNameText, gridConstraints);
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
                    gridConstraints.anchor = GridBagConstraints.WEST;
                    inputGrid.add(isOpenCheckBox, gridConstraints);
                    break;
                case 28:
                    inputGrid.add(restaurantLabel, gridConstraints);
                    break;
                case 29:
                    gridConstraints.anchor = GridBagConstraints.WEST;
                    inputGrid.add(restaurantCheckBox, gridConstraints);
                    break;
                case 32:
                    inputGrid.add(ambienceLabel, gridConstraints);
                    break;
                case 33:
                    gridConstraints.anchor = GridBagConstraints.WEST;
                    inputGrid.add(ambienceList, gridConstraints);
                    break;
                case 36:
                    inputGrid.add(goodForMealLabel, gridConstraints);
                    break;
                case 37:
                    gridConstraints.anchor = GridBagConstraints.WEST;
                    inputGrid.add(goodForMealList, gridConstraints);
                    break;
                case 40:
                    inputGrid.add(dietaryRestrictionsLabel, gridConstraints);
                    break;
                case 41:
                    gridConstraints.anchor = GridBagConstraints.WEST;
                    inputGrid.add(dietaryRestrictionsList, gridConstraints);
                    break;
                case 44:
                    inputGrid.add(additionalInfoLabel,gridConstraints);
                    break;
                case 48:
                    gridConstraints.anchor = GridBagConstraints.WEST;
                    gridConstraints.gridwidth = 2;
                    inputGrid.add(separatorLabel2,gridConstraints);
                    break;
                case 52:
                    inputGrid.add(businessIDsLabel,gridConstraints);
                    break;
                case 53:
                    gridConstraints.anchor = GridBagConstraints.WEST;
                    inputGrid.add(businessIdsCheckBox,gridConstraints);
                case 56:
                    inputGrid.add(addressLabel,gridConstraints);
                    break;
                case 57:
                    inputGrid.add(addressCheckBox,gridConstraints);
                    break;
                case 60:
                    inputGrid.add(parkingLabel,gridConstraints);
                    break;
                case 61:
                    inputGrid.add(parkingCheckBox,gridConstraints);
                    break;
                case 64:
                    inputGrid.add(saveToFileLabel,gridConstraints);
                    break;
                case 65:
                    inputGrid.add(saveToFileCheckBox,gridConstraints);
                    break;
                case 68:
                    inputGrid.add(searchButton,gridConstraints);
                    break;
//                default:
//                    inputGrid.add(new JLabel(), gridConstraints);
//                    break;
            }
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

//        selectAllPanel.add(selectAllLabel);
//        selectAllPanel.add(selectAllCheckBox);

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
     * Method to make the last piece of the main GUI, contains all the buttons to do special searches
     * @return A JPanel built with all the search buttons
     */
    private JPanel specialSearchPanel(){
        JPanel specialSearch = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

//        gridBagConstraints.anchor = GridBagConstraints.WEST;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        specialSearch.add(specialSearchLabel,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        specialSearch.add(separatorLabel3,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        specialSearch.add(shortestChainButton,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        specialSearch.add(userSummaryButton,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        specialSearch.add(furthestSpreadButton,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        specialSearch.add(bestLocalButton,gridBagConstraints);

        return specialSearch;
    }

    /**
     * Method to take results from an initial filter search and place them into the initial search table.
     *
     * @param resultData The data from the initial search packed into basicBusinessDataFrames.
     */
    private void updateSearchPanel(ResultSet resultData) {
		if (!isStarted) {
            throw new NullPointerException();
		}

		FileWriter writer = null;
		
		try {
			writer = new FileWriter("table.csv"); 
		} catch (Exception e) {
			System.out.println("Writer not open");
		}

		initialSearchResultsModel.setColumnCount(0);
		initialSearchResultsModel.setRowCount(0);

		String[] headers = {};

		// Column Headers
		try {
			ResultSetMetaData metadata = resultData.getMetaData();

			System.out.println(metadata.getColumnCount());
			headers = new String[metadata.getColumnCount()];

			for (int i = 1; i <= metadata.getColumnCount(); ++i) {
				System.out.println(metadata.getColumnLabel(i));
				headers[i - 1] = metadata.getColumnLabel(i);
			}
		} catch (Exception e) {
			System.out.println("Bad metadata");
		}

		initialSearchResultsModel.setColumnCount(headers.length);
		initialSearchResultsModel.setColumnIdentifiers(headers);

		try {
			if (saveToFileCheckBox.isSelected()) {
				for (int i = 0; i < headers.length; ++i) {

					if (writer != null) {
						System.out.println("Woo");
					}

					writer.append(headers[i]);
	
					if (i != headers.length - 1) {
						writer.append(",");
					}
					else {
						writer.append("\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		// Table Rows
		try {
			while (resultData.next()) {
				String[] data = new String[headers.length];
				
				for (int i = 0; i < data.length; ++i)
				{
					data[i] = resultData.getString(i + 1);

					if(saveToFileCheckBox.isSelected()) {			    	    
			    	    try { 
			    	        // create FileWriter object with file as parameter 
			    	        if(i != data.length-1) {
			    	        	writer.append(data[i]);
			    	        	writer.append(",");
			    	        }
			    	        else {
			    	        	writer.append(data[i]);
			    	        	writer.append("\n");
			    	        }
			    	    } 
			    	    catch (IOException e) { 
			    	        // TODO Auto-generated catch block 
			    	        e.printStackTrace(); 
			    	    } 
			    	} 
				}

				initialSearchResultsModel.addRow(data);
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
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

        if (selectAllCheckBox.isSelected()){
            int numberOfRows = initialSearchResultTable.getRowCount();
            rows = new int[numberOfRows];

            for(int i = 0;i<numberOfRows;i++){
                rows[i] = i;
            }
        }

        for(int i : rows){
            String name = initialSearchResultTable.getValueAt(i,businessNameIndex).toString();
            String ID = initialSearchResultsModel.getValueAt(i,businessIDIndex).toString();

			String[] data = {name, ID};
            dataFrames.add(new basicBusinessDataFrame(data));
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
            String postal = postalCodeText.getText();
            String street = streetText.getText();

            String ambience = (ambienceList.getSelectedObjects())[0].toString();;
            String goodForMeal = (goodForMealList.getSelectedObjects())[0].toString();
            String dietaryRestrictions = (dietaryRestrictionsList.getSelectedObjects()[0]).toString();

            int lowerStarsIndex = lowerRangeStarsList.getSelectedIndex();
            int upperStarsIndex = upperRangeStarsList.getSelectedIndex();

            int lowerStars = lowerStarsIndex + 1; //since the index is one lower than the real value, add one to the index
            int upperStars = upperStarsIndex + 1;

            boolean isOpen = isOpenCheckBox.isSelected();
            boolean isRestaurant = restaurantCheckBox.isSelected();
            boolean saveToFile = saveToFileCheckBox.isSelected();
            boolean businessIDs = businessIdsCheckBox.isSelected();
            boolean address = addressCheckBox.isSelected();
            boolean parking = parkingCheckBox.isSelected();

            /*
            if (isRestaurant){
                Object [] obj = ambienceList.getSelectedObjects();
                ambience = new String[obj.length];
                System.arraycopy(obj,0,ambience,0,obj.length);

                obj = goodForMealList.getSelectedObjects();
                goodForMeal = new String[obj.length];
                System.arraycopy(obj,0,goodForMeal,0,obj.length);

                obj = dietaryRestrictionsList.getSelectedObjects();
                dietaryRestrictions = new String[obj.length];
                System.arraycopy(obj,0,dietaryRestrictions,0,obj.length);
            }
            */


//            if (lowerStarsIndex > upperStarsIndex) {
//                throw new IndexOutOfBoundsException("Selected lower stars value is larger than upper stars");
//            }

            basicFilterDataFrame packedData = new basicFilterDataFrame(businessName, state, city, postal, street, lowerStars
                    , upperStars, isOpen, isRestaurant, saveToFile,businessIDs,address,parking,ambience,goodForMeal,dietaryRestrictions);

			String q = packedData.getQuery();

			System.out.println(q);

			ResultSet result = SQLClient.client.queryFor(q);
			System.out.println("DONE WITH QUERY");

			updateSearchPanel(result);
        }
    }

    /**
     * ActionListener that is linked to the shortest chain button, opens a new window to search
     */
    private class shortestChainHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            shortestChainGUI shortest = new shortestChainGUI();
            shortest.start();
        }
    }

    /**
     * ActionListener that is linked to the user summary button, opens a new window to search
     */
    private class userSummaryHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            userSummaryGUI userSum = new userSummaryGUI();
            userSum.start();
        }
    }

    /**
     * ActionListener that is linked to the furthest spread button, opens a new window to search
     */
    private class furthestSpreadHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            furthestSpreadGUI furthest = new furthestSpreadGUI();
            furthest.start();
        }
    }

    /**
     * ActionListener that is linked to the best local button, opens a new window to search
     */
    private class bestLocalHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            bestLocalGUI best = new bestLocalGUI();
            best.start();
        }
    }

}
