package objects;

import java.awt.*;

public class Pawn {
    private final static int WIDTH = 40;
    Color color;
    private final int posX, posY;

    Pawn(Color color, int posX, int posY){
        this.color = color;
        this.posX = posX;
        this.posY = posY;
    }

    public void displayPawn(Graphics2D g2){
        g2.fillOval(this.posX, this.posY, WIDTH, WIDTH);
    }
}
