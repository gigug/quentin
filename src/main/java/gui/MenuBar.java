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
        add(fileMenu);

        final JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(gameGUI.newGameActionListener);
        fileMenu.add(newGameMenuItem);

        final JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
        loadGameMenuItem.addActionListener(gameGUI.loadGameActionListener);
        fileMenu.add(loadGameMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(gameGUI.exitGameActionListener);
        fileMenu.add(exitMenuItem);
    }
}