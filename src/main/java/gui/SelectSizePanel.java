package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel that displays size selection menu.
 *
 * @author Gianluca Guglielmo
 */
class SelectSizePanel extends JPanel {

    private final static Color BACKGROUND_COLOR_SELECT_SIZE = Color.decode("#8963BA");

    /**
     * Default constructor for SelectSizePanel.
     */
    public SelectSizePanel(GameGUI gameGUI){
        super();

        setLayout(GameGUI.GRID_BAG_LAYOUT);
        setBackground(BACKGROUND_COLOR_SELECT_SIZE);

        Constraints constraints = new Constraints(true);

        TitleLabel label = new TitleLabel("Select board size:");
        add(label, constraints);

        JPanel buttons = new JPanel(GameGUI.GRID_BAG_LAYOUT);
        buttons.setBackground(BACKGROUND_COLOR_SELECT_SIZE);
        add(buttons, constraints);

        StandardButton tiles7Button = new StandardButton("7x7");
        tiles7Button.addActionListener(new NumberTilesActionListener(7, gameGUI));
        buttons.add(tiles7Button, constraints);

        StandardButton tiles9Button = new StandardButton("9x9");
        tiles9Button.addActionListener(new NumberTilesActionListener(9, gameGUI));
        buttons.add(tiles9Button, constraints);

        StandardButton tiles11Button = new StandardButton("11x11");
        tiles11Button.addActionListener(new NumberTilesActionListener(11, gameGUI));
        buttons.add(tiles11Button, constraints);
    }

    /**
     * Static class used when clicking on the number tiles selection buttons to initialize game correctly.
     */
    static class NumberTilesActionListener implements ActionListener {
        private final int numberTiles;
        private final GameGUI gameGUI;

        /**
         * Constructor for the NumberTilesActionListener.
         *
         * @param numberTiles int representing the number of tiles.
         * @param gameGUI current GameGUI.
         */
        public NumberTilesActionListener(int numberTiles, GameGUI gameGUI) {
            this.numberTiles = numberTiles;
            this.gameGUI = gameGUI;
        }

        /**
         * Method to initialize game correctly when size has been chosen.
         *
         * @param e the event to be processed.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            gameGUI.setSize(numberTiles + 1);
            gameGUI.setNumberTiles(numberTiles);
            gameGUI.initializeGame(numberTiles + 1);
        }
    }
}
