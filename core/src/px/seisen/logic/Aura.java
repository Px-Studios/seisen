package px.seisen.logic;

import px.seisen.Player;

public class Aura {
    private final Player player;

    public Aura(Player player) {
        this.player = player;
    }

    public void resetAura() {
        if (player.getAuraTime() == 0) {
            return;
        }
        player.getAuraSound().stop();
        player.setAuraTime(0);

        if (player.getY() != player.getStageHeight()) {
            player.setY(player.getStageHeight());
        }
    }

    public void aura(float timeDelta) {
        if (player.isLocked()) { return; }
        if (player.isJumping()) { return; }

        if (System.currentTimeMillis() - player.getLastAuraTime() < 3000) {
            return;
        }

        if (player.getAuraTime() == 0) {
            player.setAuraX(player.getX());
            player.setAuraY(player.getY());
        }

        player.setAuraTime(player.getAuraTime() + (int) (timeDelta * 1000));

        if (player.getAuraTime() > 3000) {
            player.setAuraTime(0);
            player.getAuraSound().play(0.3f);
            player.setLastAuraTime(System.currentTimeMillis());

            if (player.getY() != 80) {
                player.setY(80);
            }
        }
    }
}
