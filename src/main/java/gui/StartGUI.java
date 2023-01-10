package gui;

import players.PlayerWrapper;
import screens.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import static gui.FunctionsGUI.centerWindow;

public class StartGUI extends JFrame{

    public Game game;
    public CardLayout cards;
    Container contentPane;

    // board settings
    private final Font fontBig = new Font("monospaced", Font.TRUETYPE_FONT, 35);
    private final Font fontSmall = new Font("monospaced", Font.TRUETYPE_FONT, 20);
    private final Color fontColor = Color.decode("#E1F2FE");
    private final Color backgroundColor = Color.decode("#35012C");
    private final Color boardColor = Color.decode("#815E5B");

    public int size;
    public int numberTiles;

    int hoveredRow = -1;
    int hoveredCol = -1;

    private final static Dimension WINDOW_DIMENSION= new Dimension(700, 700);
    private final static int DIAMETER_ICON = 30;
    // panel width
    private final static int PANEL_WIDTH = 30;
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(PANEL_WIDTH, PANEL_WIDTH);

    // panels
    StartMenuPanel startMenuPanel;
    SelectSizePanel selectSizePanel;
    BoardPanel boardPanel;
    InternalBoardPanel internalBoardPanel;
    UnclickablePanel unclickablePanel;
    ClickablePanel clickablePanel;
    MenuBar menuBar;

