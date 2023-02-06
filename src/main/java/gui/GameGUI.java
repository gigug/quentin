package gui;

import screens.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class that implements the GUI for the game Quentin.
 *
 * @author Gianluca Guglielmo
 */
public class GameGUI extends JFrame{
    private final static Dimension MINIMUM_WINDOW_DIMENSION = new Dimension(1000, 1000);
    final static int PANEL_WIDTH = 30;
    final static Insets INSETS = new Insets(5, 0, 5, 0);

    // CardLayout used to switch between different panels
    final CardLayout CARDS = new CardLayout();
    final static GridBagLayout GRID_BAG_LAYOUT = new GridBagLayout();

    Game game;

    // Game-related variables
    private int size;
    private int numberTiles;

    // GUI-related variables
    int hX = -1;
    int hY = -1;

    BoardPanel boardPanel;

    ActionListener newGameActionListener = e -> CARDS.show(getContentPane(), "selectSizePanel");
    ActionListener loadGameActionListener = e -> {
        UtilsGUI.loadGame(this);
        createGameCard();
    };
    ActionListener infoActionListener = e -> CARDS.show(getContentPane(), "infoPanel");
    ActionListener exitGameActionListener = e -> System.exit(0);
    ActionListener saveGameActionListener = e -> UtilsGUI.saveGame(this);
    ActionListener passActionListener = e -> {
        game.switchPlayer();
        createGameCard();
    };
    ActionListener pieRuleActionListener = e -> {
        game.pieRule();
        createGameCard();
    };
    ActionListener undoMoveActionListener = e -> {
        game.undoMove();
        createGameCard();
    };

    /**
     * Constructor for the GameGUI class.
     */
    public GameGUI(){
        setLayout(CARDS);

        StartMenuPanel startMenuPanel = new StartMenuPanel(this);
        add("startMenuPanel", startMenuPanel);

        SelectSizePanel selectSizePanel = new SelectSizePanel(this);
        add("selectSizePanel", selectSizePanel);

        InfoPanel infoPanel = new InfoPanel();
        add("infoPanel", infoPanel);

        MenuBar menuBar = new MenuBar(this);

        setTitle("Quentin");
        setJMenuBar(menuBar);

        // Window settings
        setResizable(true);
        setVisible(true);
        setMinimumSize(MINIMUM_WINDOW_DIMENSION);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        UtilsGUI.centerWindow(this);
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
     * Method to recreate the board panel when needed.
     */
    void createGameCard(){
        boardPanel = new BoardPanel(this);
        add("boardPanel", boardPanel);
        CARDS.show(this.getContentPane(),"boardPanel");
    }

    /**
     * Method used to set the size variable.
     *
     * @param size int representing the size of the grid.
     */
    void setSize(int size) {
        this.size = size;
    }

    /**
     * Method used to set the numberTiles variable.
     *
     * @param numberTiles int representing the size of the grid.
     */
    void setNumberTiles(int numberTiles) {
        this.numberTiles = numberTiles;
    }

    /**
     * Getter for numberTiles.
     *
     * @return numberTiles - int representing the number of tiles along a dimension.
     */
    int getNumberTiles(){
        return numberTiles;
    }

    /**
     * Getter for size. g
     *
     * @return numberTiles - int representing the size of the grid.
     */
    int getGridSize(){
        return size;
    }
}

