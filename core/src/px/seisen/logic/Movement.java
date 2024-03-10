package px.seisen.logic;

import px.seisen.Player;

public class Movement {
    private final Player player;
    private final float INITIAL_JUMP_VELOCITY = 30f;
    private final float GRAVITY = -9.8f;
    private float jumpVelocity;

    public Movement(Player player) {
        this.player = player;
        this.jumpVelocity = 0;
    }

    public void moveLeft(float timeDelta) {
        player.setX(player.getX() - player.getCharacter().getMovementSpeed() * timeDelta);
        player.setFacingRight(false);
        player.updateSpritePosition();
    }

    public void moveRight(float timeDelta) {
        player.setX(player.getX() + player.getCharacter().getMovementSpeed() * timeDelta);
        player.setFacingRight(true);
        player.updateSpritePosition();
    }

    public void jump() {
        if (!player.isJumping()) {
            player.setIsJumping(true);
            jumpVelocity = INITIAL_JUMP_VELOCITY;
        }
    }

    public void applyPhysics(float deltaTime) {
        player.setCanCrit(false);
        if (player.isJumping()) {
            jumpVelocity += GRAVITY * deltaTime;
            player.setY(player.getY() + jumpVelocity * deltaTime);
            player.setCanCrit(jumpVelocity < 0);

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
