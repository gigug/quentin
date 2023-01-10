import objects.Board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import screens.Game;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {

    @Test
    public void testGrid(){

        int testSize = 3;
        Board testBoard = new Board(testSize);

        int[][] testGrid = {{0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0}};
        assertArrayEquals(testBoard.getGrid(), testGrid);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testGetPiece(int tileId) {
        int testSize = 3;
        Board testBoard = new Board(testSize);

        assertEquals(testBoard.getPiece(tileId, tileId), 0);
    }

}
