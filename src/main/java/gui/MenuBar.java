package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {

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

