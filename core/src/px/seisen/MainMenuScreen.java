package px.seisen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import px.seisen.stages.Sunrise;

public class MainMenuScreen implements Screen {
    final Seisen game;
    Stage stage;
    Texture backgroundTex;
    Image startImage, helpImage, quitImage;
    Sound theme;

    public MainMenuScreen(final Seisen game) {
        this.game = game;

        backgroundTex = new Texture("gui/bg.png");

        startImage = new Image(new Texture("buttons/start.png"));
        helpImage = new Image(new Texture("buttons/help.png"));
        quitImage = new Image(new Texture("buttons/quit.png"));

        stage = new Stage(new FitViewport(800, 480));
        Gdx.input.setInputProcessor(stage);

        startImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, new Sunrise()));
                dispose();
            }
        });

        helpImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://github.com/px-studios/seisen");
            }
        });

        quitImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(startImage).padBottom(20).row();
        table.add(helpImage).padBottom(20).row();
        table.add(quitImage).padBottom(20);
        stage.addActor(table);

        Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/main-menu.mp3"));
        long soundId = sound.play(0.05f);
        sound.setLooping(soundId, true);
        this.theme = sound;
    }

    @Override
    public void show() {
        // No additional setup required here
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTex, 0, 0, stage.getWidth(), stage.getHeight());
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        backgroundTex.dispose();
        this.theme.dispose();
    }
}
