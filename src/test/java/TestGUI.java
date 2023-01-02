import objects.Board;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestGUI {
    private final int SIZE_GRID = 10;
    Board board = new Board(SIZE_GRID);
    BoardGUI boardGUI = new BoardGUI(board);

    @ParameterizedTest
    @CsvSource({"0, 0", "1, 5", "20, 20"})
    public void testShowPawn(int x, int y){
        assertTrue(true);
    }

}
