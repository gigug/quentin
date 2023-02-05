package gui;

import players.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panel that displays turn, grid and options
 *
 * @author Gianluca Guglielmo
 */
class BoardPanel extends JPanel {
    JPanel turnPanel = new JPanel();
    JLabel circleLabel = new JLabel();
    TitleLabel turnLabel = new TitleLabel();

    GameGUI gameGUI;
    /**
     * Default constructor for BoardPanel.
     */
    BoardPanel(GameGUI gameGUI){
        super();

        this.gameGUI = gameGUI;

        turnPanel.setOpaque(false);

        setLayout(GameGUI.GRID_BAG_LAYOUT);
        setBackground(GameGUI.BACKGROUND_COLOR_3);

        InternalBoardPanel internalBoardPanel = new InternalBoardPanel(gameGUI);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = GameGUI.INSETS;

        // turnPanel displays the current player and a small colored circle indicating its stone
        turnPanel.add(turnLabel);
        turnPanel.add(circleLabel);
        setTurnLabel();

        add(turnPanel, gbc);
        add(internalBoardPanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GameGUI.BACKGROUND_COLOR_3);

        // undo move button
        BlackButton undoButton = new BlackButton("Undo move");
        undoButton.addActionListener(e -> {
            gameGUI.game.undoMove();
            gameGUI.createGameCard();
        });
        buttonPanel.add(undoButton);

        BlackButton pieRuleButton = new BlackButton("Switch sides");
        pieRuleButton.addActionListener(e -> {
            gameGUI.game.pieRule();
            gameGUI.createGameCard();
        });
        pieRuleButton.setEnabled(false);
        buttonPanel.add(pieRuleButton);

        BlackButton passableButton = new BlackButton("Pass");
        passableButton.addActionListener(e -> {
            gameGUI.game.switchPlayer();
            gameGUI.createGameCard();
        });
        passableButton.setEnabled(false);
        buttonPanel.add(passableButton);

        // save game button
        BlackButton saveGameMenuButton = new BlackButton("Save Game");
        saveGameMenuButton.addActionListener(e -> gameGUI.saveGame());
        buttonPanel.add(saveGameMenuButton);

        // Disable undoButton at the beginning and at the end of the game
        if (gameGUI.game.getTurn() == 0 || gameGUI.game.isGameFinished()) undoButton.setEnabled(false);
        // During the second turn, the pie rule can be invoked by the white player
        if (gameGUI.game.getTurn() == 1) pieRuleButton.setEnabled(true);
        // Enable passableButton when necessary
        if (gameGUI.game.checkPassable() && !gameGUI.game.isGameFinished()) passableButton.setEnabled(true);
        // Disable saveGame at the end of the game
        if (gameGUI.game.isGameFinished()) saveGameMenuButton.setEnabled(false);

        add(buttonPanel, gbc);
    }

    /**
     * Method to set the panel containing the label and the circle correctly.
     */
    void setTurnLabel() {
        String text;
        Color circleColor;
        BufferedImage circleImage;
        ImageIcon circleIcon;

        // Display final message if game is finished
        if (gameGUI.game.isGameFinished()){
            if (gameGUI.game.getWinner() == 0){
                text = "Tie!";
                circleColor = Color.BLUE;
            }
            else{
                text = (gameGUI.game.getWinner() == 1) ? "Winner: black":"Winner: white";
                circleColor = (gameGUI.game.getWinner() == 1) ? Color.BLACK:Color.WHITE;
            }

        }
        // Display whose turn it is if game is not finished
        else{
            text = (gameGUI.game.getCurrentPlayer() == PlayerColor.BLACK) ? "Turn: black":"Turn: white";
            circleColor = (gameGUI.game.getCurrentPlayer() == PlayerColor.BLACK) ? Color.BLACK:Color.WHITE;
        }

        // Create a new image that will hold the circle
        circleImage = new BufferedImage(GameGUI.DIAMETER_ICON, GameGUI.DIAMETER_ICON, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImage.createGraphics();

        // Fill the circle with the right color
        g2.setColor(circleColor);
        g2.fillOval(0, 0, GameGUI.DIAMETER_ICON, GameGUI.DIAMETER_ICON);
        g2.dispose();

        // Create an ImageIcon from the circle image
        circleIcon = new ImageIcon(circleImage);

        circleLabel.setIcon(circleIcon);
        turnLabel.setText(text);
    }
}

