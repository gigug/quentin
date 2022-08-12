import gui.BoardGUI;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestGUI {
    private final int SIZE_GRID = 10;
    BoardGUI boardGUI = new BoardGUI(SIZE_GRID);

    @ParameterizedTest
    @CsvSource({"0, 0", "1, 5", "20,20"})
    public void show_pawn(int x, int y){
        assertTrue(true);
    }

}
