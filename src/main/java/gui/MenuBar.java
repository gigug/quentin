package gui;

import javax.swing.*;

/**
 * MenuBar class.
 *
 * @author Gianluca Guglielmo
 */
class MenuBar extends JMenuBar {

    /**
     * Default constructor for MenuBar.
     */
    MenuBar(GameGUI gameGUI){
        super();

        final JMenu fileMenu = new JMenu("File");

        final JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(e -> gameGUI.CARDS.show(gameGUI.getContentPane(), "selectSizePanel"));

        final JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
        loadGameMenuItem.addActionListener(e -> {
            gameGUI.loadGame();
            gameGUI.createGameCard();
        });

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newGameMenuItem);
        fileMenu.add(loadGameMenuItem);
        fileMenu.add(exitMenuItem);

        add(fileMenu);
    }
}