package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class implementing the info panel.
 */
class InfoPanel extends JPanel {
    private static final Color BACKGROUND_COLOR_INFO = Color.decode("#74121D");
    private final JLabel label;
    private int currentText = 0;
    private final String[] texts = {
            "<html> <h3>Introduction</h3>\n" +
            "<p>Quentin is a connection game for two players: Black and White. It's played on the intersections (points) of a square board, which is initially empty. The top and bottom edges of the board are colored black; the left and right edges are colored white. " +
            "The size of the board is not fixed. " +
            "Quentin is a game designed by Luis Bolaños Mures in 2012. It is worth noting that it has recently been renamed Quinten, along with a slight update to its original rules. " +
            "To avoid any version conflict, this game implementation will explicitly refer to the original 2012 rules, which are here described in the \"Rules\" section.</p>",
            "<html><h3>Definitions</h3>\n" +
            "  <p>Stones are placed on the intersections of the board. They could form the following objects, which are noteworthy for the description of the game:</p>\n" +
            "  <ul>\n" +
            "    <li>chain: set of like-colored, orthogonally adjacent stones;</li>\n" +
            "    <li>region: a set of orthogonally adjacent empty points completely surrounded by stones or board edges;</li>\n" +
            "    <li>territory: if all points in a region are orthogonally adjacent to at least two stones, said region is also a territory.</li>\n" +
            "  </ul></html>",
            "<html><h3>Play</h3>\n" +
            "  <p>Black plays first, then turns alternate.</p>\n" +
            "  <p>On his turn, a player must place one stone of his color on an empty point. Then, every territory on the board is filled with stones of the player who has the majority of stones orthogonally adjacent to it, after which the turn ends. Territories with the same number of Black and White stones adjacent to it are filled with stones of the opponent's color.</p>\n" +
            "  <p>At the end of a turn, any two like-colored, diagonally adjacent stones must share at least one orthogonally adjacent, like-colored neighbor. Otherwise, the move is illegal and the player must choose another one. If a player can't make a move on his turn, he must pass. Passing is otherwise not allowed.</p>\n" +
            "  <p>There will always be a move available to at least one of the players, unless intersections have been filled entirely.</p></html>",
            "<html> <h3>Game end </h3>" +
            "<p>The game is won by the player who completes a chain of his color touching the two opposite board edges of his color.</p>\n" +
            "<p>A draw is possible if the board is filled entirely without any winner, or if both players win at the same time, which is a rare event made possible by the territory filling rule.</p></html>",
            "<html><h3>Pie rule</h3>\n" +
            "<p>The pie rule is used in order to make the game fair. This means that White will have the option, on his first turn only, to change sides instead of making a regular move.</p></html>"
    };

    /**
     * Constructor for InfoPanel.
     */
    InfoPanel(GameGUI gameGUI){
        super();
        setLayout(GameGUI.GRID_BAG_LAYOUT);
        setBackground(BACKGROUND_COLOR_INFO);

        Constraints constraintsPanel = new Constraints(false);
        Constraints constraintsButtons = new Constraints(true);

        label = new InfoLabel(texts[currentText]);
        add(label, constraintsPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR_INFO);
        add(buttonPanel, constraintsPanel);

        StandardButton mainMenuButton = new StandardButton("Back");
        mainMenuButton.addActionListener(gameGUI.mainMenuActionListener);
        buttonPanel.add(mainMenuButton, constraintsButtons);

        StandardButton prevButton = new StandardButton("<");
        ActionListener prevActionListener = e -> {
            currentText = currentText > 0 ? currentText - 1 : texts.length - 1;
            label.setText(texts[currentText]);
        };
        prevButton.addActionListener(prevActionListener);
        buttonPanel.add(prevButton, constraintsButtons);

        StandardButton nextButton = new StandardButton(">");
        ActionListener nextActionListener = e -> {
            currentText = currentText < texts.length - 1 ? currentText + 1 : 0;
            label.setText(texts[currentText]);
        };
        nextButton.addActionListener(nextActionListener);
        buttonPanel.add(nextButton, constraintsButtons);
    }

    /**
     * Private static class to handle the text displayed in InfoPanel.
     */
    private static class InfoLabel extends JLabel{
        private static final Font FONT_SMALL = new Font("monospaced", Font.PLAIN, 15);
        private static final Dimension TEXT_DIMENSION = new Dimension(500, 500);

        /**
         * Constructor for InfoLabel.
         *
         * @param text string to display.
         */
        InfoLabel(String text){
            super(text);
            setForeground(Color.WHITE);
            setFont(FONT_SMALL);
            setMinimumSize(TEXT_DIMENSION);
        }
    }
}
