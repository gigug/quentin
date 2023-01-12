import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import screens.Game;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testGame {

    @Test
    public void testPassable(){
        // the following configuration of pawns has to be passable
        int testSize = 2;
        Game testGame = new Game(testSize);

        // add black pawn
        testGame.addPiece(0, 0, 0);

        // add white pawns
        testGame.addPiece(1, 0, 1);
        testGame.addPiece(0, 1, 1);

        assertTrue(testGame.checkPassable());
    }

    @ParameterizedTest
    @CsvSource({"0,0","0,2","2,2","2,0"})
    public void testCheckDiagonalFalse(int posX, int posY){
        // the following configuration of pawns has to return false for checkDiagonal
        int testSize = 3;
        Game testGame = new Game(testSize);

        // add black pawn
        testGame.addPiece(1, 1, 0);

        assertFalse(testGame.checkDiagonal(posX, posY));
    }

    @ParameterizedTest
    @CsvSource({"0,0","1,2","2,1"})
    public void testCheckDiagonalTrue(int posX, int posY){
        // the following configuration of pawns has to return false for checkDiagonal
        int testSize = 3;
        Game testGame = new Game(testSize);

        // add black pawns
        testGame.addPiece(1, 1, 0);
        testGame.addPiece(1, 0, 0);

        assertTrue(testGame.checkDiagonal(posX, posY));
    }

    @Test
    public void testGetCurrentPlayer(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // check black
        assertEquals(testGame.getCurrentPlayer(), 1);

        // switch to white
        testGame.switchPlayer();
        assertEquals(testGame.getCurrentPlayer(), 2);
    }

    @Test
    public void testGetOppositePlayer(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // check white
        assertEquals(testGame.getOppositePlayer(), 2);

        // switch to white
        // check black
        testGame.switchPlayer();
        assertEquals(testGame.getOppositePlayer(), 1);
    }

    @Test
    public void testCheckInGrid(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // true
        assertTrue(testGame.checkInGrid(0,0));

        // false
        assertFalse(testGame.checkInGrid(2,2));
    }

    @Test
    public void testCheckEmpty(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // true
        assertTrue(testGame.checkEmpty(0,0));

        // add piece
        testGame.addPiece(1,1, 0);

        // false
        assertFalse(testGame.checkEmpty(1,1));
    }

    @Test
    public void testCheckPassable(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // add pieces
        testGame.addPiece(0,0, 0);
        testGame.addPiece(0,1, 0);
        testGame.addPiece(1,0, 0);
        testGame.addPiece(1,1, 0);

        // true
        assertTrue(testGame.checkPassable());
    }

    @Test
    public void testFindRegions(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // add black pieces
        testGame.addPiece(0,0, 0);
        testGame.addPiece(0,1, 0);
        testGame.addPiece(1,0, 0);

        testGame.findRegions(false);

        // region
        int[] regionElement = {1, 1};

        List<List<int[]>> regionsPredicted = testGame.getRegions();
        List<int[]> regionPredicted = regionsPredicted.get(0);
        int[] regionElementPredicted = regionPredicted.get(0);

        // true
        assertArrayEquals(regionElement, regionElementPredicted);
    }

    @Test
    public void testFindTerritories(){

        int testSize = 5;
        Game testGame = new Game(testSize);

        testGame.addPiece(1, 2, 0);
        testGame.addPiece(2, 1, 0);
        testGame.addPiece(3, 2, 0);
        testGame.addPiece(2, 3, 0);

        // region
        int[] territoryElement = {2,2};

        // check that the region is a territory
        testGame.findRegions(false);
        testGame.checkTerritories();

        List<int[]> territoryPredicted = testGame.getTerritories().get(0);
        int[] territoryElementPredicted = territoryPredicted.get(0);

        assertArrayEquals(territoryElementPredicted, territoryElement);

        // check that the color is correct
        int color = 1;

        testGame.findTerritoryColors();

        int territoryColorPredicted = testGame.getTerritoryColors().get(0);

        assertEquals(color, territoryColorPredicted);
    }

    @Test
    public void testFindRegionsChain(){
        int testSize = 5;
        Game testGame = new Game(testSize);

        // add black pieces
        testGame.addPiece(0,0, 0);
        testGame.addPiece(0,1, 0);
        testGame.addPiece(0,2, 0);
        testGame.addPiece(0,3, 0);
        testGame.addPiece(0,4, 0);

        // region
        List<int[]> chain = new ArrayList<int[]>();
        int[] chainElement0 = {0,0};
        int[] chainElement1 = {0,1};
        int[] chainElement2 = {0,2};
        int[] chainElement3 = {0,3};
        int[] chainElement4 = {0,4};

        chain.add(chainElement0);
        chain.add(chainElement1);
        chain.add(chainElement2);
        chain.add(chainElement3);
        chain.add(chainElement4);

        testGame.findRegions(true);

        List<int[]> chainPredicted = testGame.getChains().get(0);

        // true
        assertArrayEquals(chainPredicted.get(0), chainElement0);
        assertArrayEquals(chainPredicted.get(1), chainElement1);
        assertArrayEquals(chainPredicted.get(2), chainElement2);
        assertArrayEquals(chainPredicted.get(3), chainElement3);
        assertArrayEquals(chainPredicted.get(4), chainElement4);
    }

    @Test
    public void testChangePiecePieRule(){
        int testSize = 2;
        Game testGame = new Game(testSize);
        int[][] testGrid = {{2,0}, {0,0}};

        // add black piece
        testGame.addPiece(0,0, 0);

        // call pie rule
        testGame.changePiecePieRule();

        // true
        assertArrayEquals(testGame.getGrid(), testGrid);
    }

    @Test
    public void testCheckWin(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addPiece(0,0, 0);
        testGame.addPiece(0,1, 0);

        // check win
        testGame.checkWin();

        assertTrue(testGame.isFinished());
        assertEquals(testGame.getWinner(), 1);
    }

    @Test
    public void testCheckWinFalse(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addPiece(0,0, 0);
        testGame.addPiece(0,1, 1);

        // check win
        testGame.checkWin();

        assertFalse(testGame.isFinished());
    }

    @Test
    public void testCheckTie(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addPiece(0,0, 0);
        testGame.addPiece(1,1, 0);

        testGame.addPiece(0,1, 1);
        testGame.addPiece(1,0, 1);

        // check tie
        testGame.checkTie();

        assertTrue(testGame.isFinished());
        assertEquals(testGame.getWinner(), 0);
    }
}
