package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import java.awt.Graphics;

import objects.Board;
public class BoardGUI {

    private Board board;
    private final int size;

    private final JFrame gameFrame;
    //private final ExternalPanel externalPanel;

    private final Color backgroundColor = Color.decode("#815E5B");
    private final Color lineColor = Color.decode("#000000");
    private final Color externalHorizontalLineColor = Color.decode("#000000");
    private final Color externalVerticalLineColor = Color.decode("#FFFFFF");

    // panel width
    private final static int PANEL_WIDTH = 40;
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(PANEL_WIDTH, PANEL_WIDTH);

    public BoardGUI(Board board) {

        this.size = board.getSize();

        this.gameFrame = new JFrame("Quentin board");

        // xy
        this.gameFrame.setLayout(new GridBagLayout());

        // Add table menu bar to screen
        final JMenuBar tableMenuBar = createTableMenuBar();

        // Initialize externalPanel
        //this.externalPanel = new ExternalPanel(board);

        // Add externalPanel to JFrame
        //this.gameFrame.add(externalPanel);

        // Add JMenuBar
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(new Dimension(PANEL_WIDTH * (size + 2), PANEL_WIDTH * (size + 3)));
        this.gameFrame.setResizable(false);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPlayerSelectionWindow();
            }
        });
        fileMenu.add(newGameMenuItem);

        final JMenuItem openPGNMenuItem = new JMenuItem("Load PGN File");
        openPGNMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Change THIS!");
            }
        });
        fileMenu.add(openPGNMenuItem);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private void openPlayerSelectionWindow(){
        playerSelectionWindow newPlayerSelectionWindow = new playerSelectionWindow();
        this.gameFrame.add(newPlayerSelectionWindow);
    }

    private class playerSelectionWindow extends JWindow{
        playerSelectionWindow(){
            super();

            JButton blackPlayerButton = new JButton("Black");
            JButton whitePlayerButton = new JButton("White");

            setBackground(Color.YELLOW);

            add(blackPlayerButton);
            add(whitePlayerButton);

            validate();
        }
    }

    private class ExternalPanel extends JPanel{

        ExternalPanel(Board board){
            super(new GridBagLayout());
            BoardPanel boardPanel = new BoardPanel(board);

            // add boardPanel to externalPanel
            add(boardPanel);
            setBackground(Color.GRAY);

            validate();
        }
    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;

        // BoardPanel constructor
        BoardPanel(Board board){
            super(new GridLayout(BoardGUI.this.size, BoardGUI.this.size));
            this.boardTiles = new ArrayList<>();

            for(int tileIdY = 0; tileIdY < BoardGUI.this.size; tileIdY++){
                for(int tileIdX = 0; tileIdX < BoardGUI.this.size; tileIdX++){
                    final TilePanel tilePanel = new TilePanel(tileIdX, tileIdY);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
            }

            validate();
        }

    }

    private class TilePanel extends JPanel {
        private final int tileIdX;
        private final int tileIdY;

        TilePanel(final int tileIdX, final int tileIdY){
            super(new GridBagLayout());
            this.tileIdX = tileIdX;
            this.tileIdY = tileIdY;

            setPreferredSize(TILE_PANEL_DIMENSION);
            validate();
        }
        @Override
        protected void paintComponent(Graphics g) {
            // down-casting graphic component to set stroke
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);

            //xy pass everything to g2?
            // draw the rectangle here
            g.setColor(backgroundColor);
            g.fillRect(0, 0, 100, 100);

            // defining line ranges
            int hX1, hY1, hX2, hY2;
            int vX1, vY1, vX2, vY2;

            // defining line thickness
            float thickness1;
            float thickness2;
            float thicknessExternal = 20;

            // xy reorganize this as nested ifs to reduce boilerplate
            if (this.tileIdX == 0 && this.tileIdY == 0){ // upper-left
                thickness1 = 4;
                thickness2 = 4;
                vX1 = 20;
                vY1 = 20;
                vX2 = 20;
                vY2 = 40;

                hX1 = 20;
                hY1 = 20;
                hX2 = 40;
                hY2 = 20;

                g2.setColor(externalHorizontalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 0, 40, 0);

                g2.setColor(externalVerticalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 0, 0, 40);
            } else if (this.tileIdX == (BoardGUI.this.size - 1) && this.tileIdY == 0) { // upper-right
                thickness1 = 4;
                thickness2 = 4;
                vX1 = 20;
                vY1 = 20;
                vX2 = 20;
                vY2 = 40;

                hX1 = 0;
                hY1 = 20;
                hX2 = 20;
                hY2 = 20;

                g2.setColor(externalHorizontalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 0, 40, 0);

                g2.setColor(externalVerticalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(40, 0, 40, 40);
            } else if (this.tileIdX == 0 && this.tileIdY == (BoardGUI.this.size - 1)) { // lower-left
                thickness1 = 4;
                thickness2 = 4;
                vX1 = 20;
                vY1 = 20;
                vX2 = 40;
                vY2 = 20;

                hX1 = 20;
                hY1 = 0;
                hX2 = 20;
                hY2 = 20;

                g2.setColor(externalHorizontalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 40, 40, 40);

                g2.setColor(externalVerticalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 0, 0, 40);
            } else if (this.tileIdX == (BoardGUI.this.size - 1) && this.tileIdY == (BoardGUI.this.size - 1)) { // lower-right
                thickness1 = 4;
                thickness2 = 4;
                vX1 = 20;
                vY1 = 0;
                vX2 = 20;
                vY2 = 20;

                hX1 = 20;
                hY1 = 20;
                hX2 = 0;
                hY2 = 20;

                g2.setColor(externalHorizontalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 40, 40, 40);

                g2.setColor(externalVerticalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(40, 0, 40, 40);
            } else if (this.tileIdX > 0 && this.tileIdX < (BoardGUI.this.size - 1) && this.tileIdY == 0){ // upper flank
                thickness1 = 4;
                thickness2 = 2;
                vX1 = 20;
                vY1 = 20;
                vX2 = 20;
                vY2 = 40;

                hX1 = 0;
                hY1 = 20;
                hX2 = 40;
                hY2 = 20;

                g2.setColor(externalHorizontalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 0, 40, 0);
            } else if (this.tileIdX == 0 && this.tileIdY > 0 && this.tileIdY < (BoardGUI.this.size - 1)){ // left flank
                thickness1 = 2;
                thickness2 = 4;
                vX1 = 20;
                vY1 = 0;
                vX2 = 20;
                vY2 = 40;

                hX1 = 20;
                hY1 = 20;
                hX2 = 40;
                hY2 = 20;

                g2.setColor(externalVerticalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 0, 0, 40);
            } else if (this.tileIdX == (BoardGUI.this.size - 1) && this.tileIdY > 0 && this.tileIdY < (BoardGUI.this.size - 1)){ // right flank
                thickness1 = 2;
                thickness2 = 4;
                vX1 = 20;
                vY1 = 0;
                vX2 = 20;
                vY2 = 40;

                hX1 = 0;
                hY1 = 20;
                hX2 = 20;
                hY2 = 20;

                g2.setColor(externalVerticalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(40, 0, 40, 40);
            } else if (this.tileIdX > 0 && this.tileIdX < (BoardGUI.this.size - 1) && this.tileIdY == (BoardGUI.this.size - 1)){ // lower flank
                thickness1 = 4;
                thickness2 = 2;
                vX1 = 20;
                vY1 = 0;
                vX2 = 20;
                vY2 = 20;

                hX1 = 0;
                hY1 = 20;
                hX2 = 40;
                hY2 = 20;

                g2.setColor(externalHorizontalLineColor);
                g2.setStroke(new BasicStroke(thicknessExternal));
                g2.drawLine(0, 40, 40, 40);
            } else {
                thickness1 = 2;
                thickness2 = 2;

                vX1 = 20;
                vY1 = 0;
                vX2 = 20;
                vY2 = 40;

                hX1 = 0;
                hY1 = 20;
                hX2 = 40;
                hY2 = 20;

            }

            g2.setColor(lineColor);

            g2.setStroke(new BasicStroke(thickness1));
            g2.drawLine(hX1, hY1, hX2, hY2);
            g2.setStroke(new BasicStroke(thickness2));
            g2.drawLine(vX1, vY1, vX2, vY2);
        }
/*
        private void assignTilePieceIcon(final Board board) {
            //this.removeAll();
            if(this.board.getPiece(this.tileId) != null) {
                try{
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath +
                            board.getPiece(this.tileId).getPieceAllegiance().toString().substring(0, 1) + "" +
                            board.getPiece(this.tileId).toString() +
                            ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch(final IOException e) {
                    e.printStackTrace();
                }
            }
        }

 */

    }

}