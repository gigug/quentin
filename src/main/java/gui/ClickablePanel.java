package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Panel that draws board and stones.
 *
 * @author Gianluca Guglielmo
 */
class ClickablePanel extends JPanel {

    final Dimension internalBoardDimension;
    final int internalBoardWidth;

    GameGUI gameGUI;

    /**
     * Default constructor for ClickablePanel.
     */
    ClickablePanel(GameGUI gameGUI){
        super();

        this.gameGUI = gameGUI;

        setOpaque(false);

        // Set preferred size for button placement grid
        internalBoardWidth = GameGUI.PANEL_WIDTH * (gameGUI.game.getSize());
        internalBoardDimension = new Dimension(internalBoardWidth, internalBoardWidth);
        setPreferredSize(internalBoardDimension);

        if (!gameGUI.game.isGameFinished()){
            setLayout(new GridLayout(gameGUI.game.getSize(), gameGUI.game.getSize()));
            // Create an array to hold the chessboard squares
            JButton[][] squares = new JButton[gameGUI.game.getSize()][gameGUI.game.getSize()];

            // Create a button for each square and add it to the panel
            for (int row = 0; row < gameGUI.game.getSize(); row++) {
                for (int col = 0; col < gameGUI.game.getSize(); col++) {
                    JButton button = new JButton();
                    squares[row][col] = button;
                    add(button);

                    // Set the button to be transparent
                    button.setOpaque(false);
                    button.setContentAreaFilled(false);
                    button.setBorderPainted(false);

                    // Add an ActionListener to the button to handle clicks
                    final int finalRow = row;
                    final int finalCol = col;
                    button.addActionListener(e -> {
                        // If move is doable, add stone and progress
                        if (gameGUI.game.checkPlaceble(finalCol, finalRow)){
                            gameGUI.game.progress(finalCol, finalRow);
                            gameGUI.boardPanel.setTurnLabel();
                        }
                        // If special case, recreate board to allow for new buttons
                        if (gameGUI.game.getTurn() == 1 || gameGUI.game.getTurn() == 2 || gameGUI.game.checkPassable() || gameGUI.game.isGameFinished()) gameGUI.createGameCard();
                    });

                    // Set the hoveredRow and hoveredCol variables when the mouse enters the button
                    // Reset when the mouse leaves the button
                    button.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            gameGUI.hoveredRow = finalRow;
                            gameGUI.hoveredCol = finalCol;
                            repaint();
                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            gameGUI.hoveredRow = -1;
                            gameGUI.hoveredCol = -1;
                            repaint();
                        }
                    });
                }

            }
        }

    }

    /*
     * Method to paint hovered cells based on whether a stone can be placed there from the current player
     *
     * @param g Graphics component
     */
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);

        // Color hovered cells green if a stone can be placed there, otherwise red
        if (gameGUI.hoveredCol > -1) {
            if (gameGUI.game.checkPlaceble(gameGUI.hoveredCol, gameGUI.hoveredRow)) g2.setColor(Color.GREEN);
            else g2.setColor(Color.RED);
            g2.fillOval(GameGUI.PANEL_WIDTH * gameGUI.hoveredCol, GameGUI.PANEL_WIDTH * gameGUI.hoveredRow, GameGUI.PANEL_WIDTH, GameGUI.PANEL_WIDTH);
        }
    }
}

