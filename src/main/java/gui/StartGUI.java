package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

import static gui.FunctionsGUI.centerWindow;

public class StartGUI extends JFrame{
    public CardLayout cards;
    Container c;
    public StartGUI(){
        c = getContentPane();
        StartMenuPanel startMenuPanel = new StartMenuPanel();
        SelectionMenuPanel selectionMenuPanel = new SelectionMenuPanel();
        MenuBar menuBar = new MenuBar();

        cards = new CardLayout();
        c.setLayout(cards);
        c.add("start", startMenuPanel);
        c.add("selection", selectionMenuPanel);

        // title and size
        setTitle("Quentin");
        setSize(400, 400);
        setMinimumSize(new Dimension(300, 300));

        // Add table menu bar to screen
        setJMenuBar(menuBar);

        // center window
        centerWindow(this);

        // settings window
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private class StartMenuPanel extends JPanel{

        public StartMenuPanel(){
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setLayout(new GridBagLayout());

            // start new game
            JButton newGameButton = new JButton("New game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.next(c);
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

            add(new JLabel("Quentin"), gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            buttons.add(newGameButton, gbc);
            buttons.add(exitGameButton, gbc);

            add(buttons, gbc);

        }
    }

    private class SelectionMenuPanel extends JPanel{

        public SelectionMenuPanel(){
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setLayout(new GridBagLayout());

            // start new game
            JButton blackButton = new JButton("Black");
            blackButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.next(c);
                }
            });

            // exit game
            JButton whiteButton = new JButton("White");
            whiteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.next(c);
                }
            });

            // Add button to JPanel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            add(new JLabel("Select color:"), gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            buttons.add(blackButton, gbc);
            buttons.add(whiteButton, gbc);

            add(buttons, gbc);

        }
    }

}

