package px.seisen.logic;

import px.seisen.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Boomerang {
    private final Player otherPlayer;
    private float startX;
    private float startY;
    private long startTime;
    private final Sprite sprite;
    private final Player player;
    private boolean facingRight;

    public Boomerang(Player player, Player otherPlayer) {
        this.player = player;
        this.otherPlayer = otherPlayer;
        this.startTime = -1;
        this.sprite = new Sprite(new Texture("entities/boomerang/1.png"));
    }

    public void startThrowing() {
        this.startTime = System.currentTimeMillis();
        this.startX = player.getX() + 40;
        this.startY = player.getY() + 43;
        this.facingRight = player.isFacingRight();
    }

    public void update() {
        if (this.startTime == -1) {
            sprite.setScale(0f, 0f);
            sprite.setPosition(-100, -100);
            return;
        }

        if (sprite.getBoundingRectangle().overlaps(otherPlayer.getSprite().getBoundingRectangle())) {
            otherPlayer.gotHit(player, facingRight);
            this.startTime = -1;
            return;
        }

        long timePassed = System.currentTimeMillis() - this.startTime;

        float x = this.startX + timePassed * 0.8f;
        if (!facingRight) {
            x = this.startX - timePassed * 0.8f;
        }

        if (x > 800 || x < 0) {
            this.startTime = -1;
            return;
        }

        sprite.rotate(timePassed * 0.005f);

        sprite.setPosition(x, this.startY);
        sprite.setScale(3f, 3f);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
