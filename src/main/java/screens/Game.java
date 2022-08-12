package screens;

import players.Player;
import objects.Board;

public class Game {
    // initialize the two players
    Player player1 = new Player(1);
    Player player2 = new Player(2);

    // declare the board
    public Board board;

    // set finished as false
    Boolean finished = false;

    // Game constructor
    public Game(int size){
        this.board = new Board(size);
    }

    // play function
    public void play(){
        while (!this.finished) {
            this.switchPlayer();
        }
    }

    // switch player function
    public void switchPlayer(){
        System.out.println("Hello");
        this.finished = true;
    }
}
