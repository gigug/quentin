package gui;

import objects.Board;
import screens.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

import static gui.FunctionsGUI.centerWindow;

public class StartGUI extends JFrame{

    public Game game;
    public CardLayout cards;
    Container contentPane;

    // board settings
    private final Color fontColor = Color.decode("#E1F2FE");
    private final Color backgroundColor = Color.decode("#35012C");
    private final Color boardColor = Color.decode("#815E5B");

    public int size;
    public int numberTiles;

    int hoveredRow = -1;
    int hoveredCol = -1;

    private final static Dimension WINDOW_DIMENSION= new Dimension(700, 700);
    // panel width
    private final static int PANEL_WIDTH = 34;
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(PANEL_WIDTH, PANEL_WIDTH);

    public StartGUI(){

        contentPane = getContentPane();
        StartMenuPanel startMenuPanel = new StartMenuPanel();
        SelectSizePanel selectSizePanel = new SelectSizePanel();
        MenuBar menuBar = new MenuBar();

        cards = new CardLayout();
        contentPane.setLayout(cards);
        contentPane.add("startMenuPanel", startMenuPanel);
        contentPane.add("selectSizePanel", selectSizePanel);

        // title and size
        setTitle("Quentin");
        setSize(WINDOW_DIMENSION);

        // Add table menu bar to screen
        setJMenuBar(menuBar);

        // center window
        centerWindow(this);

        // settings window
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private class StartMenuPanel extends JPanel{
        public StartMenuPanel(){

            setBorder(new EmptyBorder(10, 10, 10, 10));
            GridBagLayout gridBagLayout = new GridBagLayout();
            setLayout(gridBagLayout);

            setBackground(backgroundColor);

            // start new game
            JButton newGameButton = new JButton("New game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.show(contentPane, "selectSizePanel");
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
            gbc.insets = new Insets(5, 0, 5,0);

            JLabel label = new JLabel("Quentin");
            label.setFont(new Font("Serif", Font.BOLD, 20));
            label.setForeground(fontColor);

            add(label, gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            // using same constraints for buttons
            buttons.add(newGameButton, gbc);
            buttons.add(exitGameButton, gbc);
            buttons.setBackground(backgroundColor);
            add(buttons, gbc);
        }
    }

    private class SelectSizePanel extends JPanel{
        public SelectSizePanel(){

            setBorder(new EmptyBorder(10, 10, 10, 10));
            GridBagLayout gridBagLayout = new GridBagLayout();
            setLayout(gridBagLayout);

            setBackground(backgroundColor);

            JButton size7Button = new JButton("7x7");
            size7Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numberTiles = 7;
                    size = numberTiles + 1;
                    initializeGame(size);
                }
            });

            JButton size9Button = new JButton("9x9");
            size9Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numberTiles = 9;
                    size = numberTiles + 1;
                    initializeGame(size);
                }
            });

            JButton size11Button = new JButton("11x11");
            size11Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numberTiles = 11;
                    size = numberTiles + 1;
                    initializeGame(size);
                }
            });

            // Add button to JPanel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(5, 0, 5,0);

            JLabel label = new JLabel("Select board size:");
            label.setFont(new Font("Serif", Font.BOLD, 20));
            label.setForeground(fontColor);

            add(label, gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            // using same constraints for buttons
            buttons.add(size7Button, gbc);
            buttons.add(size9Button, gbc);
            buttons.add(size11Button, gbc);
            buttons.setBackground(backgroundColor);
            add(buttons, gbc);
        }
    }

    private class BoardPanel extends JPanel{
        // BoardPanel constructor
        BoardPanel(boolean pieRule, boolean passable){
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(backgroundColor);

            InternalBoardPanel internalBoardPanel = new InternalBoardPanel();

            // center panel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(5, 0, 5, 0);

            // go back
            JButton newGameButton = new JButton("New Game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentPane.remove(2);
                    cards.show(contentPane, "selectSizePanel");
                }
            });

            JLabel label = new JLabel("Game");
            label.setFont(new Font("Serif", Font.BOLD, 20));
            label.setForeground(fontColor);

            add(label, gbc);
            add(internalBoardPanel, gbc);
            add(newGameButton, gbc);

            if (pieRule){
                JButton pieRuleButton = new JButton("Switch sides");
                pieRuleButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.changePiecePieRule();
                        createGameCard(false, false);
                    }
                });
                add(pieRuleButton, gbc);
            }

            if (passable){
                JButton passableButton = new JButton("Pass");
                passableButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.switchPlayer();
                        createGameCard(false, false);
                    }
                });
                add(passableButton, gbc);
            }
        }
    }

    private class InternalBoardPanel extends JPanel{
        int board_width;
        final Dimension boardDimension;
        InternalBoardPanel(){
            setLayout(new GridBagLayout());

            boardDimension = new Dimension(PANEL_WIDTH * (numberTiles + 4), PANEL_WIDTH * (numberTiles + 4));
            board_width = PANEL_WIDTH * (numberTiles + 4);

            setPreferredSize(boardDimension);

            // center panel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            ClickablePanel clickablePanel = new ClickablePanel();

            add(clickablePanel, gbc);

        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);

            g.setColor(boardColor);
            g.fillRect(0,0, board_width, board_width);

            float thicknessBorders = 1;
            float thicknessExternal = 4;
            float thicknessInternal = 2;

            int externalReferencePoint = PANEL_WIDTH/2;
            int internalReferencePoint = 2*PANEL_WIDTH;

            g2.setStroke(new BasicStroke(thicknessBorders));
            // white lines and triangles
            g2.setColor(Color.WHITE);
            g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width, board_width - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width, board_width - externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint, 0}, new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, 4);
            g2.fillPolygon(new int[] {board_width - externalReferencePoint, board_width, board_width, board_width - externalReferencePoint}, new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, 4);

            // black lines and triangles
            g2.setColor(Color.BLACK);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {board_width, board_width, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {board_width, board_width, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint, 0}, 4);
            g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint, board_width}, 4);

            // external thick square
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(thicknessExternal));
            g2.drawPolygon(new int[] {internalReferencePoint, internalReferencePoint, board_width - internalReferencePoint, board_width - internalReferencePoint}, new int[] {internalReferencePoint, board_width - internalReferencePoint, board_width - internalReferencePoint, internalReferencePoint}, 4);

            // internal lines
            g2.setStroke(new BasicStroke(thicknessInternal));

            // vertical lines
            for (int i = 3; i < numberTiles + 2; i++) {
                g2.drawLine(internalReferencePoint, PANEL_WIDTH * i, board_width - internalReferencePoint, PANEL_WIDTH * i);
            }

            // horizontal lines
            for (int i = 3; i < numberTiles + 2; i++) {
                g2.drawLine(PANEL_WIDTH * i, internalReferencePoint, PANEL_WIDTH * i, board_width - internalReferencePoint);
            }


            // pawns
            for(int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (game.board.grid[i][j] == 1) {
                        g2.setColor(Color.BLACK);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        }
                    if (game.board.grid[i][j] == 2) {
                        g2.setColor(Color.WHITE);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        }
                }
            }
        }
    }

    private class ClickablePanel extends JPanel{
        final Dimension internalBoardDimension;
        ClickablePanel(){
            setLayout(new GridLayout(size, size));
            setOpaque(false);

            internalBoardDimension = new Dimension(PANEL_WIDTH * (size), PANEL_WIDTH * (size));

            setPreferredSize(internalBoardDimension);

            // Create an array to hold the chessboard squares
            JButton[][] squares = new JButton[size][size];

            // Create a button for each square and add it to the panel
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    JButton button = new JButton();
                    squares[row][col] = button;
                    add(button);

                    // Set the button to be transparent
                    button.setOpaque(false);
                    button.setContentAreaFilled(false);
                    button.setBorderPainted(false);

                    // Add an ActionListener to the button to handle clicks
                    int finalRow = row;
                    int finalCol = col;
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            if (game.checkEmpty(finalCol, finalRow) && game.checkDiagonal(finalCol, finalRow)){
                                game.addPiece(finalCol, finalRow);
                                game.progress();
                            }
                            if (game.getTurn() == 2){
                                createGameCard(true, false);
                            }
                            if (game.getTurn() == 3){
                                createGameCard(false, false);
                            }
                            if (game.checkPassable()){
                                createGameCard(false, true);
                            }
                        }
                    });
                    button.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            // Set the hoveredRow and hoveredCol variables when the mouse enters the button
                            hoveredRow = finalRow;
                            hoveredCol = finalCol;
                            repaint();
                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            hoveredRow = -1;
                            hoveredCol = -1;
                            repaint();
                        }
                    });
                }

            }

        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);

            if (hoveredCol > -1) {
                if ((game.checkEmpty(hoveredCol, hoveredRow)) && (game.checkDiagonal(hoveredCol, hoveredRow))){
                    g2.setColor(Color.GREEN);
                }
                else {
                    g2.setColor(Color.RED);
                }
                g2.fillOval(PANEL_WIDTH * hoveredCol, PANEL_WIDTH * hoveredRow, PANEL_WIDTH, PANEL_WIDTH);
            }
        }
    }

    private class MenuBar extends JMenuBar {

        public MenuBar(){
            final JMenu fileMenu = new JMenu("File");

            final JMenuItem newGameMenuItem = new JMenuItem("New Game");
            newGameMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.show(contentPane, "selectSizePanel");
                }
            });
            fileMenu.add(newGameMenuItem);

            final JMenuItem exitMenuItem = new JMenuItem("Exit");
            exitMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            fileMenu.add(exitMenuItem);

            this.add(fileMenu);
        }
    }

    private void initializeGame(int size){
        game = new Game(size);
        createGameCard(false, false);
    }

    private void createGameCard(boolean pieRule, boolean passable){
        BoardPanel boardPanel = new BoardPanel(pieRule, passable);
        contentPane.add("boardPanel", boardPanel);
        cards.show(contentPane, "boardPanel");
    }

}

