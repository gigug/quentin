package gui;

import players.PlayerColor;
import screens.Game;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Stack;

/**
 * Utility class with several methods used from other GUI classes.
 *
 * @author Gianluca Guglielmo.
 */
class UtilsGUI {

    /**
     * Private constructor for UtilsGUI, cannot be used.
     */
    private UtilsGUI() {}

    /**
     * Method that centers the window frame.
     *
     * @param frame to center.
     */
    static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    /**
     * Method to save the game.
     * Saves to a file with extension .game.
     */
    static void saveGame(GameGUI gameGUI) {
        actFile(gameGUI, true);
    }

    /**
     * Method to save game objects given fileName.
     *
     * @param gameGUI current gameGUI.
     * @param fileName string indicating the file to save into.
     */
    private static void writeObjectsToFile(GameGUI gameGUI, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameGUI.game.getSize());
            oos.writeObject(gameGUI.game.getGrid());
            oos.writeObject(gameGUI.game.getCurrentPlayer());
            oos.writeObject(gameGUI.game.getTurn());
            oos.writeObject(gameGUI.game.getPreviousGridStack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to load the game.
     * Loads from a file with extension .game.
     */
    static void loadGame(GameGUI gameGUI) {
        actFile(gameGUI, false);
    }

    /**
     * Method to load game objects given fileName.
     *
     * @param gameGUI current gameGUI.
     * @param fileName string indicating the file to load.
     */
    private static void loadObjectsFromFile(GameGUI gameGUI, String fileName){
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameGUI.setSize((int) ois.readObject());
            gameGUI.setNumberTiles(gameGUI.getGridSize() - 1);
            gameGUI.game = new Game(gameGUI.getGridSize());
            gameGUI.game.loadGrid((int[][]) ois.readObject());
            gameGUI.game.loadCurrentPlayer((PlayerColor) ois.readObject());
            gameGUI.game.setTurn((int) ois.readObject());
            gameGUI.game.setPreviousGridStack((Stack<int[][]>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Auxiliary method used in saveGame and loadGame methods.
     *
     * @param gameGUI current gameGUI.
     * @param save boolean indicating whether to save or load the game.
     */
    private static void actFile(GameGUI gameGUI, boolean save){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(save ? "Save Game" : "Load Game");

        // Use .game extension
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game files (*.game)", "game"));

        // Check if JFileChooser goes through
        int result = fileChooser.showSaveDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();

            if (!fileName.endsWith(".game")) fileName += ".game";

            if (save) writeObjectsToFile(gameGUI, fileName);
            else loadObjectsFromFile(gameGUI, fileName);
        }
    }

}



