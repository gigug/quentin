package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Class that implements black JButtons used in the Quentin GUI.
 *
 * @author Gianluca Guglielmo
 */
class BlackButton extends JButton{

    /**
     * BlackButton constructor.
     *
     * @param text string representing the text to be displayed.
     */
    BlackButton(String text) {
        super(text);

        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setFont(GameGUI.FONT_SMALL);
    }
}