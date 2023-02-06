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
    private final static int DIAMETER_ICON = 30;
    private final static Color BACKGROUND_COLOR_BOARD = Color.decode("#32936F");

    private final JLabel circleLabel = new JLabel();
    private final TitleLabel turnLabel = new TitleLabel();
    private final StandardButton undoButton;
    private final StandardButton pieRuleButton;
    private final StandardButton passableButton;
    private final StandardButton saveGameMenuButton;
    GameGUI gameGUI;

    /**
     * Default constructor for BoardPanel.
     */
    BoardPanel(GameGUI gameGUI){
        super();
        this.gameGUI = gameGUI;

        setLayout(GameGUI.GRID_BAG_LAYOUT);
        setBackground(BACKGROUND_COLOR_BOARD);

        Constraints constraints = new Constraints(false);

        // turnPanel displays the current player and a small colored circle indicating its stone
        JPanel turnPanel = new JPanel();
        turnPanel.setOpaque(false);
        turnPanel.add(turnLabel);
        turnPanel.add(circleLabel);
        setTurnLabel();
        add(turnPanel, constraints);

        GridPanel internalBoardPanel = new GridPanel(gameGUI);
        add(internalBoardPanel, constraints);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR_BOARD);
        add(buttonPanel, constraints);

        undoButton = new StandardButton("Undo move");
        undoButton.addActionListener(gameGUI.undoMoveActionListener);
        buttonPanel.add(undoButton);

        pieRuleButton = new StandardButton("Switch sides");
        pieRuleButton.addActionListener(gameGUI.pieRuleActionListener);
        buttonPanel.add(pieRuleButton);

        passableButton = new StandardButton("Pass");
        passableButton.addActionListener(gameGUI.passActionListener);
        buttonPanel.add(passableButton);

        saveGameMenuButton = new StandardButton("Save Game");
        saveGameMenuButton.addActionListener(gameGUI.saveGameActionListener);
        buttonPanel.add(saveGameMenuButton);

        checkEnableButtons();
    }

    /**
     * Method used to check whether to enable or disable the Board buttons.
     */
    private void checkEnableButtons(){
        // Disable undoButton at the beginning and at the end of the game
        undoButton.setEnabled(gameGUI.game.getTurn() != 0 && !gameGUI.game.isGameFinished());
        // During the second turn, the pie rule can be invoked by the white player
        pieRuleButton.setEnabled(gameGUI.game.getTurn() == 1);
        // Enable passableButton when necessary
        passableButton.setEnabled(gameGUI.game.checkPassable() && !gameGUI.game.isGameFinished());
        // Disable saveGame at the end of the game
        saveGameMenuButton.setEnabled(!gameGUI.game.isGameFinished());
    }

    /**
     * Method to set the panel containing the label and the circle correctly.
     */
    void setTurnLabel() {
        String text;
        Color circleColor;
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
        else{
            text = (gameGUI.game.getCurrentPlayer() == PlayerColor.BLACK) ? "Turn: black":"Turn: white";
            circleColor = (gameGUI.game.getCurrentPlayer() == PlayerColor.BLACK) ? Color.BLACK:Color.WHITE;
        }

        circleIcon = getCircleIcon(circleColor);

        circleLabel.setIcon(circleIcon);
        turnLabel.setText(text);
    }

    /**
     * Method to draw the colored circle icon.
     *
     * @param circleColor color of the circle.
     * @return circleIcon.
     */
    ImageIcon getCircleIcon(Color circleColor){
        BufferedImage circleImage = new BufferedImage(DIAMETER_ICON, DIAMETER_ICON, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImage.createGraphics();

        g2.setColor(circleColor);
        g2.fillOval(0, 0, DIAMETER_ICON, DIAMETER_ICON);
        g2.dispose();

        return new ImageIcon(circleImage);
    }

}

