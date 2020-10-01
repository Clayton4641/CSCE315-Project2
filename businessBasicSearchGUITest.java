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
	}
}