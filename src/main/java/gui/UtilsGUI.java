package gui;

import players.PlayerColor;
import screens.Game;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Stack;

/**
 * Utility class with several methods used from other GUI classes.
 *
 * @author Gianluca Guglielmo.
 */
class UtilsGUI {

    /**
     * Private constructor for UtilsGUI, cannot be used.
     */
    private UtilsGUI() {}

    /**
     * Method that centers the window frame.
     *
     * @param frame to center.
     */
    static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

}



