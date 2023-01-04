import objects.Board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import screens.Game;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {

    int size = 3;

    Game game = new Game(size);
    int[][] testGrid = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

    @Test
    public void testGrid(){
        assertArrayEquals(game.board.grid, testGrid);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testGetPiece(int tileId) {
        assertEquals(game.board.getPiece(tileId, tileId), 0);
    }

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

}
