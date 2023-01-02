package objects;

import java.util.Arrays;

public class Board {

    public int size;

    public int[][] grid;

    public Board(int size){
        // size indicates the number of squares per axis
        // size + 1 indicates the number of intersections where to place pawns
        this.size = size + 1;
        this.grid = new int[this.size][this.size];
        this.initializeGrid();
    }

    public void initializeGrid(){
        for(int i = 0; i < this.size; i++)
            for(int j = 0; j < this.size; j++)
                this.grid[i][j] = 0;
    }

    public void viewGrid(){
        for(int i=0; i<this.size; i++){
            for(int j=0; j<this.size; j++){
                System.out.print(String.format("%2s", this.grid[i][j]));
            }
            System.out.println("");
        }
    }

    public int getPiece(int tileIdX, int tileIdY){
        return this.grid[tileIdX][tileIdY];
    }

    public int getSize() {
        return this.size;
    }
}
