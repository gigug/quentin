package gui;

import javax.swing.*;
import java.awt.*;

public class BlackButton extends JButton{
    public BlackButton(String text) {
        super(text);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
    }
}