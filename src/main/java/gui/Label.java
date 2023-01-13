package gui;

import javax.swing.*;

public class Label extends JLabel {
    public Label(){
        super();
        setFont(StartGUI.FONT_BIG);
        setForeground(StartGUI.FONT_COLOR);
    }
    public Label(String text) {
        this();
        setText(text);
    }
}
