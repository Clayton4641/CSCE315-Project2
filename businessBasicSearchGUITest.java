import javax.swing.*;
import java.awt.*;
import java.security.DrbgParameters.Reseed;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import static java.lang.Thread.sleep;

public class businessBasicSearchGUITest {
    public static void main(String []args) throws InterruptedException {


        businessSearchGUI testGUI = new businessSearchGUI();
        testGUI.start();

       /* ArrayList<basicBusinessDataFrame> testFrames = new ArrayList<>();
        sleep(1000);// pause so that the frame and load correctly
        for(int i = 0; i < 100; i++){

			String[] data = {"col1_" + i, "col2_" + i, "col3_" + i};

            testFrames.add(new basicBusinessDataFrame(data));
        }

		String[] headers = {"Test 1", "Test 2", "Test 3"};
        testGUI.updateSearchPanel(testFrames, headers);
        sleep(5000); // pause to select some data
        Iterable<basicBusinessDataFrame> testReturnData = testGUI.getSelectedInitialTableItems();

        for(basicBusinessDataFrame i : testReturnData){ // display selected data
            System.out.println(i.getColumn(1));
		}*/
/*

		System.out.println("Starting query...");
		ResultSet result = client.queryFor("SELECT * FROM public.businesses ORDER BY business_id ASC LIMIT 100");
		System.out.println("Query finished!");

		try
		{
			while (result.next())
			{
				System.out.println("Business ID: " + result.getString("business_id") + "Name: " + result.getString("name"));
			}
		}
		catch (Exception e)
		{
			System.out.println("Error occured");
		}*/
	}
}