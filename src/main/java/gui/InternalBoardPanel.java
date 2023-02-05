package gui;

import objects.FrozenBoard;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that draws the grid.
 *
 * @author Gianluca Guglielmo
 */
class InternalBoardPanel extends JPanel {
    private final int boardWidth;
    GameGUI gameGUI;

    /**
     * Default constructor for InternalBoardPanel.
     */
    InternalBoardPanel(GameGUI gameGUI){
        super();

        this.gameGUI = gameGUI;

        setLayout(GameGUI.GRID_BAG_LAYOUT);

        boardWidth = GameGUI.PANEL_WIDTH * (gameGUI.numberTiles + 4);
        Dimension boardDimension = new Dimension(boardWidth, boardWidth);

        setPreferredSize(boardDimension);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ClickablePanel clickablePanel = new ClickablePanel(gameGUI);

        add(clickablePanel, gbc);
    }

    /**
     * Method to draw the board.
     */
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);

        g.setColor(GameGUI.BOARD_COLOR);
        g.fillRect(0,0, boardWidth, boardWidth);

        float thicknessBorders = 1;
        float thicknessExternal = 4;
        float thicknessInternal = 2;

        int externalReferencePoint = GameGUI.PANEL_WIDTH/2;
        int internalReferencePoint = 2*GameGUI.PANEL_WIDTH;

        g2.setStroke(new BasicStroke(thicknessBorders));
        // White lines and triangles
        g2.setColor(Color.WHITE);
        g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
        g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, 3);
        g2.fillPolygon(new int[] {boardWidth, boardWidth, boardWidth - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
        g2.fillPolygon(new int[] {boardWidth, boardWidth, boardWidth - externalReferencePoint}, new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, 3);
        g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint, 0}, new int[] {externalReferencePoint, externalReferencePoint, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, 4);
        g2.fillPolygon(new int[] {boardWidth - externalReferencePoint, boardWidth, boardWidth, boardWidth - externalReferencePoint}, new int[] {externalReferencePoint, externalReferencePoint, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, 4);

        // Black lines and triangles
        g2.setColor(Color.BLACK);
        g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
        g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {boardWidth, boardWidth, boardWidth - externalReferencePoint}, 3);
        g2.fillPolygon(new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
        g2.fillPolygon(new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, new int[] {boardWidth, boardWidth, boardWidth - externalReferencePoint}, 3);
        g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint, 0}, 4);
        g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint, boardWidth}, 4);

        // External thick square
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(thicknessExternal));
        g2.drawPolygon(new int[] {internalReferencePoint, internalReferencePoint, boardWidth - internalReferencePoint, boardWidth - internalReferencePoint}, new int[] {internalReferencePoint, boardWidth - internalReferencePoint, boardWidth - internalReferencePoint, internalReferencePoint}, 4);

        // Internal lines
        g2.setStroke(new BasicStroke(thicknessInternal));

        // Vertical lines
        for (int i = 3; i < gameGUI.numberTiles + 2; i++) g2.drawLine(internalReferencePoint, GameGUI.PANEL_WIDTH * i, boardWidth - internalReferencePoint, GameGUI.PANEL_WIDTH * i);


        // Horizontal lines
        for (int i = 3; i < gameGUI.numberTiles + 2; i++) g2.drawLine(GameGUI.PANEL_WIDTH * i, internalReferencePoint, GameGUI.PANEL_WIDTH * i, boardWidth - internalReferencePoint);


        Color colorFill;
        Color colorSurface = Color.GREEN;

        // stones
        FrozenBoard board = gameGUI.game.getFrozenBoard();
        for(int i = 0; i < gameGUI.game.getSize(); i++) {
            for (int j = 0; j < gameGUI.game.getSize(); j++) {
                if (board.getStone(i,j) != 0){
                    colorFill = (board.getStone(i, j) == 1 || board.getStone(i, j) == 3) ? Color.BLACK : Color.WHITE;
                    g2.setColor(colorFill);
                    g2.fillOval(internalReferencePoint + GameGUI.PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + GameGUI.PANEL_WIDTH * j - externalReferencePoint, GameGUI.PANEL_WIDTH, GameGUI.PANEL_WIDTH);

                    if (board.getStone(i, j) > 2){
                        g2.setColor(colorSurface);
                        g2.drawOval(internalReferencePoint + GameGUI.PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + GameGUI.PANEL_WIDTH * j - externalReferencePoint, GameGUI.PANEL_WIDTH, GameGUI.PANEL_WIDTH);
                    }
                }
            }
        }
    }
}
