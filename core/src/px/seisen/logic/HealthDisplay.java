package px.seisen.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import px.seisen.Player;

public class HealthDisplay {
    private final Player player;
    private final Sprite baseSprite, fillSprite;
    private final Texture fillTexture;

    public HealthDisplay(Player player) {
        this.player = player;
        this.baseSprite = new Sprite(new Texture("entities/health/empty.png"));
        this.fillTexture = new Texture("entities/health/full.png");
        this.fillSprite = new Sprite(fillTexture);
    }

    public void update() {
        int x = player.isPlayerOne() ? 0 : 600;
        baseSprite.setPosition(x, 400);
        fillSprite.setPosition(x, 400);

        // crop fillSprite to match player's health
        int health = player.getHealth();
        TextureRegion region = new TextureRegion(fillTexture, 0, 0, fillTexture.getWidth() * health / player.getCharacter().getHealth(), fillTexture.getHeight());
        fillSprite.setRegion(region);
        fillSprite.setSize(region.getRegionWidth(), region.getRegionHeight());
    }

    public void render(Batch batch) {
        baseSprite.draw(batch);
        fillSprite.draw(batch);
    }
}