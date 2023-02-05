package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that displays size selection menu.
 *
 * @author Gianluca Guglielmo
 */
class SelectSizePanel extends JPanel {

    /**
     * Default constructor for SelectSizePanel.
     */
    public SelectSizePanel(GameGUI gameGUI){
        super();

        // Set GridBagLayout for the buttons
        setLayout(GameGUI.GRID_BAG_LAYOUT);

        // Set background color
        setBackground(GameGUI.BACKGROUND_COLOR_2);

        BlackButton size7Button = new BlackButton("7x7");
        size7Button.addActionListener(e->gameGUI.selectSize(8));

        BlackButton size9Button = new BlackButton("9x9");
        size9Button.addActionListener(e->gameGUI.selectSize(10));

        BlackButton size11Button = new BlackButton("11x11");
        size11Button.addActionListener(e->gameGUI.selectSize(12));

        GridBagConstraints gbc = new GridBagConstraints();

        // Order elements vertically
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Set small border between buttons
        gbc.insets = GameGUI.INSETS;

        TitleLabel label = new TitleLabel("Select board size:");
        add(label, gbc);

        // Make buttons the same width
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttons = new JPanel(GameGUI.GRID_BAG_LAYOUT);
        // Using same constraints for buttons
        buttons.add(size7Button, gbc);
        buttons.add(size9Button, gbc);
        buttons.add(size11Button, gbc);
        buttons.setBackground(GameGUI.BACKGROUND_COLOR_2);
        add(buttons, gbc);
    }
}
