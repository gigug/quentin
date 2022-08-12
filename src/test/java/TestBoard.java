import objects.Board;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestBoard {
    Board board = new Board(3);
    int[] testGrid = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    
    void demoTest(){
        assertArrayEquals(board.grid, testGrid);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    public void testGetPiece(int tileId) {
        assertEquals(board.getPiece(tileId), 0);
    }


}
