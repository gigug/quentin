package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that displays start menu.
 *
 * @author Gianluca Guglielmo
 */
class StartMenuPanel extends JPanel {

    /**
     * Constructor for the StartMenuPanel class.
     */
    public StartMenuPanel(GameGUI gameGUI){
        super();

        // Set GridBagLayout for the buttons
        setLayout(GameGUI.GRID_BAG_LAYOUT);

        // Set background color
        setBackground(GameGUI.BACKGROUND_COLOR_1);

        // Start new game
        BlackButton newGameButton = new BlackButton("New game");
        newGameButton.addActionListener(e -> gameGUI.CARDS.show(gameGUI.contentPane, "selectSizePanel"));

        // Load game
        BlackButton loadGameButton = new BlackButton("Load game");
        loadGameButton.addActionListener(e -> {
            gameGUI.loadGame();
            gameGUI.createGameCard();
        });

        // Exit game
        BlackButton exitGameButton = new BlackButton("Exit game");
        exitGameButton.addActionListener(e -> System.exit(0));

        // Constraints for grid bag layout
        GridBagConstraints gbc = new GridBagConstraints();

        // Order elements vertically by setting one element per row
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Set small border between buttons
        gbc.insets = GameGUI.INSETS;

        // Add label to panel
        TitleLabel label = new TitleLabel("Quentin");
        add(label, gbc);

        // Make buttons the same width
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create new panel for buttons
        JPanel buttons = new JPanel(GameGUI.GRID_BAG_LAYOUT);

        // Using same constraints for buttons
        buttons.add(newGameButton, gbc);
        buttons.add(loadGameButton, gbc);
        buttons.add(exitGameButton, gbc);
        buttons.setBackground(GameGUI.BACKGROUND_COLOR_1);
        add(buttons, gbc);
    }
}
