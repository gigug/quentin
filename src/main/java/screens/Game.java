package screens;

import objects.Board;
import players.Player;
import players.PlayerWrapper;

import java.util.ArrayList;
import java.util.List;


public class Game {
    Player blackPlayer = new Player(1);
    Player whitePlayer = new Player(2);

    int turn = 1;

    PlayerWrapper currentPlayer;

    // declare the board
    public Board board;

    // size is the number of intersections where to place pawns (= number of tiles + 1)
    private final int size;

    // Game constructor
    public Game(int size) {
        this.size = size;
        this.board = new Board(this.size);

        // initialize first player
        this.currentPlayer = new PlayerWrapper(blackPlayer);
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

    public boolean checkPassable(){
        boolean passable = true;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (checkDiagonal(i, j) && checkEmpty(i, j)){
                    passable = false;
                }
            }
        }

        return passable;
    }

    public void checkTerritory() {
        // loop grid and sign positives
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {

            }
        }
    }


    public boolean checkInTerritory(int posX, int posY, boolean starting, boolean[][] visited){

        if (starting){
            visited = new boolean[this.size][this.size];
            for(int i = 0; i < this.size; i++)
                for(int j = 0; j < this.size; j++)
                    visited[i][j] = false;
            starting = false;
        }

        // initialize directions
        int[][] directions = new int[][]{{0, 1}, {1, 0}};

        if (checkInGrid(posX, posY)){
            visited[posX][posY] = true;
        }

        for (int i = 0; i < 2; i++) {
            // position to check
            int newPosX = posX + directions[i][0];
            int newPosY = posY + directions[i][1];

            if (checkInGrid(newPosX, newPosY)){
                if (checkEmpty(newPosX, newPosY)){
                    if (!visited[newPosX][newPosY]){
                        return checkInTerritory(newPosX, newPosY, false, visited);
                    }
                }
            }
            else{
                return true;
            }

        }

        int[][] fullDirections = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

        boolean end = true;

        for (int i = 0; i < 4; i++){
            // position to check
            int newPosX = posX + directions[i][0];
            int newPosY = posY + directions[i][1];

            if (!visited[newPosX][newPosY]  || checkEmpty(newPosX, newPosY)){
                end = false;
            }
        }

        return end;
    }

    public void findTerritories(){

        List<List<int[]>> possibleInTerritory = new ArrayList<>();
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                possibleInTerritory.add(j + this.size * i, checkEligible(i, j));
            }
        }

        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                List<int[]> neighbors = possibleInTerritory.get(i + this.size * j);
                if (neighbors.size() == 0){
                    System.out.print(String.format("[None]"));
                }
                else{
                    System.out.print(String.format("["));
                    for (int[] neighbor : neighbors) {
                        System.out.print(String.format("(%2s,  %2s)", neighbor[0], neighbor[1]));
                    }
                    System.out.print(String.format("]"));
                }
            }
            System.out.println("");
        }

        // function to mark territories
        // ... something recursive

        // function to color territories
        // ... something that checks surroundings



    }


    public List<int[]> checkEligible(int posX, int posY) {

        // instead of returning boolean, you can return list of adjacent colored blocks
        List<int[]> adjacents = new ArrayList<int[]>();

        if (!checkEmpty(posX, posY)){
            return adjacents;
        }

        // initialize directions
        int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

        int counter = 0;
        // loop over directions
        for (int i = 0; i < 4; i++) {

            // position to check
            int newPosX = posX + directions[i][0];
            int newPosY = posY + directions[i][1];

            if (checkInGrid(newPosX, newPosY)){
                if (!checkEmpty(newPosX, newPosY)) {
                    adjacents.add(counter, new int[] {newPosX, newPosY});
                    counter += 1;
                }
            }
        }
        return adjacents;
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

    public void changePiecePieRule(){
        outerloop:
        for(int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                if (getPiece(i,j) == 1) {
                    this.board.grid[i][j] = 2;
                    break outerloop;
                }
            }
        }
        switchPlayer();
    };

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
        findTerritories();
    }
}