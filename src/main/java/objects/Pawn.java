package objects;

import java.awt.*;

public class Pawn {
    Color color;

    Pawn(Color color){
        this.color = color;
    }

    public void displayPawn(Graphics2D g2, int x, int y){
        g2.fillOval(x, y, 10, 10);
    }
}
