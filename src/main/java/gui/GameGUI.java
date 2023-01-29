package gui;

import screens.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import static gui.FunctionsGUI.centerWindow;

/**
 * Class that implements the GUI for the game Quentin.
 */
public class GameGUI extends JFrame{

    // Constants
    final static Font FONT_BIG = new Font("monospaced", Font.BOLD, 35);
    final static Font FONT_SMALL = new Font("monospaced", Font.PLAIN, 20);
    final static Color FONT_COLOR = Color.decode("#E1F2FE");
    private final static Color BACKGROUND_COLOR_1 = Color.decode("#F46036");
    private final static Color BACKGROUND_COLOR_2 = Color.decode("#8963BA");
    private final static Color BACKGROUND_COLOR_3 = Color.decode("#32936F");
    private final static Color BOARD_COLOR = Color.decode("#815E5B");
    private final static Dimension MINIMUM_WINDOW_DIMENSION = new Dimension(1000, 1000);
    private final static int DIAMETER_ICON = 30;
    private final static int PANEL_WIDTH = 30;
    private final static Insets INSETS = new Insets(5, 0, 5, 0);
    // CardLayout that will be used to switch between different panels
    public final CardLayout CARDS = new CardLayout();
    private final static GridBagLayout GRID_BAG_LAYOUT = new GridBagLayout();

    private Game game;

    // Container with CardLayout
    private final Container contentPane;

    // Game-related variables
    private int size;
    private int numberTiles;

    // GUI-related variables
    private int hoveredRow = -1;
    private int hoveredCol = -1;

    private BoardPanel boardPanel;

