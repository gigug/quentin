package screens;

import objects.Board;
import players.Player;
import players.PlayerWrapper;


public class Game {
    Player blackPlayer = new Player(1);
    Player whitePlayer = new Player(2);

    int turn = 1;

    PlayerWrapper currentPlayer;

    // declare the board
    public Board board;
    private final int size;

    // set ended as false
    Boolean ended = false;

    // Game constructor
    public Game(int size) {
        this.size = size;
        this.board = new Board(this.size);
        currentPlayer = new PlayerWrapper(blackPlayer);
    }

    public int currentPlayer(){return currentPlayer.getSide();}
    
    public void switchPlayer() {
        if (currentPlayer.getSide() == 1){
            currentPlayer = new PlayerWrapper(whitePlayer);
        }
        else{
            currentPlayer = new PlayerWrapper(blackPlayer);
        }
    }

    public boolean checkInGrid(int posX, int posY) {
        return posX >= 0 && posX < this.size && posY >= 0 && posY < this.size;
    }

    public boolean checkEmpty(int posX, int posY) {
        // if in grid, check empty
        if (checkInGrid(posX, posY)){
            return this.board.grid[posX][posY] == 0;
        }

        // else empty
        return true;
    }

    public boolean checkDiagonal(int posX, int posY) {
        // check if any same-colored tiles are diagonally adjacent
        int[][] directions = new int[][]{{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

        int newPosX;
        int newPosY;
        boolean valid = false;
        boolean allDifferent = true;

        // loop over directions
        for (int i = 0; i < 4; i++) {

            // position to check
            newPosX = posX + directions[i][0];
            newPosY = posY + directions[i][1];

            // check orthogonally adjacent positions
            if (getPiece(newPosX, newPosY) == currentPlayer()){
                if (checkInGrid(newPosX, newPosY)) {
                    if (getPiece(newPosX, posY) == currentPlayer() || getPiece(posX, newPosY) == currentPlayer()) {
                        valid = true;
                        break;
                    }
                }
                allDifferent = false;
            }

        }
        // if no diagonally adjacent neighbors share the color, move is leaal
        if (allDifferent){
            return true;
        }

        // if any diagonally adjacent neighbor shares the color, return valid variable
        return valid;
    }

    public void checkTerritory() {
        // loop grid and sign positives
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {

            }
        }
    }

    public boolean checkEligible(int posX, int posY) {

        // initialize counters
        int countBlack = 0;
        int countWhite = 0;

        // initialize directions
        int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

        // loop over directions
        for (int i = 0; i < 4; i++) {

            // position to check
            int newPosX = posX + directions[i][0];
            int newPosY = posY + directions[i][1];

            if (checkInGrid(newPosX, newPosY)){
            //if (newPosX >= 0 && newPosY >= 0 && newPosX < this.board.getSize() && newPosY < this.board.getSize()) {
                if (this.board.grid[newPosX][newPosY] == 1) {
                    countBlack += 1;
                } else if (this.board.grid[newPosX][newPosY] == -1) {
                    countWhite += 1;
                }
            }
        }
        return (countBlack + countWhite) >= 2;
    }

    public int getPiece(int tileIdX, int tileIdY){
        // if in grid return piece
        if (checkInGrid(tileIdX, tileIdY)){
            return this.board.grid[tileIdX][tileIdY];
        }

        // else return empty
        return 0;

    }

    public void addPiece(int tileIdX, int tileIdY){
        this.board.grid[tileIdX][tileIdY] = currentPlayer();
    }

    public void changePiecePieRule(int tileIdX, int tileIdY){this.board.grid[tileIdX][tileIdY] = 2;};

    public int getTurn(){
        return this.turn;
    }

    public void increaseTurn(){
        this.turn += 1;
    }

    public void progress(){
        increaseTurn();
        //checkFill();
        //checkWin();
        switchPlayer();
    }
}