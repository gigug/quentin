package players;

public class PlayerWrapper{
    Player player;

    public PlayerWrapper(Player player){
        this.player = player;
    }

    public int getSide() {
        return player.getSide();
    }
}
