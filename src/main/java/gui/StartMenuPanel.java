package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that displays start menu.
 *
 * @author Gianluca Guglielmo
 */
class StartMenuPanel extends JPanel {
    private final static Color BACKGROUND_COLOR_START_MENU = Color.decode("#F46036");

    /**
     * Constructor for the StartMenuPanel class.
     */
    public StartMenuPanel(GameGUI gameGUI){
        super();

        setLayout(GameGUI.GRID_BAG_LAYOUT);
        setBackground(BACKGROUND_COLOR_START_MENU);

        Constraints constraintsPanel = new Constraints(false);
        Constraints constraintsButtons = new Constraints(true);

        TitleLabel label = new TitleLabel("Quentin");
        add(label, constraintsPanel);

        JPanel buttons = new JPanel(GameGUI.GRID_BAG_LAYOUT);
        buttons.setBackground(BACKGROUND_COLOR_START_MENU);
        add(buttons, constraintsPanel);

        StandardButton newGameButton = new StandardButton("New game");
        newGameButton.addActionListener(gameGUI.newGameActionListener);
        buttons.add(newGameButton, constraintsButtons);

        StandardButton loadGameButton = new StandardButton("Load game");
        loadGameButton.addActionListener(gameGUI.loadGameActionListener);
        buttons.add(loadGameButton, constraintsButtons);

        StandardButton exitGameButton = new StandardButton("Exit game");
        exitGameButton.addActionListener(gameGUI.exitGameActionListener);
        buttons.add(exitGameButton, constraintsButtons);
    }
}
