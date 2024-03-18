package px.seisen.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Countdown {
    private final long startTime;
    private Sprite sprite;
    private float fallingSpeed = 500.0f;
    private float alpha;

    public Countdown() {
        this.startTime = System.currentTimeMillis();
        this.sprite = new Sprite(new Texture(Gdx.files.internal("entities/countdown/3.png")));
        centerSprite();
        this.alpha = 1.0f;
    }

    private void centerSprite() {
        float startX = 800 / 2f - sprite.getWidth() / 2f;
        float startY = 480 / 2f - sprite.getHeight() / 2f + 100;
        sprite.setPosition(startX, startY);
    }

    public void render() {
        long currentTime = System.currentTimeMillis();
        double msElapsed = currentTime - startTime;

        int countdownNumber = 3 - (int) Math.floor(msElapsed / 1000);

        if (countdownNumber < 0) {
            if (alpha > 0) {
                alpha -= 0.01f;
            }
            if (alpha < 0) {
                alpha = 0;
            }

            sprite.setAlpha(alpha);
            return;
        }

        if (!sprite.getTexture().toString().equals("entities/countdown/" + countdownNumber + ".png")) {
            sprite.getTexture().dispose();
            sprite.setTexture(new Texture(Gdx.files.internal("entities/countdown/" + countdownNumber + ".png")));
            centerSprite();
        }

        float newY = sprite.getY() - fallingSpeed * Gdx.graphics.getDeltaTime();
        if (newY < 480 / 2f - sprite.getHeight() / 2f) {
            newY = 480 / 2f - sprite.getHeight() / 2f;
        }
        sprite.setY(newY);
    }

    public Sprite getSprite() {
        return sprite;
    }
}