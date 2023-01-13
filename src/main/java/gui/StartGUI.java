package gui;

import screens.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import static gui.FunctionsGUI.centerWindow;

/**
 * Class that implements the GUI for the game Quentin
 */
public class StartGUI extends JFrame{

    // constants
    final static Font FONT_BIG = new Font("monospaced", Font.TRUETYPE_FONT, 35);
    final static Font FONT_SMALL = new Font("monospaced", Font.TRUETYPE_FONT, 20);
    final static Color FONT_COLOR = Color.decode("#E1F2FE");
    private final static Color BACKGROUND_COLOR = Color.decode("#35012C");
    private final static Color BOARD_COLOR = Color.decode("#815E5B");
    private final static Dimension WINDOW_DIMENSION= new Dimension(700, 700);
    private final static int DIAMETER_ICON = 30;
    private final static int PANEL_WIDTH = 30;
    private final static Insets INSETS = new Insets(5, 0, 5, 0);
    // cardLayout that will be used to switch between different panels
    private final static CardLayout CARDS = new CardLayout();
    private final static GridBagLayout GRID_BAG_LAYOUT = new GridBagLayout();

    // define game
    private Game game;

    // container with CardLayout
    private final Container contentPane;

    // game-related variables
    private int size;
    private int numberTiles;

    // GUI-related variables
    private int hoveredRow = -1;
    private int hoveredCol = -1;

    // panels
    StartMenuPanel startMenuPanel;
    SelectSizePanel selectSizePanel;
    BoardPanel boardPanel;
    InternalBoardPanel internalBoardPanel;
    UnclickablePanel unclickablePanel;
    ClickablePanel clickablePanel;
    MenuBar menuBar;

