package px.seisen.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import px.seisen.Player;

public class Win {
    private Sprite sprite;
    private final Player player1, player2;
    private long winTime;

    public Win(Player player1, Player player2) {
        this.sprite = null;
        this.player1 = player1;
        this.player2 = player2;
        this.winTime = 0;
    }

    public void onWin() {
        player1.setLocked(true);
        player2.setLocked(true);

        player1.setHealth(Math.max(0, player1.getHealth()));
        player2.setHealth(Math.max(0, player2.getHealth()));
    }

    public void render(SpriteBatch batch) {
        if (player1.getHealth() > 0 && player2.getHealth() > 0) { return; }
        if (winTime == 0) {
            winTime = System.currentTimeMillis();
            onWin();
        }

        int winner = player1.getHealth() > 0 ? 1 : 2;

        Texture winTexture = new Texture("entities/win/" + winner + ".png");

        sprite = new Sprite(winTexture);
        sprite.setPosition(400 - sprite.getWidth() / 2, 240 - sprite.getHeight() / 2);

        sprite.draw(batch);
    }
}
