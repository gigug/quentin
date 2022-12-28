package gui;

import objects.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

import static gui.FunctionsGUI.centerWindow;

public class StartGUI extends JFrame{

    public CardLayout cards;
    Container c;

    // board settings
    private final Color backgroundColor = Color.decode("#35012C");
    private final Color boardColor = Color.decode("#815E5B");
    private final Color lineColor = Color.decode("#000000");
    private final Color externalHorizontalLineColor = Color.decode("#000000");
    private final Color externalVerticalLineColor = Color.decode("#FFFFFF");

    // panel width
    private final static int PANEL_WIDTH = 40;
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(PANEL_WIDTH, PANEL_WIDTH);


    public StartGUI(){
        c = getContentPane();
        StartMenuPanel startMenuPanel = new StartMenuPanel();
        BoardPanel boardPanel = new BoardPanel();
        MenuBar menuBar = new MenuBar();

        cards = new CardLayout();
        c.setLayout(cards);
        c.add("start", startMenuPanel);
        c.add("game", boardPanel);

        // title and size
        setTitle("Quentin");
        setSize(400, 400);
        setMinimumSize(new Dimension(300, 300));

        // Add table menu bar to screen
        setJMenuBar(menuBar);

        // center window
        centerWindow(this);

        // settings window
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private class StartMenuPanel extends JPanel{

        public StartMenuPanel(){
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setLayout(new GridBagLayout());

            // start new game
            JButton newGameButton = new JButton("New game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.next(c);
                }
            });

            // exit game
            JButton exitGameButton = new JButton("Exit game");
            exitGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            // Add button to JPanel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            add(new JLabel("Quentin"), gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            buttons.add(newGameButton, gbc);
            buttons.add(exitGameButton, gbc);

            add(buttons, gbc);

        }
    }

    private class BoardPanel extends JPanel{
        int size = 10;
        // BoardPanel constructor
        BoardPanel(){
            super(new GridLayout(10, 10));

            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(backgroundColor);

            
            final List<StartGUI.TilePanel> boardTiles = new ArrayList<>();

            for(int tileIdY = 0; tileIdY < size; tileIdY++){
                for(int tileIdX = 0; tileIdX < size; tileIdX++){
                    final StartGUI.TilePanel tilePanel = new StartGUI.TilePanel(tileIdX, tileIdY);
                    boardTiles.add(tilePanel);
                    add(tilePanel);
                }
            }

            validate();

        }
    }


    private class TilePanel extends JPanel {
        private final int tileIdX;
        private final int tileIdY;

        int size = 10;
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
            g.setColor(boardColor);
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
            } else if (this.tileIdX == (size - 1) && this.tileIdY == 0) { // upper-right
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
            } else if (this.tileIdX == 0 && this.tileIdY == (size - 1)) { // lower-left
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
            } else if (this.tileIdX == (size - 1) && this.tileIdY == (size - 1)) { // lower-right
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
            } else if (this.tileIdX > 0 && this.tileIdX < (size - 1) && this.tileIdY == 0){ // upper flank
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
            } else if (this.tileIdX == 0 && this.tileIdY > 0 && this.tileIdY < (size - 1)){ // left flank
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
            } else if (this.tileIdX == (size - 1) && this.tileIdY > 0 && this.tileIdY < (size - 1)){ // right flank
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
            } else if (this.tileIdX > 0 && this.tileIdX < (size - 1) && this.tileIdY == (size - 1)){ // lower flank
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
 */

    }


}

