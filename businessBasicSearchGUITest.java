import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class businessBasicSearchGUITest {
    public static void main(String []args) throws InterruptedException {

        businessSearchGUI testGUI = new businessSearchGUI();
        testGUI.start();

        ArrayList<basicBusinessDataFrame> testFrames = new ArrayList<>();
        sleep(1000);// pause so that the frame and load correctly
        for(int i = 0;i<100;i++){
            testFrames.add(new basicBusinessDataFrame("test"+i,"id"+i));
        }

        testGUI.updateSearchPanel(testFrames);
        sleep(5000); // pause to select some data
        Iterable<basicBusinessDataFrame> testReturnData = testGUI.getSelectedInitialTableItems();

        for(basicBusinessDataFrame i : testReturnData){ // display selected data
            System.out.println(i.getBusinessName());
        }
    }
}