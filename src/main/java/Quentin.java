import gui.BoardGUI;
import screens.Game;

import java.util.Arrays;
public class Quentin {
    public static void main(String[] args){
        // read size in input
        int size = Integer.parseInt(args[0]);

        // initialize and start game
        Game game = new Game(size);

        BoardGUI boardGUI = new BoardGUI(game.board);

        game.board.viewGrid();
        game.play();
    }
}
