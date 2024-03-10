package px.seisen.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import px.seisen.Player;

public class Attacks {
    private final Player player;

    public Attacks(Player player) {
        this.player = player;
    }

    public void attack(Player otherPlayer) {
        if (System.currentTimeMillis() - player.getLastAttackTime() < player.getCharacter().getAttackCooldown()) {
            return;
        }

        if (!this.canHit(otherPlayer)) {
            return;
        }

        player.setLastAuraTime(System.currentTimeMillis());
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("characters/" + player.getCharacter().getId() + "/attack.mp3"));
        sound.play(0.3f);
        otherPlayer.gotHit(player);
    }

    public boolean canHit(Player otherPlayer) {
        float leftEdge = player.getX();
        float rightEdge = player.getX() + player.getCharacter().getWidth();
        float otherLeftEdge = otherPlayer.getX();
        float otherRightEdge = otherPlayer.getX() + otherPlayer.getCharacter().getWidth();

        return (leftEdge < otherRightEdge && rightEdge > otherLeftEdge);
    }

}
