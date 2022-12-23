package screens;

import players.Player;
import objects.Board;

import java.awt.*;

public class Game {

    // initialize the two players
    Player player1 = new Player(1);
    Player player2 = new Player(2);
    int currentPlayer;

    // declare the board
    public Board board;
    private int size;

    // set ended as false
    Boolean ended = false;

    // Game constructor
    public Game() {
        this.size = 10;
        this.board = new Board(this.size);
    }

    // play function
    public void play() {
        chooseStartingPlayer();
        while (!this.ended) {
            this.switchPlayer();
        }
    }

    // initialize first player
    public void chooseStartingPlayer(){

        //this.currentPlayer = player;
        System.out.println("Initialized player:");
        //System.out.println(this.currentPlayer);
    }

    // switch player function
    public void switchPlayer() {
        this.ended = true;
    }


    public boolean checkInGrid(int posX, int posY) {
        return posX >= 0 && posX < this.size && posY >= 0 && posY < this.size;
    }

    public boolean checkEmpty(int posX, int posY) {
        return this.board.grid[posX][posY] == 0;
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
            int newPosY = posY + directions[i][0];

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


}