    public StartGUI(){

        contentPane = getContentPane();
        this.startMenuPanel = new StartMenuPanel();
        this.selectSizePanel = new SelectSizePanel();
        this.menuBar = new MenuBar();

        cards = new CardLayout();
        contentPane.setLayout(cards);
        contentPane.add("startMenuPanel", this.startMenuPanel);
        contentPane.add("selectSizePanel", this.selectSizePanel);

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
            BlackButton newGameButton = new BlackButton("New game");
            newGameButton.setFont(fontSmall);
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cards.show(contentPane, "selectSizePanel");
                }
            });

            // exit game
            BlackButton exitGameButton = new BlackButton("Exit game");
            exitGameButton.setFont(fontSmall);
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
            label.setFont(fontBig);
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

            BlackButton size7Button = new BlackButton("7x7");
            size7Button.setFont(fontSmall);
            size7Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numberTiles = 7;
                    size = numberTiles + 1;
                    initializeGame(size);
                }
            });

            BlackButton size9Button = new BlackButton("9x9");
            size9Button.setFont(fontSmall);
            size9Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numberTiles = 9;
                    size = numberTiles + 1;
                    initializeGame(size);
                }
            });

            BlackButton size11Button = new BlackButton("11x11");
            size11Button.setFont(fontSmall);
            size11Button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    numberTiles = 11;
                    size = numberTiles + 1;
                    initializeGame(size);
                }
            });

            // Add button to JPanel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(5, 0, 5,0);

            JLabel label = new JLabel("Select board size:");
            label.setFont(fontBig);
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
        JPanel turnPanel = new JPanel();
        JLabel circleLabel = new JLabel();
        JLabel turnLabel = new JLabel();

        // BoardPanel constructor
        BoardPanel(){

            turnPanel.setOpaque(false);
            turnLabel.setFont(fontBig);
            turnLabel.setForeground(fontColor);

            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(backgroundColor);

            internalBoardPanel = new InternalBoardPanel();

            // center panel
            GridBagConstraints gbc = new GridBagConstraints();
            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(5, 0, 5, 0);

            // go back
            BlackButton newGameButton = new BlackButton("New Game");
            newGameButton.setFont(fontSmall);
            newGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    contentPane.remove(2);
                    cards.show(contentPane, "selectSizePanel");
                }
            });

            turnPanel.add(turnLabel);
            turnPanel.add(circleLabel);

            setTurnLabel();

            add(turnPanel, gbc);
            add(internalBoardPanel, gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(backgroundColor);
            buttonPanel.add(newGameButton);

            if (game.getTurn() == 1){
                BlackButton pieRuleButton = new BlackButton("Switch sides");
                pieRuleButton.setFont(fontSmall);
                pieRuleButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.changePiecePieRule();
                        createGameCard();
                    }
                });
                buttonPanel.add(pieRuleButton);
            }

            if (game.checkPassable() && !game.finished){
                BlackButton passableButton = new BlackButton("Pass");
                passableButton.setFont(fontSmall);
                passableButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.switchPlayer();
                        createGameCard();
                    }
                });
                buttonPanel.add(passableButton);
            }

            add(buttonPanel, gbc);
        }

        public void redrawInternalBoardPanel(){
            internalBoardPanel = new InternalBoardPanel();
        }

        public void setTurnLabel() {
            String text;
            Color circleColor;
            BufferedImage circleImage;
            ImageIcon circleIcon;

            if (game.finished){
                if (game.getWinner() == 0){
                    text = "Tie!";
                    circleColor = Color.BLUE;
                }
                else{
                    text = (game.getWinner() == 1) ? "Winner: black":"Winner: white";
                    circleColor = (game.getWinner() == 1) ? Color.BLACK:Color.WHITE;
                }

            }
            else{
                text = (game.getCurrentPlayer() == 1) ? "Turn: black":"Turn: white";
                circleColor = (game.getCurrentPlayer() == 1) ? Color.BLACK:Color.WHITE;
            }

            // Create a new image that will hold the white circle
            circleImage = new BufferedImage(DIAMETER_ICON, DIAMETER_ICON, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleImage.createGraphics();

            // Set the color to white and fill the circle with it
            g2.setColor(circleColor);
            g2.fillOval(0, 0, DIAMETER_ICON, DIAMETER_ICON);
            g2.dispose();

            // Create an ImageIcon from the circle image
            circleIcon = new ImageIcon(circleImage);

            circleLabel.setIcon(circleIcon);
            turnLabel.setText(text);
        }
    }

    private class InternalBoardPanel extends JPanel{
        int board_width;
        final Dimension boardDimension;
        InternalBoardPanel(){
            setLayout(new GridBagLayout());

            boardDimension = new Dimension(PANEL_WIDTH * (numberTiles + 4), PANEL_WIDTH * (numberTiles + 4));
            board_width = PANEL_WIDTH * (numberTiles + 4);

            setPreferredSize(boardDimension);

            // center panel
            GridBagConstraints gbc = new GridBagConstraints();

            // order elements vertically
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            // make buttons the same width
            gbc.fill = GridBagConstraints.HORIZONTAL;

            if (!game.finished){
                clickablePanel = new ClickablePanel();
                add(clickablePanel, gbc);
            }

            else{
                unclickablePanel = new UnclickablePanel();
                add(unclickablePanel, gbc);
            }

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
            g2.setColor(Color.WHITE);
            g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, 0, externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width, board_width - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width, board_width - externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint, 0}, new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, 4);
            g2.fillPolygon(new int[] {board_width - externalReferencePoint, board_width, board_width, board_width - externalReferencePoint}, new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, 4);

            // black lines and triangles
            g2.setColor(Color.BLACK);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {0, externalReferencePoint, externalReferencePoint}, new int[] {board_width, board_width, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {0, 0, externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {board_width, board_width, board_width - externalReferencePoint}, 3);
            g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {0, externalReferencePoint, externalReferencePoint, 0}, 4);
            g2.fillPolygon(new int[] {externalReferencePoint, externalReferencePoint, board_width - externalReferencePoint, board_width - externalReferencePoint}, new int[] {board_width, board_width - externalReferencePoint, board_width - externalReferencePoint, board_width}, 4);

            // external thick square
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(thicknessExternal));
            g2.drawPolygon(new int[] {internalReferencePoint, internalReferencePoint, board_width - internalReferencePoint, board_width - internalReferencePoint}, new int[] {internalReferencePoint, board_width - internalReferencePoint, board_width - internalReferencePoint, internalReferencePoint}, 4);

            // internal lines
            g2.setStroke(new BasicStroke(thicknessInternal));

            // vertical lines
            for (int i = 3; i < numberTiles + 2; i++) {
                g2.drawLine(internalReferencePoint, PANEL_WIDTH * i, board_width - internalReferencePoint, PANEL_WIDTH * i);
            }

            // horizontal lines
            for (int i = 3; i < numberTiles + 2; i++) {
                g2.drawLine(PANEL_WIDTH * i, internalReferencePoint, PANEL_WIDTH * i, board_width - internalReferencePoint);
            }


            // pawns
            for(int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (game.getPiece(i, j) == 1) {
                        g2.setColor(Color.BLACK);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        }
                    if (game.getPiece(i, j) == 2) {
                        g2.setColor(Color.WHITE);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        }
                    if (game.getPiece(i, j) == 3) {
                        g2.setColor(Color.BLACK);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        g2.setColor(Color.GREEN);
                        g2.drawOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                    }
                    if (game.getPiece(i, j) == 4) {
                        g2.setColor(Color.WHITE);
                        g2.fillOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                        g2.setColor(Color.GREEN);
                        g2.drawOval(internalReferencePoint + PANEL_WIDTH * i - externalReferencePoint, internalReferencePoint + PANEL_WIDTH * j - externalReferencePoint, PANEL_WIDTH, PANEL_WIDTH);
                    }
                }
            }
        }
    }

    private class UnclickablePanel extends JPanel{
        final Dimension internalBoardDimension;
        UnclickablePanel(){
            //setLayout(new GridLayout(size, size));
            setOpaque(false);

            internalBoardDimension = new Dimension(PANEL_WIDTH * (size), PANEL_WIDTH * (size));

            setPreferredSize(internalBoardDimension);

        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);

            if (hoveredCol > -1) {
                if (game.checkEmpty(hoveredCol, hoveredRow) && game.checkDiagonal(hoveredCol, hoveredRow)){
                    g2.setColor(Color.GREEN);
                }
                else {
                    g2.setColor(Color.RED);
                }
                g2.fillOval(PANEL_WIDTH * hoveredCol, PANEL_WIDTH * hoveredRow, PANEL_WIDTH, PANEL_WIDTH);
            }
        }
    }

    private class ClickablePanel extends UnclickablePanel{
        final Dimension internalBoardDimension;
        ClickablePanel(){
            setLayout(new GridLayout(size, size));
            setOpaque(false);

            internalBoardDimension = new Dimension(PANEL_WIDTH * (size), PANEL_WIDTH * (size));

            setPreferredSize(internalBoardDimension);

            // Create an array to hold the chessboard squares
            JButton[][] squares = new JButton[size][size];

            // Create a button for each square and add it to the panel
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    JButton button = new JButton();
                    squares[row][col] = button;
                    add(button);

                    // Set the button to be transparent
                    button.setOpaque(false);
                    button.setContentAreaFilled(false);
                    button.setBorderPainted(false);

                    // Add an ActionListener to the button to handle clicks
                    int finalRow = row;
                    int finalCol = col;
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            if (game.checkEmpty(finalCol, finalRow) && game.checkDiagonal(finalCol, finalRow)){
                                game.addPiece(finalCol, finalRow);
                                game.progress();
                                boardPanel.setTurnLabel();
                            }
                            if (game.getTurn() == 1 || game.getTurn() == 2 || game.checkPassable() || game.finished){
                                createGameCard();
                            }
                        }
                    });
                    button.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            // Set the hoveredRow and hoveredCol variables when the mouse enters the button
                            hoveredRow = finalRow;
                            hoveredCol = finalCol;
                            repaint();
                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            hoveredRow = -1;
                            hoveredCol = -1;
                            repaint();
                        }
                    });
                }

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

            final JMenuItem saveGameMenuItem = new JMenuItem("Save Game");
            saveGameMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveGame();
                }
            });
            fileMenu.add(saveGameMenuItem);

            final JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
            loadGameMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    // load game
                    loadGame();

                    // create new game card
                    createGameCard();
                }
            });
            fileMenu.add(loadGameMenuItem);

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

    private void initializeGame(int size){
        game = new Game(size);
        createGameCard();
    }

    private void createGameCard(){
        boardPanel = new BoardPanel();
        contentPane.add("boardPanel", boardPanel);
        cards.show(contentPane, "boardPanel");
    }

    public void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game files (*.game)", "game"));
        int result = fileChooser.showSaveDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();
            if (!fileName.endsWith(".game")) {
                fileName += ".game";
            }
            try (FileOutputStream fos = new FileOutputStream(fileName);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(this.game.getSize());
                oos.writeObject(this.game.getGrid());
                oos.writeObject(this.game.getCurrentPlayer());
                oos.writeObject(this.game.getTurn());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game files (*.game)", "game"));
        int result = fileChooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();
            try (FileInputStream fis = new FileInputStream(fileName);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                size = (int) ois.readObject();
                numberTiles = size - 1;

                game = new Game(size);

                game.loadGrid((int[][]) ois.readObject());
                game.loadCurrentPlayer((int) ois.readObject());
                game.loadTurn((int) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

