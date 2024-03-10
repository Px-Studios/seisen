package px.seisen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

import px.seisen.characters.Bunny;
import px.seisen.characters.Samurai;
import px.seisen.logic.Boomerang;
import px.seisen.stages.BaseStage;

public class GameScreen implements Screen {
    private final Seisen game;
    private final KeyboardControls keyboardControls;
    private OrthographicCamera camera;
    private Texture backgroundTex;
    private final Player player1, player2;
    private final Boomerang boomerang1;

public GameScreen(final Seisen game, BaseStage stage) {
    this.game = game;
    this.keyboardControls = new KeyboardControls();

    camera = new OrthographicCamera();
    camera.setToOrtho(false, 800, 480);

    backgroundTex = new Texture("stages/" + stage.getStageId() + ".png");

    player1 = new Player("Player 1", new Bunny(), true, stage.getStageHeight());
    player2 = new Player("Player 2", new Samurai(), false, stage.getStageHeight());
    boomerang1 = new Boomerang(player1, player2);
    player1.setBoomerang(boomerang1);

    Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/soundtrack.mp3"));
    sound.play(0.05f);
}

private void updatePlayer(Player player, float delta) {
    player.updateSprite();
    player.getMovement().applyPhysics(delta*15);
    player.getSprite().draw(game.batch);
}

private void updateBoomerang(Boomerang boomerang, float delta) {
    boomerang.update();
    boomerang.getSprite().draw(game.batch);
}

@Override
public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);

    camera.update();
    game.batch.setProjectionMatrix(camera.combined);

    game.batch.begin();
    game.batch.draw(backgroundTex, 0, 0, 800, 480);

    updatePlayer(player1, delta);
    updatePlayer(player2, delta);
    updateBoomerang(boomerang1, delta);

    game.font.draw(game.batch, "P1 HP: " + player1.getHealth(), 10, 470);
    game.font.draw(game.batch, "P2 HP: " + player2.getHealth(), 700, 470);

    game.batch.end();

    delta = Gdx.graphics.getDeltaTime();
    keyboardControls.handleInput(player1, player2, delta);
}
    @Override
    public void resize(int width, int height) {
        float aspectRatio = (float) height / (float) width;
        float scale;
        float cropX = 0;
        float cropY = 0;

        if (aspectRatio > 1.5) {
            scale = (float) width / 800;
            cropY = (height - 480 * scale) / 2;
        } else {
            scale = (float) height / 480;
            cropX = (width - 800 * scale) / 2;
        }

        Gdx.gl.glViewport((int) cropX, (int) cropY, (int) (800 * scale), (int) (480 * scale));
    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTex.dispose();
    }
}
