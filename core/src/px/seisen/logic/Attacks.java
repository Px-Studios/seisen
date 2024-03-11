package px.seisen.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import px.seisen.Player;
import px.seisen.characters.Bunny;

public class Attacks {
    private final Player player;

    public Attacks(Player player) {
        this.player = player;
    }

    public void attack(Player otherPlayer) {
        if (System.currentTimeMillis() - player.getLastAttackTime() < player.getCharacter().getAttackCooldown()) {
            return;
        }

        player.setLastAttackTime(System.currentTimeMillis());

        boolean toRight = player.isFacingRight();

        if (player.getCharacter() instanceof Bunny) {
            player.getBoomerang().startThrowing();

        } else {
            if (!player.canHit(otherPlayer)) {
                return;
            }

            otherPlayer.gotHit(player, toRight);
        }

        Sound sound = Gdx.audio.newSound(Gdx.files.internal("characters/" + player.getCharacter().getId() + "/attack.mp3"));
        sound.play(0.15f);
    }
}
