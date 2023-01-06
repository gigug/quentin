package screens;

import objects.Board;
import players.Player;
import players.PlayerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Game {
    Player blackPlayer = new Player(1);
    Player whitePlayer = new Player(2);

    int turn = 1;

    PlayerWrapper currentPlayer;

    // declare the board
    public Board board;

    // size is the number of intersections where to place pawns (= number of tiles + 1)
    private final int size;

    List<List<int[]>> neighborsOccupiedList = new ArrayList<>();
    List<List<int[]>> neighborsEmptyList = new ArrayList<>();
    List<List<int[]>> regions = new ArrayList<>();
    List<List<int[]>> territories = new ArrayList<>();
    List<Integer> territoriesColors = new ArrayList<>();

    // Game constructor
    public Game(int size) {
        this.size = size;
        this.board = new Board(this.size);

        // initialize first player
        this.currentPlayer = new PlayerWrapper(blackPlayer);
    }

    public int currentPlayer() {
        return currentPlayer.getSide();
    }

    public void switchPlayer() {
        if (currentPlayer.getSide() == 1) {
            currentPlayer = new PlayerWrapper(whitePlayer);
        } else {
            currentPlayer = new PlayerWrapper(blackPlayer);
        }
    }

    public boolean checkInGrid(int posX, int posY) {
        return posX >= 0 && posX < this.size && posY >= 0 && posY < this.size;
    }

    public boolean checkEmpty(int posX, int posY) {
        // if in grid, check empty
        if (checkInGrid(posX, posY)) {
            return this.board.grid[posX][posY] == 0;
        }

        // else empty
        return true;
    }

    public boolean checkDiagonal(int posX, int posY) {
        // check if any same-colored tiles are diagonally adjacent
        int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

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
            if (getPiece(newPosX, newPosY) == currentPlayer()) {
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
        if (allDifferent) {
            return true;
        }

        // if any diagonally adjacent neighbor shares the color, return valid variable
        return valid;
    }

    public boolean checkPassable() {
        boolean passable = true;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (checkDiagonal(i, j) && checkEmpty(i, j)) {
                    passable = false;
                }
            }
        }

        return passable;
    }

    public void findRegions() {

        boolean[][] visited = new boolean[this.size][this.size];
        this.regions = new ArrayList<>();

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (checkEmpty(i, j) && !visited[i][j]) {
                    // start a new region
                    List<int[]> region = new ArrayList<>();
                    exploreRegion(i, j, visited, region);
                    this.regions.add(region);
                }
            }
        }
    }

    private void exploreRegion(int posX, int posY, boolean[][] visited, List<int[]> region) {
        // base case: out of bounds or not an empty tile
        if (!checkEmpty(posX, posY) || !checkInGrid(posX, posY)){
            return;
        }

        // mark the current tile as visited
        visited[posX][posY] = true;

        // add the current tile to the region
        region.add(new int[] {posX, posY});

        int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

        for (int dir = 0; dir < 4; dir++){

            int newPosX = posX + directions[dir][0];
            int newPosY = posY + directions[dir][1];

            // explore empty regions
            if (checkInGrid(newPosX, newPosY)){
                if (!visited[newPosX][newPosY]){
                    exploreRegion(newPosX, newPosY, visited, region);
                }
            }
        }
    }

    public void validateTerritories(){

        int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

        for(List<int[]> region: this.regions){

            // indicates if region is territory
            boolean valid = true;
            for(int[] element: region){

                int neighbors = 0;
                for(int dir = 0; dir < 4; dir ++) {
                    int newPosX = element[0] + this.size * directions[dir][0];
                    int newPosY = element[1] + this.size * directions[dir][1];

                    if (checkInGrid(newPosX, newPosY) && !checkEmpty(newPosX, newPosY)){
                        neighbors += 1;
                    }
                }
                if (neighbors < 2){
                    valid = false;
                    break;
                }
            }

            // promote region to territory
            if (valid){
                territories.add(region);
            }
        }
    }

    public void findTerritoriesColor(){

        int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

        for(List<int[]> territory: this.territories){

            List<int[]> listOccupiedNeighbors = new ArrayList<>();

            for(int[] element: territory){
                for(int dir = 0; dir < 4; dir ++){
                    int newPosX = element[0] + this.size * directions[dir][0];
                    int newPosY = element[1] + this.size * directions[dir][1];

                    if (!checkEmpty(newPosX, newPosY)){

                        boolean found = false;
                        for(int[] neighbor: listOccupiedNeighbors){
                            if (Objects.equals(neighbor, new int[]{newPosX, newPosY})){
                                found = true;
                                break;
                            }
                        }
                        if (!found){
                            listOccupiedNeighbors.add(new int[] {newPosX, newPosY});
                        }
                    }
                }
            }

            colorTerritory(listOccupiedNeighbors);
        }
    }

    

    public void colorTerritory(List<int[]> listOccupiedNeighbors){

        int blackCounter = 0;
        int whiteCounter = 0;

        for (int[] neighbor: listOccupiedNeighbors){
            if (getPiece(neighbor[0], neighbor[1]) == 1){
                blackCounter += 1;
            }
            else{
                whiteCounter += 1;
            }
        }

        if (blackCounter > whiteCounter){
            territoriesColors.add(1);
        } else if (whiteCounter > blackCounter) {
            territoriesColors.add(2);
        }
        else{
            territoriesColors.add(0);
        }
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
        findRegions();
        validateTerritories();
        findTerritoriesColor();
        printoneeeee();
        //checkFill();
        //checkWin();
        switchPlayer();
    }
}