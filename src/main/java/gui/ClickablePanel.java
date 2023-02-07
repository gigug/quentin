package gui;

import game.FrozenGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Panel that draws board and stones.
 *
 * @author Gianluca Guglielmo
 */
class ClickablePanel extends JPanel {
    GameGUI gameGUI;
    FrozenGame frozenGame;

    /**
     * Default constructor for ClickablePanel.
     */
    ClickablePanel(GameGUI gameGUI){
        super();
        setOpaque(false);

        this.gameGUI = gameGUI;
        this.frozenGame = gameGUI.getFrozenGame();
        int size = gameGUI.getGridSize();

        // Set preferred size for button placement grid
        int clickableWidth = GameGUI.PANEL_WIDTH * (size);
        Dimension clickableDimension = new Dimension(clickableWidth, clickableWidth);
        setPreferredSize(clickableDimension);

        if (!frozenGame.isGameFinished()){
            setLayout(new GridLayout(size, size));

            // Create a button for each square and add it to the panel
            for (int Y = 0; Y < size; Y++) {
                for (int X = 0; X < size; X++) {
                    JButton button = new JButton();
                    add(button);

                    button.setOpaque(false);
                    button.setContentAreaFilled(false);
                    button.setBorderPainted(false);
                    button.addMouseListener(new GridMouseListener(X, Y, gameGUI, this));
                }

            }
        }
    }

    /**
     * Method to paint hovered cells based on whether a stone can be placed there from the current player
     *
     * @param g Graphics component
     */
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);

        // Color hovered cells green if a stone can be placed there, otherwise red
        if (gameGUI.hX > -1) {
            if (frozenGame.checkPlaceable(gameGUI.hX, gameGUI.hY)) g2.setColor(Color.GREEN);
            else g2.setColor(Color.RED);
            g2.fillOval(GameGUI.PANEL_WIDTH * gameGUI.hX, GameGUI.PANEL_WIDTH * gameGUI.hY, GameGUI.PANEL_WIDTH, GameGUI.PANEL_WIDTH);
        }
    }

    /**
     * Static class that manages clicks on the grid.
     */
    static class GridMouseListener implements MouseListener {
        private final int X;
        private final int Y;
        private final ClickablePanel clickablePanel;
        private final GameGUI gameGUI;
        private final FrozenGame frozenGame;

        /**
         * Constructor for gridMouseListener.
         *
         * @param X int representing the X coordinate of the button.
         * @param Y int representing the Y coordinate of the button.
         * @param gameGUI current gameGUI.
         * @param clickablePanel current clickablePanel.
         */
        GridMouseListener(int X, int Y, GameGUI gameGUI, ClickablePanel clickablePanel){
            this.X = X;
            this.Y = Y;
            this.gameGUI = gameGUI;
            this.frozenGame = gameGUI.getFrozenGame();
            this.clickablePanel = clickablePanel;
        }
        /**
         * Method that checks for clicks on the current button and carries the needed action if the stone can be placed.
         *
         * @param e the event to be processed.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (frozenGame.checkPlaceable(X, Y)) gameGUI.progress(X, Y);
            // If special case, enable/disable buttons
            if (frozenGame.getTurn() == 1 || frozenGame.getTurn() == 2 || frozenGame.checkPassable()) gameGUI.boardPanel.checkEnableButtons();
            if (frozenGame.isGameFinished()) gameGUI.createGameCard();
            repaint(-1, -1);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        /**
         * Method that assigns hX and hY to the button coordinates.
         *
         * @param e the event to be processed.
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            repaint(X, Y);
        }

        /**
         * Method that assigns hX and hY to -1, to indicate a possible exit of the mouse from the grid.
         *
         * @param evt the event to be processed.
         */
        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            repaint(-1, -1);
        }

        /**
         * Method to repaint the grid when an update happens.
         * It sets hX and hY accordingly.
         *
         * @param X int representing the X coordinate.
         * @param Y int representing the Y coordinate.
         */
        private void repaint(int X, int Y){
            gameGUI.hX = X;
            gameGUI.hY = Y;
            clickablePanel.repaint();
        }
    }
}

