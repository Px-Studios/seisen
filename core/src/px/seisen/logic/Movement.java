package px.seisen.logic;

import px.seisen.Player;
import px.seisen.characters.Bunny;
import px.seisen.characters.Samurai;

public class Movement {
    private final Player player;
    private final float GRAVITY = -9.8f;
    private float jumpVelocity;
    private long lastDoubleJumpTime;

    public Movement(Player player) {
        this.player = player;
        this.jumpVelocity = 0;
        this.lastDoubleJumpTime = 0;
    }

    public void moveLeft(float timeDelta) {
        if (player.isLocked()) { return; }
        player.setX(player.getX() - player.getCharacter().getMovementSpeed() * timeDelta);
        player.setFacingRight(false);
        player.updateSpritePosition();
    }

    public void moveRight(float timeDelta) {
        if (player.isLocked()) { return; }
        player.setX(player.getX() + player.getCharacter().getMovementSpeed() * timeDelta);
        player.setFacingRight(true);
        player.updateSpritePosition();
    }

    public void jump() {
        if (player.isLocked()) { return; }
        if (!player.isJumping()) {
            player.setIsJumping(true);
            jumpVelocity = this.player.getCharacter().getJumpVelocity();
        } else if (player.getCharacter() instanceof Samurai) {
            if (System.currentTimeMillis() - lastDoubleJumpTime > 750) {
                jumpVelocity = this.player.getCharacter().getJumpVelocity();
                lastDoubleJumpTime = System.currentTimeMillis();
            }
        }
    }

    public void applyPhysics(float deltaTime) {
        player.setCanCrit(false);
        if (player.isJumping()) {
            jumpVelocity += GRAVITY * deltaTime;
            player.setY(player.getY() + jumpVelocity * deltaTime);

            if (!(player.getCharacter() instanceof Bunny)) {
                player.setCanCrit(jumpVelocity < 0);
            }

            if (player.getY() < player.getStageHeight()) {
                player.setY(player.getStageHeight());
                player.setIsJumping(false);
            }
        }
    }

    public void stayInBounds() {
        if (player.getX() < 0) {
            player.setX(0);
        }

        float rightEdgePosition = player.getX() + player.getCharacter().getWidth();
        if (rightEdgePosition > 800) {
            player.setX(800 - player.getCharacter().getWidth());
        }
    }
}
