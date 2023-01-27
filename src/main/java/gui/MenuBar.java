package gui;

import javax.swing.*;

/**
 * MenuBar class.
 */
public class MenuBar extends JMenuBar {

    /**
     * Default constructor for MenuBar.
     */
    MenuBar(GameGUI gameGUI){
        super();

        final JMenu fileMenu = new JMenu("File");

        // New game item
        final JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(e -> gameGUI.CARDS.show(gameGUI.getContentPane(), "selectSizePanel"));

        // load game item
        final JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
        loadGameMenuItem.addActionListener(e -> {
            gameGUI.loadGame();
            gameGUI.createGameCard();
        });

        // Exit program item
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newGameMenuItem);
        fileMenu.add(loadGameMenuItem);
        fileMenu.add(exitMenuItem);

        add(fileMenu);
    }
}