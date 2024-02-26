package px.seisen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import javax.sound.midi.SysexMessage;

public class KeyboardControls {
    public void handleInput(Player player1, Player player2, float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { Gdx.app.exit(); }

        handlePlayerInput(player1, player2, delta, Input.Keys.A, Input.Keys.D, Input.Keys.C, Input.Keys.W, Input.Keys.S);
        handlePlayerInput(player2, player1, delta, Input.Keys.J, Input.Keys.L, Input.Keys.N, Input.Keys.I, Input.Keys.K);

        for (Player player : new Player[]{player1, player2}) { player.stayInBounds();}
    }

    private void handlePlayerInput(Player player, Player opponent, float delta, int moveLeftKey, int moveRightKey, int attackKey, int jumpKey, int auraKey) {
        if (Gdx.input.isKeyPressed(moveLeftKey)) { player.moveLeft(delta); }
        if (Gdx.input.isKeyPressed(moveRightKey)) { player.moveRight(delta); }
        if (Gdx.input.isKeyPressed(attackKey)) { player.attack(opponent); }
        if (!Gdx.input.isKeyPressed(auraKey)) { player.resetAura(); }
        if (Gdx.input.isKeyPressed(auraKey)) { player.aura(delta); }
        if (Gdx.input.isKeyJustPressed(jumpKey)) { player.jump(); }
    }
}