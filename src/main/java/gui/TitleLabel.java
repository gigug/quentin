package gui;

import javax.swing.*;

/**
 * Class that implements the labels shown in the different panels of the GUI.
 *
 * @author Gianluca Guglielmo
 */
class TitleLabel extends JLabel {

    /**
     * Default constructor for the class Label.
     */
    TitleLabel(){
        super();
        setFont(GameGUI.FONT_BIG);
        setForeground(GameGUI.FONT_COLOR);
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
