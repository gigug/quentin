package gui;

import game.FrozenGame;
import objects.FrozenBoard;
import players.PlayerColor;
import game.Game;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Stack;

/**
 * Class that implements the GUI for the game Quentin.
 *
 * @author Gianluca Guglielmo
 */
public class GameGUI extends JFrame{
    private final static Dimension MINIMUM_WINDOW_DIMENSION = new Dimension(1000, 1000);
    final static int PANEL_WIDTH = 30;

    // CardLayout used to switch between different panels
    final CardLayout CARDS = new CardLayout();
    final static GridBagLayout GRID_BAG_LAYOUT = new GridBagLayout();

    Game game;

    // GUI-related variables
    int hX = -1;
    int hY = -1;

    BoardPanel boardPanel;

    ActionListener mainMenuActionListener = e -> CARDS.show(getContentPane(), "startMenuPanel");
    ActionListener newGameActionListener = e -> CARDS.show(getContentPane(), "selectSizePanel");
    ActionListener loadGameActionListener = e -> {
        loadGame();
        createGameCard();
    };
    ActionListener infoActionListener = e -> CARDS.show(getContentPane(), "infoPanel");
    ActionListener exitGameActionListener = e -> System.exit(0);
    ActionListener undoMoveActionListener = e -> {
        game.undoMove();
        createGameCard();
    };
    ActionListener pieRuleActionListener = e -> {
        game.pieRule();
        createGameCard();
    };
    ActionListener passActionListener = e -> {
        game.switchPlayer();
        createGameCard();
    };
    ActionListener saveGameActionListener = e -> saveGame();
    /**
     * Constructor for the GameGUI class.
     */
    public GameGUI(){
        setLayout(CARDS);

        StartMenuPanel startMenuPanel = new StartMenuPanel(this);
        add("startMenuPanel", startMenuPanel);

        SelectSizePanel selectSizePanel = new SelectSizePanel(this);
        add("selectSizePanel", selectSizePanel);

        InfoPanel infoPanel = new InfoPanel(this);
        add("infoPanel", infoPanel);

        MenuBar menuBar = new MenuBar(this);
        setJMenuBar(menuBar);

        setTitle("Quentin");

        // Window settings
        setResizable(true);
        setVisible(true);
        setMinimumSize(MINIMUM_WINDOW_DIMENSION);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        centerWindow();
    }

    /**
     * Method to initialize the game given size of the board.
     *
     * @param size int representing the size of the board.
     */
    void initializeGame(int size){
        game = new Game(size);
        createGameCard();
    }

    /**
     * Method to initialize the game given additional features when loading game.
     *
     * @param size int representing the size of the board.
     * @param grid loaded grid.
     * @param currentPlayer player that needs to move.
     * @param turn current turn.
     * @param previousGridStack previous moves.
     */
    void initializeGame(int size, int[][] grid, PlayerColor currentPlayer, int turn, Stack<int[][]> previousGridStack){
        game = new Game(size, grid, currentPlayer, turn, previousGridStack);
        createGameCard();
    }

    /**
     * Method to recreate the board panel when needed.
     */
    void createGameCard(){
        boardPanel = new BoardPanel(this);
        add("boardPanel", boardPanel);
        CARDS.show(this.getContentPane(),"boardPanel");
    }

    /**
     * Getter for numberTiles.
     *
     * @return numberTiles - int representing the number of tiles along a dimension.
     */
    int getNumberTiles(){
        return getGridSize() - 1;
    }

    /**
     * Getter for size. g
     *
     * @return numberTiles - int representing the size of the grid.
     */
    int getGridSize(){
        return game.getSize();
    }

    /**
     * Method to carry out the sequence of actions needed at the end of a turn.
     *
     * @param X int representing the X coordinate of the stone.
     * @param Y int representing the Y coordinate of the stone
     */
    void progress(int X, int Y){
        game.progress(X, Y);
        boardPanel.setTurnLabel();
    }

    /**
     * Method to avoid duplication on save/load file.
     *
     * @param save boolean indicating whether to display save or load text.
     * @return fileName.
     */
    private String fileSelector(boolean save){
        JFileChooser fileChooser = new JFileChooser(save ? "Choose save file" : "Choose load file");

        // Use .game extension
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game files (*.game)", "game"));

        // Check if JFileChooser goes through
        int result = fileChooser.showSaveDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();

            if (!fileName.endsWith(".game")) fileName += ".game";

            return fileName;
        }

        return null;
    }

    /**
     * Method to load the game.
     * Loads from a file with extension .game.
     */
    private void loadGame(){
        String fileName = fileSelector(false);
        loadObjectsFromFile(fileName);
    }

    /**
     * Method to save the game. Saves to a file with extension .game.
     */
    void saveGame(){
        String fileName = fileSelector(true);
        writeObjectsToFile(fileName);
    }

    /**
     * Method to load game objects given fileName.
     *
     * @param fileName string indicating the file to load.
     */
    private void loadObjectsFromFile(String fileName) {
        int size;
        int[][] grid;
        PlayerColor currentPlayer;
        int turn;
        Stack<int[][]> previousGridStack;

        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            size = (int) ois.readObject();
            grid = (int[][]) ois.readObject();
            currentPlayer = (PlayerColor) ois.readObject();
            turn = (int) ois.readObject();
            previousGridStack = (Stack<int[][]>) ois.readObject();

            initializeGame(size, grid, currentPlayer, turn, previousGridStack);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to save game objects given fileName.
     *
     * @param fileName string indicating the file to save into.
     */
    private void writeObjectsToFile(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game.getSize());
            oos.writeObject(game.getGrid());
            oos.writeObject(game.getCurrentPlayer());
            oos.writeObject(game.getTurn());
            oos.writeObject(game.getPreviousGridStack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that centers the window frame.
     */
    private void centerWindow() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
    }

    /**
     * Method to return frozen version of the game for safe handling;
     *
     * @return frozenGame.
     */
    FrozenGame getFrozenGame() {
        return game;
    }
}

