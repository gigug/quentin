import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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
        testGame.addPiece(0, 0);

        // play with white
        testGame.switchPlayer();

        // add white pawns
        testGame.addPiece(1, 0);
        testGame.addPiece(0, 1);

        // play with black
        testGame.switchPlayer();

        assertTrue(testGame.checkPassable());
    }

    @ParameterizedTest
    @CsvSource({"0,0","0,2","2,2","2,0"})
    public void testCheckDiagonalFalse(int posX, int posY){
        // the following configuration of pawns has to return false for checkDiagonal
        int testSize = 3;
        Game testGame = new Game(testSize);

        // add black pawn
        testGame.addPiece(1, 1);

        assertFalse(testGame.checkDiagonal(posX, posY));
    }

    @ParameterizedTest
    @CsvSource({"0,0","1,2","2,1"})
    public void testCheckDiagonalTrue(int posX, int posY){
        // the following configuration of pawns has to return false for checkDiagonal
        int testSize = 3;
        Game testGame = new Game(testSize);

        // add black pawn
        testGame.addPiece(1, 1);
        testGame.addPiece(1, 0);

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
        testGame.addPiece(1,1);

        // false
        assertFalse(testGame.checkEmpty(1,1));
    }

    @Test
    public void testCheckPassable(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // add pieces
        testGame.addPiece(0,0);
        testGame.addPiece(0,1);
        testGame.addPiece(1,0);
        testGame.addPiece(1,1);

        // true
        assertTrue(testGame.checkPassable());
    }

    @Test
    public void testFindRegions(){
        int testSize = 5;
        Game testGame = new Game(testSize);

        // add black pieces
        testGame.addPiece(2,1);
        testGame.addPiece(1,2);
        testGame.addPiece(3,2);
        testGame.addPiece(2,3);

        // region
        List<List<int[]>> regions = new ArrayList<>();
        List<int[]> region = new ArrayList<int[]>();
        int[] regionElement = {1,1};

        region.add(regionElement);
        regions.add(region);

        // true
        assertEquals(regions, testGame.getRegions());

        // check that the region is a territory
        testGame.validateTerritories();
        assertEquals(regions, testGame.getTerritories());

        // check that the color is correct
        List<Integer> territoriesColor = new ArrayList<>();
        int color = 0;
        territoriesColor.add(color);

        testGame.findTerritoriesColor();
        assertEquals(territoriesColor, testGame.getTerritoriesColors());
    }

    @Test
    public void testFindRegionsChain(){
        int testSize = 5;
        Game testGame = new Game(testSize);

        // add black pieces
        testGame.addPiece(0, 0);
        testGame.addPiece(0,1);
        testGame.addPiece(0,2);
        testGame.addPiece(0,3);
        testGame.addPiece(0,4);

        // region
        List<List<int[]>> chains = new ArrayList<>();
        List<int[]> chain = new ArrayList<int[]>();
        int[] chainElement1 = {0,0};
        int[] chainElement2 = {0,1};
        int[] chainElement3 = {0,2};
        int[] chainElement4 = {0,3};
        int[] chainElement5 = {0,4};

        chain.add(chainElement1);
        chain.add(chainElement2);
        chain.add(chainElement3);
        chain.add(chainElement4);
        chain.add(chainElement5);

        chains.add(chain);

        // true
        assertEquals(chains, testGame.getChains());
    }

    @Test
    public void testChangePiecePieRule(){
        int testSize = 2;
        Game testGame = new Game(testSize);
        int[][] testGrid = {{2,0}, {0,0}};

        // add black piece
        testGame.addPiece(0,0);

        // call pie rule
        testGame.changePiecePieRule();

        // true
        assertArrayEquals(testGame.getGrid(), testGrid);
    }

    @Test
    public void testCheckWin(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addPiece(0,0);
        testGame.addPiece(0,1);

        // check win
        testGame.checkWin();

        assertTrue(testGame.finished);
        assertEquals(testGame.getWinner(), 1);
    }

    @Test
    public void testNotCheckWin(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addPiece(0,0);

        testGame.switchPlayer();
        testGame.addPiece(0,1);

        // check win
        testGame.checkWin();

        assertFalse(testGame.finished);
    }

    @Test
    public void testCheckTie(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addPiece(0,0);
        testGame.addPiece(1,1);

        testGame.switchPlayer();
        testGame.addPiece(0,1);
        testGame.addPiece(1,0);

        // check tie
        testGame.checkTie();

        assertTrue(testGame.finished);
        assertEquals(testGame.getWinner(), 0);
    }
}
