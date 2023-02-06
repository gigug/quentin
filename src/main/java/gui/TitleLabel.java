package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Class that implements the labels shown in the different panels of the GUI.
 *
 * @author Gianluca Guglielmo
 */
class TitleLabel extends JLabel {
    private final static Font FONT_TITLE = new Font("monospaced", Font.BOLD, 35);
    private final static Color FONT_COLOR = Color.decode("#E1F2FE");

    /**
     * Default constructor for the class Label.
     */
    TitleLabel(){
        super();
        setFont(FONT_TITLE);
        setForeground(FONT_COLOR);
    }

    /**
     * Custom constructor for the class Label.
     *
     * @param text string representing the text to be displayed.
     */
    TitleLabel(String text) {
        this();
        setText(text);
    }
}
