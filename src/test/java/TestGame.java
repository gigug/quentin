import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import players.PlayerColor;
import screens.Game;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGame {

    @Test
    void testPassable(){
        // the following configuration of stones has to be passable
        int testSize = 2;
        Game testGame = new Game(testSize);

        // add black stone
        testGame.addStone(0, 0, 1);

        // add white stones
        testGame.addStone(1, 0, 2);
        testGame.addStone(0, 1, 2);

        assertTrue(testGame.checkPassable());
    }

    @ParameterizedTest
    @CsvSource({"0,0","0,2","2,2","2,0"})
    void testCheckDiagonalFalse(int posX, int posY){
        // the following configuration of stones has to return false for checkDiagonal
        int testSize = 3;
        Game testGame = new Game(testSize);

        // add black stone
        testGame.addStone(1, 1, 1);

        assertFalse(testGame.checkDiagonal(posX, posY));
    }

    @ParameterizedTest
    @CsvSource({"0,0","1,2","2,1"})
    void testCheckDiagonalTrue(int posX, int posY){
        // the following configuration of stones has to return false for checkDiagonal
        int testSize = 3;
        Game testGame = new Game(testSize);

        // add black stones
        testGame.addStone(1, 1, 1);
        testGame.addStone(1, 0, 1);

        assertTrue(testGame.checkDiagonal(posX, posY));
    }

    @Test
    void testGetCurrentPlayer(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // check black
        assertEquals(PlayerColor.BLACK, testGame.getCurrentPlayer());

        // switch to white
        testGame.switchPlayer();
        assertEquals(PlayerColor.WHITE, testGame.getCurrentPlayer());
    }

    @Test
    void testGetOppositePlayer(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // check white
        assertEquals(2, testGame.getOppositePlayer());

        // switch to white
        // check black
        testGame.switchPlayer();
        assertEquals(1, testGame.getOppositePlayer());
    }

    @Test
    void testCheckInGrid(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // true
        assertTrue(testGame.checkInGrid(0,0));

        // false
        assertFalse(testGame.checkInGrid(2,2));
    }

    @Test
    void testCheckEmpty(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // true
        assertTrue(testGame.checkEmpty(0,0));

        // add stone
        testGame.addStone(1,1, 1);

        // false
        assertFalse(testGame.checkEmpty(1,1));
    }

    @Test
    void testCheckPassable(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // add stones
        testGame.addStone(0,0, 1);
        testGame.addStone(0,1, 1);
        testGame.addStone(1,0, 1);
        testGame.addStone(1,1, 1);

        // true
        assertTrue(testGame.checkPassable());
    }

    @Test
    void testFindRegions(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        // add black stones
        testGame.addStone(0,0, 1);
        testGame.addStone(0,1, 1);
        testGame.addStone(1,0, 1);

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
    void testFindTerritories(){

        int testSize = 5;
        Game testGame = new Game(testSize);

        testGame.addStone(1, 2, 1);
        testGame.addStone(2, 1, 1);
        testGame.addStone(3, 2, 1);
        testGame.addStone(2, 3, 1);

        // region
        int[] territoryElement = {2,2};

        // check that the region is a territory
        testGame.findRegions(false);
        testGame.validateTerritories();
        testGame.findTerritoryColors();

        List<int[]> territoryPredicted = testGame.getTerritories().get(0);
        int[] territoryElementPredicted = territoryPredicted.get(0);

        assertArrayEquals(territoryElementPredicted, territoryElement);
    }

    @Test
    void testFindRegionsChain(){
        int testSize = 5;
        Game testGame = new Game(testSize);

        // add black stones
        testGame.addStone(0,0, 1);
        testGame.addStone(0,1, 1);
        testGame.addStone(0,2, 1);
        testGame.addStone(0,3, 1);
        testGame.addStone(0,4, 1);

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
    void testChangeStonePieRule(){
        int testSize = 2;
        Game testGame = new Game(testSize);
        int[][] testGrid = {{2,0}, {0,0}};

        // add black stone
        testGame.addStone(0,0, 1);

        // call pie rule
        testGame.changeStonePieRule();

        // true
        assertArrayEquals(testGame.getGrid(), testGrid);
    }

    @Test
    void testCheckWin(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addStone(0,0, 1);
        testGame.addStone(0,1, 1);

        // check win
        testGame.checkWin();

        assertTrue(testGame.isFinished());
        assertEquals(1, testGame.getWinner());
    }

    @Test
    void testCheckWinFalse(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addStone(0,0, 1);
        testGame.addStone(0,1, 2);

        // check win
        testGame.checkWin();

        assertFalse(testGame.isFinished());
    }

    @Test
    void testCheckTie(){
        int testSize = 2;
        Game testGame = new Game(testSize);

        testGame.addStone(0,0, 1);
        testGame.addStone(1,1, 1);
        testGame.addStone(0,1, 2);
        testGame.addStone(1,0, 2);

        // check tie
        testGame.checkWin();
        testGame.checkTie();

        assertTrue(testGame.isFinished());
        assertEquals(0, testGame.getWinner());
    }
}
