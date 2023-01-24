package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Class that implements black JButtons used in the Quentin GUI.
 */
public class BlackButton extends JButton{

    /**
     * BlackButton constructor.
     *
     * @param text string representing the text to be displayed.
     */
    public BlackButton(String text) {
        super(text);

        // Set style
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setFont(StartGUI.FONT_SMALL);
    }
}