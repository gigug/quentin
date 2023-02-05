import objects.Board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TestBoard {

    private int testSize;
    private Board testBoard;

    @AfterEach
    void tearDown(){
        testBoard = null;
    }
    @Test
    void testGrid(){

        testSize = 3;
        testBoard = new Board(testSize);

        int[][] testGrid = {{0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0}};
        assertArrayEquals(testBoard.getGrid(), testGrid);
    }

    @Test
    void testCheckInGrid(){
        testSize = 2;
        testBoard = new Board(testSize);

        assertTrue(testBoard.checkInGrid(0,0));
        assertFalse(testBoard.checkInGrid(2,2));
    }

    @Test
    void testCheckEmpty(){
        testSize = 2;
        testBoard = new Board(testSize);

        assertTrue(testBoard.checkEmpty(0,0));
        testBoard.addStone(1,1, 1);
        assertFalse(testBoard.checkEmpty(1,1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void testGetStone(int tileId) {
        testSize = 3;
        testBoard = new Board(testSize);

        assertEquals(testBoard.getStone(tileId, tileId), 0);
    }

    @Test
    void testChangeStonePieRule(){
        testSize = 2;
        testBoard = new Board(testSize);
        int[][] testGrid = {{2,0}, {0,0}};

        testBoard.addStone(0,0, 1);
        testBoard.changeStonePieRule();

        assertArrayEquals(testBoard.getGrid(), testGrid);
    }

}
