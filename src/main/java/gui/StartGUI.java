package gui;

import objects.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

import static gui.FunctionsGUI.centerWindow;

public class StartGUI extends JFrame{

    public CardLayout cards;
    Container contentPane;

    // board settings
    private final Color fontColor = Color.decode("#E1F2FE");
    private final Color backgroundColor = Color.decode("#35012C");
    private final Color boardColor = Color.decode("#815E5B");
    private final Color lineColor = Color.decode("#000000");
    private final Color externalHorizontalLineColor = Color.decode("#000000");
    private final Color externalVerticalLineColor = Color.decode("#FFFFFF");

    public int size;

    private final static Dimension WINDOW_DIMENSION= new Dimension(700, 700);
    // panel width
    private final static int PANEL_WIDTH = 34;
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(PANEL_WIDTH, PANEL_WIDTH);

    public StartGUI(){

        contentPane = getContentPane();
        StartMenuPanel startMenuPanel = new StartMenuPanel();
        SelectSizePanel selectSizePanel = new SelectSizePanel();
        MenuBar menuBar = new MenuBar();

        cards = new CardLayout();
        contentPane.setLayout(cards);
        contentPane.add("startMenuPanel", startMenuPanel);
        contentPane.add("selectSizePanel", selectSizePanel);

        // title and size
        setTitle("Quentin");
        setSize(WINDOW_DIMENSION);

        // Add table menu bar to screen
        setJMenuBar(menuBar);

        // center window
        centerWindow(this);

        // settings window
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private class StartMenuPanel extends JPanel{
        public StartMenuPanel(){

            setBorder(new EmptyBorder(10, 10, 10, 10));
            GridBagLayout gridBagLayout = new GridBagLayout();
            setLayout(gridBagLayout);

            setBackground(backgroundColor);

            // start new game
            JButton newGameButton = new JButton("New game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.show(contentPane, "selectSizePanel");
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
            gbc.insets = new Insets(5, 0, 5,0);

            JLabel label = new JLabel("Quentin");
            label.setFont(new Font("Serif", Font.BOLD, 20));
            label.setForeground(fontColor);

            add(label, gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            // using same constraints for buttons
            buttons.add(newGameButton, gbc);
            buttons.add(exitGameButton, gbc);
            buttons.setBackground(backgroundColor);
            add(buttons, gbc);
        }
    }

    private class SelectSizePanel extends JPanel{
        public SelectSizePanel(){

            setBorder(new EmptyBorder(10, 10, 10, 10));
            GridBagLayout gridBagLayout = new GridBagLayout();
            setLayout(gridBagLayout);

            setBackground(backgroundColor);

            // start new game
            JButton size7Button = new JButton("7x7");
            size7Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    size = 7;
                    BoardPanel boardPanel = new BoardPanel();
                    contentPane.add("boardPanel", boardPanel);
                    cards.show(contentPane, "boardPanel");
                }
            });

            // exit game
            JButton size9Button = new JButton("9x9");
            size9Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    size = 9;
                    BoardPanel boardPanel = new BoardPanel();
                    contentPane.add("boardPanel", boardPanel);
                    cards.show(contentPane, "boardPanel");
                }
            });

            JButton size11Button = new JButton("11x11");
            size11Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    size = 11;
                    BoardPanel boardPanel = new BoardPanel();
                    contentPane.add("boardPanel", boardPanel);
                    cards.show(contentPane, "boardPanel");
                }
            });

            // Add button to JPanel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(5, 0, 5,0);

            JLabel label = new JLabel("Select board size:");
            label.setFont(new Font("Serif", Font.BOLD, 20));
            label.setForeground(fontColor);

            add(label, gbc);

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            // using same constraints for buttons
            buttons.add(size7Button, gbc);
            buttons.add(size9Button, gbc);
            buttons.add(size11Button, gbc);
            buttons.setBackground(backgroundColor);
            add(buttons, gbc);
        }
    }

    private class BoardPanel extends JPanel{
        // BoardPanel constructor
        BoardPanel(){
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(backgroundColor);

            InternalBoardPanel internalBoardPanel = new InternalBoardPanel();

            // center panel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(5, 0, 5, 0);

            // go back
            JButton newGameButton = new JButton("New Game");
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentPane.remove(2);
                    cards.show(contentPane, "selectSizePanel");
                }
            });

            JLabel label = new JLabel("Game");
            label.setFont(new Font("Serif", Font.BOLD, 20));
            label.setForeground(fontColor);

            add(label, gbc);
            add(internalBoardPanel, gbc);
            add(newGameButton, gbc);
        }
    }

    private class InternalBoardPanel extends JPanel{
        int board_width;
        InternalBoardPanel(){
            final Dimension board_dimension = new Dimension(PANEL_WIDTH * (size + 4), PANEL_WIDTH * (size + 4));
            board_width = PANEL_WIDTH * (size + 4);
            setPreferredSize(board_dimension);
        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);

            g.setColor(boardColor);
            g.fillRect(0,0, board_width, board_width);

            float thicknessBorders = 1;
            float thicknessExternal = 4;
            float thicknessInternal = 2;

            int externalReferencePoint = PANEL_WIDTH/2;
            int internalReferencePoint = 2*PANEL_WIDTH;

            g2.setStroke(new BasicStroke(thicknessBorders));
            // white lines and triangles
            g2.setColor(externalVerticalLineColor);
            g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width, board_width - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width, board_width - externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint, 0}, new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, 4);
            g2.fillPolygon(new int[] {board_width - externalReferencePoint, board_width, board_width, board_width - externalReferencePoint}, new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, 4);

            // black lines and triangles
            g2.setColor(externalHorizontalLineColor);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {board_width, board_width, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {board_width, board_width, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint, 0}, 4);
            g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint, board_width}, 4);

            // external thick square
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(thicknessExternal));
            g2.drawPolygon(new int[] {internalReferencePoint, internalReferencePoint, board_width - internalReferencePoint, board_width - internalReferencePoint}, new int[] {internalReferencePoint, board_width - internalReferencePoint, board_width - internalReferencePoint, internalReferencePoint}, 4);

            // internal lines
            g2.setStroke(new BasicStroke(thicknessInternal));

            // vertical lines
            for (int i = 3; i < size + 2; i++) {
                g2.drawLine(internalReferencePoint, PANEL_WIDTH * i, board_width - internalReferencePoint, PANEL_WIDTH * i);
            }

            // horizontal lines
            for (int i = 3; i < size + 2; i++) {
                g2.drawLine(PANEL_WIDTH * i, internalReferencePoint, PANEL_WIDTH * i, board_width - internalReferencePoint);
            }

        }
    }

    private class MenuBar extends JMenuBar {

        public MenuBar(){
            final JMenu fileMenu = new JMenu("File");

            final JMenuItem newGameMenuItem = new JMenuItem("New Game");
            newGameMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.show(contentPane, "selectSizePanel");
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


}

