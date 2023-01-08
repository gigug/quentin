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

    int turn = 0;

    PlayerWrapper currentPlayer;
    int winner;
    public boolean finished;

    // declare the board
    public Board board;

    // size is the number of intersections where to place pawns (= number of tiles + 1)
    private final int size;
    List<List<int[]>> regions = new ArrayList<>();
    List<List<int[]>> chains = new ArrayList<>();
    List<List<int[]>> territories = new ArrayList<>();
    List<Integer> territoriesColors = new ArrayList<>();

    int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
    int[][] directionsDiagonal = new int[][]{{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

    // Game constructor
    public Game(int size) {
        this.size = size;
        this.board = new Board(this.size);

        // initialize first player
        this.currentPlayer = new PlayerWrapper(blackPlayer);
    }

    public int getCurrentPlayer() {
        return currentPlayer.getSide();
    }

    public int getOppositePlayer() {
        return (getCurrentPlayer() == 1) ? 2:1;
    }

    public void switchPlayer() {
        this.currentPlayer = (getCurrentPlayer() == 1) ? new PlayerWrapper(whitePlayer) : new PlayerWrapper(blackPlayer);
    }

    public boolean checkInGrid(int posX, int posY) {
        return posX >= 0 && posX < this.size && posY >= 0 && posY < this.size;
    }

    public boolean checkEmpty(int posX, int posY) {
        // if in grid, check empty
        if (checkInGrid(posX, posY)) {
            return getPiece(posX, posY) == 0;
        }

        // else empty
        return true;
    }

    public boolean checkDiagonal(int posX, int posY) {
        // check if any same-colored tiles are diagonally adjacent

        int newPosX;
        int newPosY;
        boolean valid = false;
        boolean allDifferent = true;

        // loop over directions
        for (int i = 0; i < 4; i++) {

            // position to check
            newPosX = posX + this.directionsDiagonal[i][0];
            newPosY = posY + this.directionsDiagonal[i][1];

            // check orthogonally adjacent positions
            if (getPiece(newPosX, newPosY) == getCurrentPlayer() && checkInGrid(newPosX, newPosY)) {
                allDifferent = false;
                if (getPiece(newPosX, posY) == getCurrentPlayer() || getPiece(posX, newPosY) == getCurrentPlayer()) {
                    valid = true;
                    break;
                }
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

        outerloop:
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (checkDiagonal(i, j) && checkEmpty(i, j)) {
                    passable = false;
                    break outerloop;
                }
            }
        }

        return passable;
    }

    public void findRegions(boolean chains) {

        boolean[][] visited = new boolean[this.size][this.size];

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (!chains){
                    if (checkEmpty(i, j) && !visited[i][j]) {
                        // start a new region
                        List<int[]> region = new ArrayList<>();
                        int color = getPiece(i, j);
                        exploreRegion(i, j, visited, region, chains, color);
                        this.addRegion(region);
                    }
                }
                else{
                    if (!checkEmpty(i, j) && !visited[i][j]) {
                        // start a new region
                        List<int[]> region = new ArrayList<>();
                        int color = getPiece(i, j);
                        exploreRegion(i, j, visited, region, chains, color);
                        this.addChain(region);
                    }
                }
            }
        }
    }

    private void exploreRegion(int posX, int posY, boolean[][] visited, List<int[]> region, boolean chains, int color) {

        if (!chains){
            // base case: not an empty tile or out of bounds
            if (!checkEmpty(posX, posY) || !checkInGrid(posX, posY)){
                return;
            }
        }
        else{
            // if wrong color or out of bounds
            if (getPiece(posX, posY) != color || !checkInGrid(posX, posY)){
                return;
            }
        }

        // mark the current tile as visited
        visited[posX][posY] = true;

        // add the current tile to the region
        region.add(new int[] {posX, posY});

        for (int dir = 0; dir < 4; dir++){

            int newPosX = posX + this.directions[dir][0];
            int newPosY = posY + this.directions[dir][1];

            // explore empty regions
            if (checkInGrid(newPosX, newPosY)){
                if (!visited[newPosX][newPosY]){
                    exploreRegion(newPosX, newPosY, visited, region, chains, color);
                }
            }
        }
    }

    public void emptyRegions(){
        this.regions = new ArrayList<>();
        this.chains = new ArrayList<>();
        this.territories = new ArrayList<>();
        this.territoriesColors = new ArrayList<>();
    }

    public List<List<int[]>> getRegions(){
        return this.regions;
    }
    public List<List<int[]>> getChains(){
        return this.chains;
    }
    public List<List<int[]>> getTerritories(){
        return this.territories;
    }
    public List<Integer> getTerritoriesColors(){
        return this.territoriesColors;
    }
    public int[][] getGrid(){return this.board.grid;}

    public void addRegion(List<int[]> region){
        this.regions.add(region);
    }

    public void addChain(List<int[]> chain){
        this.chains.add(chain);
    }

    public void validateTerritories(){

        for(List<int[]> region: this.getRegions()){

            // indicates if region is territory
            boolean valid = true;

            // iterate over elements
            for(int[] element: region){

                int neighbors = 0;
                for(int dir = 0; dir < 4; dir ++) {
                    int newPosX = element[0] + this.directions[dir][0];
                    int newPosY = element[1] + this.directions[dir][1];

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

        for(List<int[]> territory: this.territories){

            List<int[]> listOccupiedNeighbors = new ArrayList<>();

            for(int[] element: territory){
                for(int dir = 0; dir < 4; dir ++){
                    int newPosX = element[0] + this.directions[dir][0];
                    int newPosY = element[1] + this.directions[dir][1];

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

            countNeighbors(listOccupiedNeighbors);
        }
    }

    public void countNeighbors(List<int[]> listOccupiedNeighbors){

        // initialize counters
        int blackCounter = 0;
        int whiteCounter = 0;

        // loop over neighbors
        for (int[] neighbor: listOccupiedNeighbors){
            if (getPiece(neighbor[0], neighbor[1]) == 1){
                blackCounter += 1;
            }
            else{
                whiteCounter += 1;
            }
        }

        // assign:
        // 0: equal
        // 1: black
        // 2: white

        if (blackCounter > whiteCounter){
            this.territoriesColors.add(1);
        } else if (whiteCounter > blackCounter) {
            this.territoriesColors.add(2);
        }
        else{
            this.territoriesColors.add(0);
        }
    }

    public void colorTerritories(){

        for(int i = 0; i < this.territories.size(); i++){

            int color = territoriesColors.get(i);
            for(int[] element: this.territories.get(i)){

                if (color == 0){
                    this.board.grid[element[0]][element[1]] = getOppositePlayer();
                }
                else{
                    this.board.grid[element[0]][element[1]] = color;
                }
            }

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
        if (this.checkEmpty(tileIdX, tileIdY)){
            this.board.grid[tileIdX][tileIdY] = getCurrentPlayer();
        }
    }

    public void changePiecePieRule(){
        outerloop:
        for(int i = 0; i < this.size ; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.getPiece(i,j) == 1) {
                    // bypassing the addPiece function to avoid adding an additional term
                    this.board.grid[i][j] = 2;
                    break outerloop;
                }
            }
        }
        switchPlayer();
    };

    public void checkWin(){
        // find chains
        findRegions(true);

        // check chains
        checkChains();

    }

    public void checkTie() {
        boolean isFull = true;

        if (!this.finished){
            outerloop:
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    if (getPiece(i, j) == 0) {
                        isFull = false;
                        break outerloop;
                    }
                }
            }

            if (isFull) {
                this.finished = true;
                this.winner = 0;
            }
        }


    }

    public void checkChains(){

        int color = 0;

        int counter = 0;
        for(List<int[]> chain: chains){

            counter += 1;
            boolean N, E, S, W;
            N = E = S = W = false;
            for(int[] element: chain){
                color = getPiece(element[0], element[1]);
                if (element[0] == 0){
                    N = true;
                }
                if (element[0] == this.size - 1){
                    S = true;
                }
                if (element[1] == 0){
                    E = true;
                }
                if (element[1] == this.size - 1){
                    W = true;
                }
            }

            if ((N && S) || (E && W)){
                winner = color;
                finished = true;
            }

            if(finished){
                int value = (color == 1) ? 3:4 ;
                for(int[] element: chain){
                    this.board.grid[element[0]][element[1]] = value;
                }
                break;
            }
        }
    }

    public int getWinner(){
        return this.winner;
    }
    public int getTurn(){
        return this.turn;
    }

    public void increaseTurn(){
        this.turn += 1;
    }

    public void checkTerritories(){
        validateTerritories();
        findTerritoriesColor();
        colorTerritories();
    }

    public void progress(){
        increaseTurn();
        findRegions(false);
        checkTerritories();
        checkWin();
        checkTie();
        emptyRegions();
        switchPlayer();
    }
}