package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class StartGUI extends JFrame{

    public StartGUI(){
        MenuPanel menuPanel = new MenuPanel();
        MenuBar menuBar = new MenuBar();

        // title and size
        setTitle("Quentin");
        setSize(400, 400);

        // Add table menu bar to screen
        setJMenuBar(menuBar);

        // add JPanel to the JFrame itself
        getContentPane().add(menuPanel);

        // set layout
        //setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // settings window
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private static class MenuBar extends JMenuBar{

        public MenuBar(){
            final JMenu fileMenu = new JMenu("File");

            final JMenuItem newGameMenuItem = new JMenuItem("New Game");
            newGameMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("I was clicked!");
                }
            });
            fileMenu.add(newGameMenuItem);

            final JMenuItem exitMenuItem = new JMenuItem("Exit");
            exitMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            fileMenu.add(exitMenuItem);

            this.add(fileMenu);
        }
    }
    private static class MenuPanel extends JPanel{

        public MenuPanel(){
            Box box = Box.createVerticalBox();
            // start new game
            JButton newGameButton = new JButton("New game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("I was clicked!");
                }
            });
            newGameButton.setPreferredSize(new Dimension(100, 30));

            // exit game
            JButton exitGameButton = new JButton("Exit game");
            exitGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            exitGameButton.setPreferredSize(new Dimension(100, 30));

            // add border to space buttons
            this.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));

            // Add button to JPanel
            //this.add(newGameButton);
            //this.add(exitGameButton);

            box.add(newGameButton);
            box.add(exitGameButton);

            this.add(box);
        }
    }
}

