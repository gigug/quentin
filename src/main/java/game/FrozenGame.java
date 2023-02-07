package game;

import players.PlayerColor;

/**
 * Interface for safe interaction between panels and the Game.
 */
public interface FrozenGame {
    int getWinner();
    int getTurn();
    boolean isGameFinished();
    boolean checkPassable();
    boolean checkPlaceable(int X, int Y);
    PlayerColor getCurrentPlayer();
}
