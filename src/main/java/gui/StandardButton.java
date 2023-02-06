package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Class that implements black JButtons used in the Quentin GUI.
 *
 * @author Gianluca Guglielmo
 */
class StandardButton extends JButton{

    private final static Font FONT_BUTTON = new Font("monospaced", Font.PLAIN, 20);

    /**
     * BlackButton constructor.
     *
     * @param text string representing the text to be displayed.
     */
    StandardButton(String text) {
        super(text);

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setFont(FONT_BUTTON);
    }
}