    /**
     * Constructor for the GameGUI class.
     */
    public GameGUI(){

        // Initialize contentPane
        contentPane = getContentPane();

        StartMenuPanel startMenuPanel = new StartMenuPanel();
        SelectSizePanel selectSizePanel = new SelectSizePanel();

        MenuBar menuBar = new MenuBar(this);

        // Initialize cards
        contentPane.setLayout(CARDS);
        contentPane.add("startMenuPanel", startMenuPanel);
        contentPane.add("selectSizePanel", selectSizePanel);

        // Title and size
        setTitle("Quentin");

        // Add table menu bar to screen
        setJMenuBar(menuBar);

        // Window settings
        setResizable(true);
        setVisible(true);
        setMinimumSize(MINIMUM_WINDOW_DIMENSION);

        // Center window
        centerWindow(this);

        // Close and not just hide
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Panel that displays start menu.
     */
    private class StartMenuPanel extends JPanel{

        /**
         * Constructor for the StartMenuPanel class.
         */
        public StartMenuPanel(){
            super();

            // Set GridBagLayout for the buttons
            setLayout(GRID_BAG_LAYOUT);

            // Set background color
            setBackground(BACKGROUND_COLOR_1);

            // Start new game
            BlackButton newGameButton = new BlackButton("New game");
            newGameButton.addActionListener(e -> CARDS.show(contentPane, "selectSizePanel"));

            // Load game
            BlackButton loadGameButton = new BlackButton("Load game");
            loadGameButton.addActionListener(e -> {
                loadGame();
                createGameCard();
            });

            // Exit game
            BlackButton exitGameButton = new BlackButton("Exit game");
            exitGameButton.addActionListener(e -> System.exit(0));

            // Constraints for grid bag layout
            GridBagConstraints gbc = new GridBagConstraints();

            // Order elements vertically by setting one element per row
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            // Set small border between buttons
            gbc.insets = INSETS;

            // Add label to panel
            TitleLabel label = new TitleLabel("Quentin");
            add(label, gbc);

            // Make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Create new panel for buttons
            JPanel buttons = new JPanel(GRID_BAG_LAYOUT);

            // Using same constraints for buttons
            buttons.add(newGameButton, gbc);
            buttons.add(loadGameButton, gbc);
            buttons.add(exitGameButton, gbc);
            buttons.setBackground(BACKGROUND_COLOR_1);
            add(buttons, gbc);
        }
    }

    /**
     * Panel that displays size selection menu.
     */
    private class SelectSizePanel extends JPanel{

        /**
         * Default constructor for SelectSizePanel.
         */
        public SelectSizePanel(){
            super();

            // Set GridBagLayout for the buttons
            setLayout(GRID_BAG_LAYOUT);

            // Set background color
            setBackground(BACKGROUND_COLOR_2);

            BlackButton size7Button = new BlackButton("7x7");
            size7Button.addActionListener(e->selectSize(8));

            BlackButton size9Button = new BlackButton("9x9");
            size9Button.addActionListener(e->selectSize(10));

            BlackButton size11Button = new BlackButton("11x11");
            size11Button.addActionListener(e->selectSize(12));

            GridBagConstraints gbc = new GridBagConstraints();

            // Order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            // Set small border between buttons
            gbc.insets = INSETS;

            TitleLabel label = new TitleLabel("Select board size:");
            add(label, gbc);

            // Make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(GRID_BAG_LAYOUT);
            // Using same constraints for buttons
            buttons.add(size7Button, gbc);
            buttons.add(size9Button, gbc);
            buttons.add(size11Button, gbc);
            buttons.setBackground(BACKGROUND_COLOR_2);
            add(buttons, gbc);
        }
    }

    /**
     * Panel that displays turn, grid and options
     */
    private class BoardPanel extends JPanel{
        JPanel turnPanel = new JPanel();
        JLabel circleLabel = new JLabel();
        TitleLabel turnLabel = new TitleLabel();

        /**
         * Default constructor for BoardPanel.
         */
        BoardPanel(){
            super();

            turnPanel.setOpaque(false);

            setLayout(GRID_BAG_LAYOUT);
            setBackground(BACKGROUND_COLOR_3);

            InternalBoardPanel internalBoardPanel = new InternalBoardPanel();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = INSETS;

            // turnPanel displays the current player and a small colored circle indicating its stone
            turnPanel.add(turnLabel);
            turnPanel.add(circleLabel);
            setTurnLabel();

            add(turnPanel, gbc);
            add(internalBoardPanel, gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(BACKGROUND_COLOR_3);

            // undo move button
            BlackButton undoButton = new BlackButton("Undo move");
            undoButton.addActionListener(e -> {
                game.undoMove();
                createGameCard();
            });
            buttonPanel.add(undoButton);

            BlackButton pieRuleButton = new BlackButton("Switch sides");
            pieRuleButton.addActionListener(e -> {
                game.pieRule();
                createGameCard();
            });
            pieRuleButton.setEnabled(false);
            buttonPanel.add(pieRuleButton);

            BlackButton passableButton = new BlackButton("Pass");
            passableButton.addActionListener(e -> {
                game.switchPlayer();
                createGameCard();
            });
            passableButton.setEnabled(false);
            buttonPanel.add(passableButton);

            // save game button
            BlackButton saveGameMenuButton = new BlackButton("Save Game");
            saveGameMenuButton.addActionListener(e -> saveGame());
            buttonPanel.add(saveGameMenuButton);

            // Disable undoButton at the beginning and at the end of the game
            if (game.getTurn() == 0 || game.isFinished()) undoButton.setEnabled(false);
            // During the second turn, the pie rule can be invoked by the white player
            if (game.getTurn() == 1) pieRuleButton.setEnabled(true);
            // Enable passableButton when necessary
            if (game.checkPassable() && !game.isFinished()) passableButton.setEnabled(true);
            // Disable saveGame at the end of the game
            if (game.isFinished()) saveGameMenuButton.setEnabled(false);

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

            // Display final message if game is finished
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
            // Display whose turn it is if game is not finished
            else{
                text = (game.getCurrentPlayer() == 1) ? "Turn: black":"Turn: white";
                circleColor = (game.getCurrentPlayer() == 1) ? Color.BLACK:Color.WHITE;
            }

            // Create a new image that will hold the circle
            circleImage = new BufferedImage(DIAMETER_ICON, DIAMETER_ICON, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleImage.createGraphics();

            // Fill the circle with the right color
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
     * Panel that draws the grid.
     */
    private class InternalBoardPanel extends JPanel{
        private final int boardWidth;

        /**
         * Default constructor for InternalBoardPanel.
         */
        InternalBoardPanel(){
            super();
            setLayout(GRID_BAG_LAYOUT);

            boardWidth = PANEL_WIDTH * (numberTiles + 4);
            Dimension boardDimension = new Dimension(boardWidth, boardWidth);

            setPreferredSize(boardDimension);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            ClickablePanel clickablePanel = new ClickablePanel();

            add(clickablePanel, gbc);
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
            for (int i = 3; i < numberTiles + 2; i++) {
                g2.drawLine(internalReferencePoint, PANEL_WIDTH * i, boardWidth - internalReferencePoint, PANEL_WIDTH * i);
            }

            // Horizontal lines
            for (int i = 3; i < numberTiles + 2; i++) {
                g2.drawLine(PANEL_WIDTH * i, internalReferencePoint, PANEL_WIDTH * i, boardWidth - internalReferencePoint);
            }

            Color colorFill;
            Color colorSurface = Color.GREEN;

            // stones
            for(int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (game.getStone(i,j) != 0){
                        colorFill = (game.getStone(i, j) == 1 || game.getStone(i, j) == 3) ? Color.BLACK : Color.WHITE;
                        g2.setColor(colorFill);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);

                        if (game.getStone(i, j) > 2){
                            g2.setColor(colorSurface);
                            g2.drawOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        }
                    }
                }
            }
        }
    }

    /**
     * Panel that draws board and stones.
     */
    private class ClickablePanel extends JPanel{

        final Dimension internalBoardDimension;
        final int internalBoardWidth;
        /**
         * Default constructor for ClickablePanel.
         */
        ClickablePanel(){
            super();

            setOpaque(false);

            // Set preferred size for button placement grid
            internalBoardWidth = PANEL_WIDTH * (size);
            internalBoardDimension = new Dimension(internalBoardWidth, internalBoardWidth);
            setPreferredSize(internalBoardDimension);

            if (!game.isFinished()){
                setLayout(new GridLayout(size, size));
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
                        final int finalRow = row;
                        final int finalCol = col;
                        button.addActionListener(e -> {
                            // If move is doable, add stone and progress
                            if (game.checkEmpty(finalCol, finalRow) && game.checkDiagonal(finalCol, finalRow)){
                                game.progress(finalCol, finalRow);
                                boardPanel.setTurnLabel();
                            }
                            // If special case, recreate board to allow for new buttons
                            if (game.getTurn() == 1 || game.getTurn() == 2 || game.checkPassable() || game.isFinished()){
                                createGameCard();
                            }
                        });

                        // Set the hoveredRow and hoveredCol variables when the mouse enters the button
                        // Reset when the mouse leaves the button
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
     * Method to initialize the game given size of the board.
     *
     * @param size int representing the size of the board.
     */
    private void initializeGame(int size){
        game = new Game(size);
        createGameCard();
    }

    /**
     * Method to recreate the board panel when needed.
     */
    void createGameCard(){
        boardPanel = new BoardPanel();
        contentPane.add("boardPanel", boardPanel);
        CARDS.show(contentPane, "boardPanel");
    }

    /**
     * Method to save the game.
     * Saves, in order: size, grid, current player and turn.
     * Saves to a file with extension .game.
     */
    private void saveGame() {

        // Setup JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");

        // Save game using .game extension
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game files (*.game)", "game"));

        // Check if JFileChooser goes through
        int result = fileChooser.showSaveDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get file and fileName
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();

            // If file name is missing the extension
            if (!fileName.endsWith(".game")) {
                fileName += ".game";
            }

            // Try to read the file and write each object in order
            try (FileOutputStream fos = new FileOutputStream(fileName);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(game.getSize());
                oos.writeObject(game.getGrid());
                oos.writeObject(game.getCurrentPlayer());
                oos.writeObject(game.getTurn());
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
    void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");

        // Filter files with .game extension
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game files (*.game)", "game"));

        // Check if JFileChooser goes through
        int result = fileChooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get file and fileName
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();

            // If file name is missing the extension, add it
            if (!fileName.endsWith(".game")) fileName += ".game";

            // Try to read the file and read each object in order
            try (FileInputStream fis = new FileInputStream(fileName);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                setSize((int) ois.readObject());
                setNumberTiles(size - 1);
                game = new Game(size);
                game.setGrid((int[][]) ois.readObject());
                game.loadCurrentPlayer((int) ois.readObject());
                game.setTurn((int) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method used when clicking on the size selection buttons.
     *
     * @param size int representing the size of the grid.
     */
    private void selectSize(int size) {
        setSize(size);
        setNumberTiles(size - 1);
        initializeGame(size);
    }

    /**
     * Method used to set the size variable.
     *
     * @param size int representing the size of the grid.
     */
    private void setSize(int size) {
        this.size = size;
    }

    /**
     * Method used to set the numberTiles variable.
     *
     * @param numberTiles int representing the size of the grid.
     */
    private void setNumberTiles(int numberTiles) {
        this.numberTiles = numberTiles;
    }
}