    public StartGUI(){

        // initialize contentPane
        contentPane = getContentPane();

        // initialize start menu panel
        startMenuPanel = new StartMenuPanel();

        // initialize select size panel
        selectSizePanel = new SelectSizePanel();

        // initialize menuBar
        menuBar = new MenuBar();

        // initialize cards
        contentPane.setLayout(CARDS);
        contentPane.add("startMenuPanel", startMenuPanel);
        contentPane.add("selectSizePanel", selectSizePanel);

        // title and size
        setTitle("Quentin");
        setSize(WINDOW_DIMENSION);

        // Add table menu bar to screen
        setJMenuBar(menuBar);

        // center window
        centerWindow(this);

        // window settings
        setResizable(false);
        setVisible(true);

        // close and not just hide
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Panel that displays start menu
     */
    private class StartMenuPanel extends JPanel{
        public StartMenuPanel(){
            // set GridBagLayout for the buttons
            setLayout(GRID_BAG_LAYOUT);

            // set background color
            setBackground(BACKGROUND_COLOR);

            // start new game
            BlackButton newGameButton = new BlackButton("New game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CARDS.show(contentPane, "selectSizePanel");
                }
            });

            // load game
            BlackButton loadGameButton = new BlackButton("Load game");
            loadGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // load game
                    loadGame();

                    // create new game card
                    createGameCard();
                }
            });

            // exit game
            BlackButton exitGameButton = new BlackButton("Exit game");
            exitGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            // constraints for grid bag layout
            GridBagConstraints gbc = new GridBagConstraints();

            // order elements vertically by setting one element per row
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            // set small border between buttons
            gbc.insets = INSETS;

            // add label to panel
            Label label = new Label("Quentin");
            add(label, gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // create new panel for buttons
            JPanel buttons = new JPanel(GRID_BAG_LAYOUT);
            // using same constraints for buttons
            buttons.add(newGameButton, gbc);
            buttons.add(loadGameButton, gbc);
            buttons.add(exitGameButton, gbc);
            buttons.setBackground(BACKGROUND_COLOR);
            add(buttons, gbc);
        }
    }

    /**
     * Panel that displays size selection menu
     */
    private class SelectSizePanel extends JPanel{
        public SelectSizePanel(){
            // set GridBagLayout for the buttons
            setLayout(GRID_BAG_LAYOUT);

            // set background color
            setBackground(BACKGROUND_COLOR);

            BlackButton size7Button = new BlackButton("7x7");
            size7Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectSize(8);
                }
            });

            BlackButton size9Button = new BlackButton("9x9");
            size9Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectSize(10);
                }
            });

            BlackButton size11Button = new BlackButton("11x11");
            size11Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectSize(12);
                }
            });

            GridBagConstraints gbc = new GridBagConstraints();

            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            // set small border between buttons
            gbc.insets = INSETS;

            Label label = new Label("Select board size:");
            add(label, gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(GRID_BAG_LAYOUT);
            // using same constraints for buttons
            buttons.add(size7Button, gbc);
            buttons.add(size9Button, gbc);
            buttons.add(size11Button, gbc);
            buttons.setBackground(BACKGROUND_COLOR);
            add(buttons, gbc);
        }
    }

    /**
     * Panel that displays turn, grid and options
     */
    private class BoardPanel extends JPanel{
        JPanel turnPanel = new JPanel();
        JLabel circleLabel = new JLabel();
        Label turnLabel = new Label();

        /**
         * Board panel constructor
         */
        BoardPanel(){

            turnPanel.setOpaque(false);

            setLayout(GRID_BAG_LAYOUT);
            setBackground(BACKGROUND_COLOR);

            internalBoardPanel = new InternalBoardPanel();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = INSETS;

            // new game button
            BlackButton newGameButton = new BlackButton("New Game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CARDS.show(contentPane, "selectSizePanel");
                    contentPane.remove(boardPanel);
                }
            });

            // turn panel displays the current player and a small colored circle indicating its pawn
            turnPanel.add(turnLabel);
            turnPanel.add(circleLabel);
            setTurnLabel();

            add(turnPanel, gbc);
            add(internalBoardPanel, gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.add(newGameButton);

            // during the second turn the pie rule can be invoked by the white player
            if (game.getTurn() == 1){
                BlackButton pieRuleButton = new BlackButton("Switch sides");
                pieRuleButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.changePiecePieRule();
                        game.switchPlayer();
                        createGameCard();
                    }
                });
                buttonPanel.add(pieRuleButton);
            }

            // if the game is passable a pass button appears
            if (game.checkPassable() && !game.isFinished()){
                BlackButton passableButton = new BlackButton("Pass");
                passableButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.switchPlayer();
                        createGameCard();
                    }
                });
                buttonPanel.add(passableButton);
            }

            // save game button
            BlackButton saveGameMenuButton = new BlackButton("Save Game");
            saveGameMenuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveGame();
                }
            });
            buttonPanel.add(saveGameMenuButton);

            add(buttonPanel, gbc);
        }

        /**
         * Method to set the panel containing the label and the circle correctly.
         */
        private void setTurnLabel() {
            String text;
            Color circleColor;
            BufferedImage circleImage;
            ImageIcon circleIcon;

            // display final message if game is finished
            if (game.isFinished()){
                if (game.getWinner() == 0){
                    text = "Tie!";
                    circleColor = Color.BLUE;
                }
                else{
                    text = (game.getWinner() == 1) ? "Winner: black":"Winner: white";
                    circleColor = (game.getWinner() == 1) ? Color.BLACK:Color.WHITE;
                }

            }
            // display whose turn it is if game is not finished
            else{
                text = (game.getCurrentPlayer() == 1) ? "Turn: black":"Turn: white";
                circleColor = (game.getCurrentPlayer() == 1) ? Color.BLACK:Color.WHITE;
            }

            // create a new image that will hold the circle
            circleImage = new BufferedImage(DIAMETER_ICON, DIAMETER_ICON, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleImage.createGraphics();

            // fill the circle with the right color
            g2.setColor(circleColor);
            g2.fillOval(0, 0, DIAMETER_ICON, DIAMETER_ICON);
            g2.dispose();

            // Create an ImageIcon from the circle image
            circleIcon = new ImageIcon(circleImage);

            circleLabel.setIcon(circleIcon);
            turnLabel.setText(text);
        }
    }

    /**
     * Panel that draws the grid
     */
    private class InternalBoardPanel extends JPanel{
        int boardWidth;
        final Dimension boardDimension;
        InternalBoardPanel(){
            setLayout(GRID_BAG_LAYOUT);

            boardDimension = new Dimension(PANEL_WIDTH * (numberTiles + 4), PANEL_WIDTH * (numberTiles + 4));
            boardWidth = PANEL_WIDTH * (numberTiles + 4);

            setPreferredSize(boardDimension);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // if the game is not finished create clickable panel of buttons
            if (!game.isFinished()){
                clickablePanel = new ClickablePanel();
                add(clickablePanel, gbc);
            }
            // if the game is finished create unclickable panel of buttons
            else{
                unclickablePanel = new UnclickablePanel();
                add(unclickablePanel, gbc);
            }

        }

        /**
         * Method to draw the board.
         */
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);

            g.setColor(BOARD_COLOR);
            g.fillRect(0,0, boardWidth, boardWidth);

            float thicknessBorders = 1;
            float thicknessExternal = 4;
            float thicknessInternal = 2;

            int externalReferencePoint = PANEL_WIDTH/2;
            int internalReferencePoint = 2*PANEL_WIDTH;

            g2.setStroke(new BasicStroke(thicknessBorders));
            // white lines and triangles
            g2.setColor(Color.WHITE);
            g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {boardWidth, boardWidth, boardWidth - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {boardWidth, boardWidth, boardWidth - externalReferencePoint}, new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint, 0}, new int[] {externalReferencePoint, externalReferencePoint, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, 4);
            g2.fillPolygon(new int[] {boardWidth - externalReferencePoint, boardWidth, boardWidth, boardWidth - externalReferencePoint}, new int[] {externalReferencePoint, externalReferencePoint, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, 4);

            // black lines and triangles
            g2.setColor(Color.BLACK);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {boardWidth, boardWidth, boardWidth - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, new int[] {boardWidth, boardWidth, boardWidth - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint, 0}, 4);
            g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint}, new int[] {boardWidth, boardWidth - externalReferencePoint, boardWidth - externalReferencePoint, boardWidth}, 4);

            // external thick square
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(thicknessExternal));
            g2.drawPolygon(new int[] {internalReferencePoint, internalReferencePoint, boardWidth - internalReferencePoint, boardWidth - internalReferencePoint}, new int[] {internalReferencePoint, boardWidth - internalReferencePoint, boardWidth - internalReferencePoint, internalReferencePoint}, 4);

            // internal lines
            g2.setStroke(new BasicStroke(thicknessInternal));

            // vertical lines
            for (int i = 3; i < numberTiles + 2; i++) {
                g2.drawLine(internalReferencePoint, PANEL_WIDTH * i, boardWidth - internalReferencePoint, PANEL_WIDTH * i);
            }

            // horizontal lines
            for (int i = 3; i < numberTiles + 2; i++) {
                g2.drawLine(PANEL_WIDTH * i, internalReferencePoint, PANEL_WIDTH * i, boardWidth - internalReferencePoint);
            }


            // pieces
            for(int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (game.getPiece(i, j) == 1) {
                        g2.setColor(Color.BLACK);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        }
                    if (game.getPiece(i, j) == 2) {
                        g2.setColor(Color.WHITE);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        }
                    if (game.getPiece(i, j) == 3) {
                        g2.setColor(Color.BLACK);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        g2.setColor(Color.GREEN);
                        g2.drawOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                    }
                    if (game.getPiece(i, j) == 4) {
                        g2.setColor(Color.WHITE);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        g2.setColor(Color.GREEN);
                        g2.drawOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                    }
                }
            }
        }
    }

    /**
     * Panel that does not allow for pawn placement
     */
    private class UnclickablePanel extends JPanel{
        final Dimension internalBoardDimension;

        /**
         * Constructor for UnclickablePanel
         */
        UnclickablePanel(){
            setOpaque(false);

            // set preferred size for button placement grid
            internalBoardDimension = new Dimension(PANEL_WIDTH * (size), PANEL_WIDTH * (size));
            setPreferredSize(internalBoardDimension);
        }
    }

    /**
     * Panel that allows for pawn placement
     */
    private class ClickablePanel extends UnclickablePanel{
        private final JButton[][] squares;
        ClickablePanel(){
            super();
            setLayout(new GridLayout(size, size));

            // Create an array to hold the chessboard squares
            squares = new JButton[size][size];

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
                    final int finalRow = row;
                    final int finalCol = col;
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // if move is doable, add pawn and progress
                            if (game.checkEmpty(finalCol, finalRow) && game.checkDiagonal(finalCol, finalRow)){
                                game.addPiece(finalCol, finalRow, game.getCurrentPlayer());
                                game.progress();
                                boardPanel.setTurnLabel();
                            }
                            // if special case, recreate board to allow for new buttons
                            if (game.getTurn() == 1 || game.getTurn() == 2 || game.checkPassable() || game.isFinished()){
                                createGameCard();
                            }
                        }
                    });

                    // set the hoveredRow and hoveredCol variables when the mouse enters the button
                    // reset when the mouse leaves the button
                    button.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
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

        /*
         * Method to paint hovered cells based on whether a pawn can be placed there from the current player
         *
         * @param g Graphics component
         */
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);

            // color hovered cells green if a pawn can be placed there, otherwise red
            if (hoveredCol > -1) {
                if (game.checkEmpty(hoveredCol, hoveredRow) && game.checkDiagonal(hoveredCol, hoveredRow)){
                    g2.setColor(Color.GREEN);
                }
                else {
                    g2.setColor(Color.RED);
                }
                g2.fillOval(PANEL_WIDTH * hoveredCol, PANEL_WIDTH * hoveredRow, PANEL_WIDTH, PANEL_WIDTH);
            }
        }
    }

    /**
     * MenuBar class
     */
    private class MenuBar extends JMenuBar {

        /**
         * MenuBar constructor
         */
        private MenuBar(){
            final JMenu fileMenu = new JMenu("File");

            // new game item
            final JMenuItem newGameMenuItem = new JMenuItem("New Game");
            newGameMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CARDS.show(contentPane, "selectSizePanel");
                }
            });

            // load game item
            final JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
            loadGameMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    // load game
                    loadGame();

                    // create new game card
                    createGameCard();
                }
            });

            // exit program item
            final JMenuItem exitMenuItem = new JMenuItem("Exit");
            exitMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            fileMenu.add(newGameMenuItem);
            fileMenu.add(loadGameMenuItem);
            fileMenu.add(exitMenuItem);

            this.add(fileMenu);
        }
    }

    /**
     * Method to initialize the game given size of the board
     *
     * @param size int representing the size of the board
     */
    private void initializeGame(int size){
        game = new Game(size);
        createGameCard();
    }

    /**
     * Method to recreate the board panel when needed
     */
    private void createGameCard(){
        boardPanel = new BoardPanel();
        contentPane.add("boardPanel", boardPanel);
        CARDS.show(contentPane, "boardPanel");
    }

    /**
     * Method to save the game.
     * Saves, in order: size, grid, current player and turn.
     * Saves to a file with extension .game.
     */
    public void saveGame() {

        // setup JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        // save game using .game extension
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game files (*.game)", "game"));

        // check if JFileChooser goes through
        int result = fileChooser.showSaveDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            // get file and fileName
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();

            // if file name is missing the extension
            if (!fileName.endsWith(".game")) {
                fileName += ".game";
            }

            // try to read the file and write each object in order
            try (FileOutputStream fos = new FileOutputStream(fileName);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(this.game.getSize());
                oos.writeObject(this.game.getGrid());
                oos.writeObject(this.game.getCurrentPlayer());
                oos.writeObject(this.game.getTurn());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to load the game.
     * Loads, in order: size, grid, current player and turn.
     * Loads from a file with extension .game.
     */
    public void loadGame() {
        // setup JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");

        // filter files with .game extension
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game files (*.game)", "game"));

        // check if JFileChooser goes through
        int result = fileChooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            // get file and fileName
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();

            // if file name is missing the extension
            if (!fileName.endsWith(".game")) {
                fileName += ".game";
            }

            // try to read the file and read each object in order
            try (FileInputStream fis = new FileInputStream(fileName);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                // load size and number of tiles
                this.setSize((int) ois.readObject());
                this.setNumberTiles(size - 1);

                // start game based on size
                game = new Game(size);

                // load grid, player and turn
                game.setGrid((int[][]) ois.readObject());
                game.loadCurrentPlayer((int) ois.readObject());
                game.setTurn((int) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method used when clicking on the size selection buttons
     *
     * @param size int representing the size of the grid
     */
    public void selectSize(int size) {
        this.setSize(size);
        this.setNumberTiles(size - 1);
        initializeGame(size);
    }

    /**
     * Method used to set the size variable
     *
     * @param size int representing the size of the grid
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Method used to set the numberTiles variable
     *
     * @param numberTiles int representing the size of the grid
     */
    public void setNumberTiles(int numberTiles) {
        this.numberTiles = numberTiles;
    }
}

