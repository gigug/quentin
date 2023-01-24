package gui;

import javax.swing.*;

/**
 * Class that implements the labels shown in the different panels of the GUI.
 */
public class Label extends JLabel {

    /**
     * Default constructor for the class Label.
     */
    public Label(){
        super();
        setFont(GameGUI.FONT_BIG);
        setForeground(GameGUI.FONT_COLOR);
    }

    /**
     * Custom constructor for the class Label.
     *
     * @param text string representing the text to be displayed.
     */
    public Label(String text) {
        this();
        setText(text);
    }
}
