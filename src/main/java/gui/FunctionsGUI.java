package gui;

import java.awt.*;

/**
 * Class that implements some functions used in the GUI.
 */
public class FunctionsGUI {

    /**
     * Method that centers the window frame.
     *
     * @param frame to center.
     */
    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
