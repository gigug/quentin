package gui;

import objects.FrozenBoard;
import players.PlayerColor;
import screens.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class that implements the GUI for the game Quentin.
 *
 * @author Gianluca Guglielmo
 */
public class GameGUI extends JFrame{
    final static Font FONT_BIG = new Font("monospaced", Font.BOLD, 35);
    final static Font FONT_SMALL = new Font("monospaced", Font.PLAIN, 20);
    final static Color FONT_COLOR = Color.decode("#E1F2FE");
    final static Color BACKGROUND_COLOR_1 = Color.decode("#F46036");
    final static Color BACKGROUND_COLOR_2 = Color.decode("#8963BA");
    final static Color BACKGROUND_COLOR_3 = Color.decode("#32936F");
    final static Color BOARD_COLOR = Color.decode("#815E5B");
    private final static Dimension MINIMUM_WINDOW_DIMENSION = new Dimension(1000, 1000);
    final static int DIAMETER_ICON = 30;
    final static int PANEL_WIDTH = 30;
    final static Insets INSETS = new Insets(5, 0, 5, 0);
    // CardLayout that will be used to switch between different panels
    public final CardLayout CARDS = new CardLayout();
    final static GridBagLayout GRID_BAG_LAYOUT = new GridBagLayout();

    Game game;

    // Container with CardLayout
    final Container contentPane;

    // Game-related variables
    private int size;
    int numberTiles;

    // GUI-related variables
    int hoveredRow = -1;
    int hoveredCol = -1;

    BoardPanel boardPanel;

    /**
     * Constructor for the GameGUI class.
     */
    public GameGUI(){

        // Initialize contentPane
        contentPane = getContentPane();

        StartMenuPanel startMenuPanel = new StartMenuPanel(this);
        SelectSizePanel selectSizePanel = new SelectSizePanel(this);

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
        boardPanel = new BoardPanel(this);
        contentPane.add("boardPanel", boardPanel);
        CARDS.show(contentPane, "boardPanel");
    }

    /**
     * Method that centers the window frame.
     *
     * @param frame to center.
     */
    private static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    /**
     * Method to save the game.
     * Saves, in order: size, grid, current player and turn.
     * Saves to a file with extension .game.
     */
    void saveGame() {
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
            if (!fileName.endsWith(".game")) fileName += ".game";

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
                game.loadGrid((int[][]) ois.readObject());
                game.loadCurrentPlayer((PlayerColor) ois.readObject());
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
    void selectSize(int size) {
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

