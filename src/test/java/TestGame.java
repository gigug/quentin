import objects.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import players.PlayerColor;
import screens.Game;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGame {

    private int testSize;
    private Game testGame;
    private Board testBoard;

    @AfterEach
    public void tearDown(){
        testGame = null;
        testBoard = null;
    }

    @Test
    void testPassable(){
        testSize = 2;
        testBoard = new Board(testSize);
        testGame = new Game(testSize);

        testBoard.addStone(0, 0, 1);
        testBoard.addStone(1, 0, 2);
        testBoard.addStone(0, 1, 2);

        testGame.setGrid(testBoard.getGrid());

        assertTrue(testGame.checkPassable());
    }

    @ParameterizedTest
    @CsvSource({"0,0","0,2","2,2","2,0"})
    void testCheckDiagonalFalse(int posX, int posY){
        testSize = 3;
        testBoard = new Board(testSize);
        testGame = new Game(testSize);

        testBoard.addStone(1, 1, 1);

        testGame.setGrid(testBoard.getGrid());
        assertFalse(testGame.checkDiagonal(posX, posY));
    }

    @ParameterizedTest
    @CsvSource({"0,0","1,2","2,1"})
    void testCheckDiagonalTrue(int posX, int posY){
        testSize = 3;
        testBoard = new Board(testSize);
        testGame = new Game(testSize);

        testBoard.addStone(1, 1, 1);
        testBoard.addStone(1, 0, 1);

        testGame.setGrid(testBoard.getGrid());

        assertTrue(testGame.checkDiagonal(posX, posY));
    }

    @Test
    void testGetCurrentPlayer(){
        testSize = 2;
        testGame = new Game(testSize);

        assertEquals(PlayerColor.BLACK, testGame.getCurrentPlayer());

        testGame.switchPlayer();
        assertEquals(PlayerColor.WHITE, testGame.getCurrentPlayer());
    }

    @Test
    void testGetOppositePlayer(){
        testSize = 2;
        testGame = new Game(testSize);

        assertEquals(2, testGame.getOppositePlayer());
        testGame.switchPlayer();
        assertEquals(1, testGame.getOppositePlayer());
    }

    @Test
    void testCheckPassable(){
        testSize = 2;
        testGame = new Game(testSize);
        testBoard = new Board(testSize);

        testBoard.addStone(0,0, 1);
        testBoard.addStone(0,1, 1);
        testBoard.addStone(1,0, 1);
        testBoard.addStone(1,1, 1);

        testGame.setGrid(testBoard.getGrid());
        assertTrue(testGame.checkPassable());
    }

    @Test
    void testFindRegions(){
        testSize = 2;
        testGame = new Game(testSize);
        testBoard = new Board(testSize);

        testBoard.addStone(0,0, 1);
        testBoard.addStone(0,1, 1);
        testBoard.addStone(1,0, 1);

        testGame.setGrid(testBoard.getGrid());
        testGame.findRegions(false);

        int[] regionElement = {1, 1};

        List<List<int[]>> regionsPredicted = testGame.getRegions();
        List<int[]> regionPredicted = regionsPredicted.get(0);
        int[] regionElementPredicted = regionPredicted.get(0);

        assertArrayEquals(regionElement, regionElementPredicted);
    }

    @Test
    void testFindTerritories(){
        testSize = 5;
        testGame = new Game(testSize);
        testBoard = new Board(testSize);

        testBoard.addStone(1, 2, 1);
        testBoard.addStone(2, 1, 1);
        testBoard.addStone(3, 2, 1);
        testBoard.addStone(2, 3, 1);

        testGame.setGrid(testBoard.getGrid());

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
        testSize = 5;
        testGame = new Game(testSize);
        testBoard = new Board(testSize);

        testBoard.addStone(0,0, 1);
        testBoard.addStone(0,1, 1);
        testBoard.addStone(0,2, 1);
        testBoard.addStone(0,3, 1);
        testBoard.addStone(0,4, 1);

        testGame.setGrid(testBoard.getGrid());

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
    void testCheckWin(){
        testSize = 2;
        testGame = new Game(testSize);
        testBoard = new Board(testSize);

        testBoard.addStone(0,0, 1);
        testBoard.addStone(0,1, 1);

        testGame.setGrid(testBoard.getGrid());

        // check win
        testGame.checkWin();

        assertTrue(testGame.isGameFinished());
        assertEquals(1, testGame.getWinner());
    }

    @Test
    void testCheckWinFalse(){
        testSize = 2;
        testGame = new Game(testSize);
        testBoard = new Board(testSize);

        testBoard.addStone(0,0, 1);
        testBoard.addStone(0,1, 2);

        testGame.setGrid(testBoard.getGrid());
        testGame.checkWin();

        assertFalse(testGame.isGameFinished());
    }

    @Test
    void testCheckTie(){
        testSize = 2;
        testGame = new Game(testSize);
        testBoard = new Board(testSize);

        testBoard.addStone(0,0, 1);
        testBoard.addStone(1,1, 1);
        testBoard.addStone(0,1, 2);
        testBoard.addStone(1,0, 2);

        testGame.setGrid(testBoard.getGrid());

        testGame.checkWin();
        testGame.checkTie();

        assertTrue(testGame.isGameFinished());
        assertEquals(0, testGame.getWinner());
    }
}
