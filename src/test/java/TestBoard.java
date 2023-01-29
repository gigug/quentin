import objects.Board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import screens.Game;

import static org.junit.jupiter.api.Assertions.*;

class TestBoard {

    @Test
    void testGrid(){

        int testSize = 3;
        Board testBoard = new Board(testSize);

        int[][] testGrid = {{0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0}};
        assertArrayEquals(testBoard.getGrid(), testGrid);
    }

    @Test
    void testCheckInGrid(){
        int testSize = 2;
        Board testBoard = new Board(testSize);

        assertTrue(testBoard.checkInGrid(0,0));
        assertFalse(testBoard.checkInGrid(2,2));
    }

    @Test
    void testCheckEmpty(){
        int testSize = 2;
        Board testBoard = new Board(testSize);

        assertTrue(testBoard.checkEmpty(0,0));
        testBoard.addStone(1,1, 1);
        assertFalse(testBoard.checkEmpty(1,1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void testGetStone(int tileId) {
        int testSize = 3;
        Board testBoard = new Board(testSize);

        assertEquals(testBoard.getStone(tileId, tileId), 0);
    }

}
