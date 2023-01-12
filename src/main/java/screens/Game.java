package screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import objects.Board;
import players.Player;
import players.PlayerWrapper;

/**
 * Game class that implements the board game Quentin
 */
public class Game {
    // Declare players
    private final Player blackPlayer = new Player(1);
    private final Player whitePlayer = new Player(2);

    // Turn number
    private int turn;

    // Current player
    private PlayerWrapper currentPlayer;

    // Winner of the game
    private int winner;
    // If the game is finished or not
    private boolean finished;

    // Declare the board
    private final Board board;

    // Number of intersections where to place pawns (= number of tiles + 1)
    private final int size;

    // Lists to hold the regions, chains, and territories
    private List<List<int[]>> regions = new ArrayList<>();
    private List<List<int[]>> chains = new ArrayList<>();
    private List<List<int[]>> territories = new ArrayList<>();
    private List<Integer> territoryColors = new ArrayList<>();

    // Initialize directions used in the class methods
    int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
    int[][] directionsDiagonal = new int[][]{{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

    // Game constructor
    public Game(int size) {
        // size = number of intersections (per side) where to place pawns
        this.size = size;
        this.board = new Board(this.size);

        // initialize first player
        this.currentPlayer = new PlayerWrapper(blackPlayer);
    }

    /**
     * Set loaded player as current player
     *
     * @param loadedPlayer loaded player from .game file
     */
    public void loadCurrentPlayer(int loadedPlayer){
        this.currentPlayer = (loadedPlayer == 1) ? new PlayerWrapper(blackPlayer) : new PlayerWrapper(whitePlayer);
    }

    /**
     * Get current player's color
     *
     * @return color of current player
     */
    public int getCurrentPlayer() {
        return currentPlayer.getColor();
    }

    /**
     * Get opposite player's color
     *
     * @return color of opposite player
     */
    public int getOppositePlayer() {
        return (getCurrentPlayer() == 1) ? 2:1;
    }

    /**
     * Switch current player with opposite player
     */
    public void switchPlayer() {
        this.currentPlayer = (getCurrentPlayer() == 1) ? new PlayerWrapper(whitePlayer) : new PlayerWrapper(blackPlayer);
    }

    /**
     * Check if coordinates are within the limits of the grid
     *
     * @param X int representing the x position of the piece
     * @param Y int representing the y position of the piece
     * @return true if the coordinates are within the limits of the grid
     */
    public boolean checkInGrid(int X, int Y) {
        return X >= 0 && X < this.size && Y >= 0 && Y < this.size;
    }

    /**
     * Check if grid is empty in specified coordinates.
     * If coordinates are not within the limits of the grid, return true.
     *
     * @param X int representing the x position of the piece
     * @param Y int representing the y position of the piece
     * @return true if grid is empty in specified coordinates
     */
    public boolean checkEmpty(int X, int Y) {
        if (!this.checkInGrid(X, Y)){
            return true;
        }
        return getPiece(X, Y) == 0;
    }

    /**
     * Getter method to retrieve the finished attribute.
     *
     * @return finished boolean
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Check if any same-colored tiles are diagonally adjacent.
     * This method is needed to implement the rule that bans placing tiles
     * when both the following conditions are respected:
     * - the coordinates have a same-colored tile which is diagonally adjacent
     * - there is no same-colored tile orthogonal to both the new coordinates and to the found same-colored tile
     *
     * @return true if the previous rule does not apply, false otherwise
     */
    public boolean checkDiagonal(int X, int Y) {

        int newX;
        int newY;
        boolean valid = false;
        boolean allDifferent = true;

        // loop over diagonal directions
        for (int i = 0; i < 4; i++) {

            // diagonal coordinate to check
            newX = X + this.directionsDiagonal[i][0];
            newY = Y + this.directionsDiagonal[i][1];

            // check if piece is in grid and has the same color as current player
            if (getPiece(newX, newY) == getCurrentPlayer() && checkInGrid(newX, newY)) {
                allDifferent = false;
                // check orthogonally adjacent positions
                if (getPiece(newX, Y) == getCurrentPlayer() || getPiece(X, newY) == getCurrentPlayer()) {
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

    /**
     * Check if turn is passable.
     *
     * @return true if turn is passable
     */
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

    /**
     * Method to find regions and chains on the grid.
     * They are not searched at the same time, the method needs to be called twice
     *
     * @param chains boolean indicating if regions or chains need to be found
     */
    public void findRegions(boolean chains) {

        boolean[][] visited = new boolean[this.size][this.size];

        int color = 0;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (!visited[i][j]){
                    if (chains && !checkEmpty(i, j)){
                        // start a new chain
                        List<int[]> chain = new ArrayList<>();

                        // get color of the piece
                        color = getPiece(i, j);

                        // explore chain
                        exploreRegion(i, j, visited, chain, chains, color);

                        // add to list of chains
                        this.addChain(chain);
                    }
                    else if(!chains && checkEmpty(i, j)){
                        // start a new region
                        List<int[]> region = new ArrayList<>();

                        // color is not important
                        exploreRegion(i, j, visited, region, chains, color);

                        // add to region
                        this.addRegion(region);
                    }
                }
            }
        }
    }

    /**
     * Support method for the findRegions method
     *
     * @param X int representing the x position of the piece
     * @param Y int representing the y position of the piece
     * @param visited array indicating which coordinates have been visited
     * @param region list of coordinates belonging to that region
     * @param chains boolean indicating whether to find chains or regions
     * @param color int indicating color of the analyzed coordinate
     */
    private void exploreRegion(int X, int Y, boolean[][] visited, List<int[]> region, boolean chains, int color) {

        // if: searching for chains and found wrong color or searching for regions and not empty, return
        if ((chains && getPiece(X, Y) != color) || (!chains && !checkEmpty(X, Y))){
            return;
        }

        // mark the current tile as visited
        visited[X][Y] = true;

        // add the current tile to the region
        region.add(new int[] {X, Y});

        // new coordinates
        int newX, newY;

        for (int dir = 0; dir < 4; dir++){

            newX = X + this.directions[dir][0];
            newY = Y + this.directions[dir][1];

            // explore coordinates not yet visited
            if (checkInGrid(newX, newY)){
                if (!visited[newX][newY]){
                    exploreRegion(newX, newY, visited, region, chains, color);
                }
            }
        }
    }

    /**
     * Method to empty regions, chains, territories and their colors when updating grid
     */
    public void emptyRegions(){
        this.regions = new ArrayList<>();
        this.chains = new ArrayList<>();
        this.territories = new ArrayList<>();
        this.territoryColors = new ArrayList<>();
    }

    /**
     * Getter method to retrieve the list of regions
     *
     * @return regions List<List<int[]>>
     */
    public List<List<int[]>> getRegions(){
        return this.regions;
    }

    /**
     * Getter method to retrieve the list of chains
     *
     * @return chains List<List<int[]>>
     */
    public List<List<int[]>> getChains(){
        return this.chains;
    }

    /**
     * Getter method to retrieve the list of territories
     *
     * @return territories List<List<int[]>>
     */
    public List<List<int[]>> getTerritories(){
        return this.territories;
    }

    /**
     * Getter method to retrieve the list of territories colors
     *
     * @return territoriesColors List<Integer>
     */
    public List<Integer> getTerritoryColors(){
        return this.territoryColors;
    }

    /**
     * Getter method to retrieve the grid
     *
     * @return grid int[][]
     */
    public int[][] getGrid(){return this.board.getGrid();}

    /**
     * Setter method to add a region to the list of regions
     *
     * @param region List<int[]> indicating the region to add
     */
    public void addRegion(List<int[]> region){
        this.regions.add(region);
    }

    /**
     * Setter method to add a chain to the list of chains
     *
     * @param chain List<int[]> indicating the region to add
     */
    public void addChain(List<int[]> chain){
        this.chains.add(chain);
    }

    /**
     * Setter method to add a territory to the list of territories
     *
     * @param territory List<int[]> indicating the region to add
     */
    public void addTerritory(List<int[]> territory){
        this.territories.add(territory);
    }

    /**
     * Setter method to add a territory color to the list of territory colors
     *
     * @param territoryColor int indicating the color to add
     */
    public void addTerritoryColor(int territoryColor){
        this.territoryColors.add(territoryColor);
    }

    /**
     * Method to transform eligible regions into territories
     */
    public void validateTerritories(){

        // iterate over regions
        for(List<int[]> region: this.getRegions()){

            // indicates if region is territory
            boolean valid = true;
            int newX, newY, neighbors;

            // iterate over elements
            for(int[] element: region){
                neighbors = 0;

                for(int dir = 0; dir < 4; dir ++) {
                    newX = element[0] + this.directions[dir][0];
                    newY = element[1] + this.directions[dir][1];

                    if (checkInGrid(newX, newY) && !checkEmpty(newX, newY)){
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
                this.addTerritory(region);
            }
        }
    }

    /**
     * Method to assign a color to a territory.
     */
    public void findTerritoryColors(){

        int newX, newY;
        boolean found;

        // iterate over territories
        for(List<int[]> territory: this.getTerritories()){
            List<int[]> listOccupiedNeighbors = new ArrayList<>();

            // iterate over elements of territory to find every neighbor
            for(int[] element: territory){
                for(int dir = 0; dir < 4; dir ++){
                    newX = element[0] + this.directions[dir][0];
                    newY = element[1] + this.directions[dir][1];

                    if (checkInGrid(newX, newY) && !checkEmpty(newX, newY)){
                        found = false;

                        // check if coordinate was already included
                        for(int[] neighbor: listOccupiedNeighbors){
                            if (Arrays.equals(neighbor, new int[]{newX, newY})){
                                found = true;
                                break;
                            }
                        }
                        if (!found){
                            listOccupiedNeighbors.add(new int[] {newX, newY});
                        }
                    }
                }
            }

            countNeighbors(listOccupiedNeighbors);
        }
    }

    /**
     * Count neighbors and assign a color accordingly
     *
     * @param listOccupiedNeighbors list of neighbors
     */
    public void countNeighbors(List<int[]> listOccupiedNeighbors){

        // initialize counters
        int blackCounter = 0;
        int whiteCounter = 0;

        // iterate over neighbors
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
            this.addTerritoryColor(1);
        }
        else if (whiteCounter > blackCounter) {
            this.addTerritoryColor(2);
        }
        else{
            this.addTerritoryColor(0);
        }
    }

    /**
     * Method to add pieces to complete a territory according to its color
     */
    public void colorTerritories(){
        int color;

        // iterate over territories
        for(int i = 0; i < this.getTerritories().size(); i++){
            color = this.getTerritoryColors().get(i);

            // iterate over elements of territory
            for(int[] element: this.getTerritories().get(i)){

                if (color == 0){
                    this.addPiece(element[0], element[1], getOppositePlayer());
                }
                else{
                    this.addPiece(element[0], element[1], color);
                }
            }

        }
    }

    /**
     * Getter method to retrieve a piece from coordinates
     *
     * @param X int representing the x position of the piece
     * @param Y int representing the y position of the piece
     * @return int representing the color of the piece, 0 if empty
     */
    public int getPiece(int X, int Y){
        // if in grid return piece
        if (checkInGrid(X, Y)){
            return this.board.getPiece(X, Y);
        }

        // else return empty
        return 0;

    }

    /**
     * Setter method to add a piece at the specified position
     *
     * @param X int representing the x position of the piece
     * @param Y int representing the y position of the piece
     * @param color representing the color of the piece
     */
    public void addPiece(int X, int Y, int color){
        this.board.addPiece(X, Y, color);
    }

    /**
     * Method to invert the single initial black piece if the pie rule is invoked
     */
    public void changePiecePieRule(){
        outerloop:
        for(int i = 0; i < this.size ; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.getPiece(i,j) == 1) {
                    this.addPiece(i, j, 2);
                    break outerloop;
                }
            }
        }
    };

    /**
     * Method to check if the game has been won
     */
    public void checkWin(){
        // find chains
        findRegions(true);

        // check chains
        checkChains();
    }

    /**
     * Method to check if the game has been tied
     */
    public void checkTie() {
        boolean isFull = true;

        // simply checks if there is any empty spot
        if (!this.isFinished()){
            outerloop:
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    if (getPiece(i, j) == 0) {
                        isFull = false;
                        break outerloop;
                    }
                }
            }
            if (isFull && !isFinished()) {
                this.setFinished(true);
                this.setWinner(0);
            }
        }
    }

    /**
     * Setter method for finished
     *
     * @param finished boolean indicating whether the game is finished
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Setter method for winner
     *
     * @param winner int indicating the winner of the game
     */
    public void setWinner(int winner) {
        this.winner = winner;
    }

    /**
     * Method to check if any chain reaches from one side of the grid to the other
     */
    public void checkChains(){

        int color = 0;
        int value;
        // booleans indicating whether a chain touches any side of the grid
        boolean N, E, S, W;

        // iterate over chains
        for(List<int[]> chain: chains){
            N = E = S = W = false;

            // iterate over elements of chain
            for(int[] element: chain){
                color = this.getPiece(element[0], element[1]);
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

            // if chain touches opposite sides, assign winner and finish game
            if ((N && S) || (E && W)){
                this.setWinner(color);
                this.setFinished(true);
            }

            if(this.isFinished()){
                // pawns belonging to the winning chains are colored differently to highlight them graphically
                value = (color == 1) ? 3:4 ;
                for(int[] element: chain){
                    this.addPiece(element[0], element[1], value);
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
        findTerritoryColors();
        colorTerritories();
    }

    public void setGrid(int[][] newGrid) {
        this.board.setGrid(newGrid);
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

    public int getSize() {
        return this.size;
    }

    public void setTurn(int loadedTurn) {
        this.turn = loadedTurn;
    }

}