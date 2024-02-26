package px.seisen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import px.seisen.characters.BaseCharacter;

import javax.sound.midi.SysexMessage;

public class Player {
    private final BaseCharacter character;
    private boolean isFacingRight;
    private boolean isJumping;
    private final boolean isPlayerOne;
    private final String name;
    private float x, y;
    private long lastGotHitTime;
    private long lastAttackTime;
    private long lastMoveSoundTime;
    private Sprite sprite;
    private float targetX;
    private float knockbackAnimationProgress;
    private float jumpVelocity;
    private int health;
    private final float GRAVITY = -9.8f; // Gravity, adjust as needed for your game scale
    private final float INITIAL_JUMP_VELOCITY = 30f; // Initial jump velocity, adjust for desired jump height
    private boolean canCrit;
    private int auraTime;
    private long lastAuraTime;
    private Sound auraSound;
    private float auraX;
    private float auraY;
    private long auraSoundId;

    public Player(String name, BaseCharacter character, boolean isPlayerOne, int stageHeight) {
        this.name = name;
        this.isJumping = false;
        this.character = character;
        this.isPlayerOne = isPlayerOne;
        this.isFacingRight = isPlayerOne;
        this.lastGotHitTime = 0;
        this.lastMoveSoundTime = 0;
        this.lastAttackTime = 0;
        this.knockbackAnimationProgress = 1;
        this.health = character.getHealth();
        this.canCrit = false;
        this.auraTime = 0;
        this.lastAuraTime = 0;
        this.auraSound = Gdx.audio.newSound(Gdx.files.internal("audio/aura.mp3"));
        this.auraX = 0;
        this.auraY = 0;
        this.auraSoundId = -1;

        if (isPlayerOne) {
            this.x = 100;
        } else {
            this.x = 600;
        }
        this.y = 80;

    }

    public void resetAura() {
        if (this.auraTime == 0) {
            return;
        }
        this.auraTime = 0;
        this.auraSound.stop();

        if (this.y != 80) {
            this.y = 80;
        }
    }

    public void aura(float timeDelta) {
        if (this.isJumping) {
            return;
        }

        // don't allow a new aura if the last one was less than 3 seconds ago
        if (System.currentTimeMillis() - this.lastAuraTime < 3000) {
            return;
        }

        if (this.auraTime == 0) {
            this.auraX = this.x;
            this.auraY = this.y;
        }

        this.auraTime += (int) (timeDelta * 1000);

        if (this.auraTime > 3000) {
            this.auraTime = 0;
            this.auraSoundId = auraSound.play(0.03f);
            this.lastAuraTime = System.currentTimeMillis();

            if (this.y != 80) {
                this.y = 80;
            }
         }
    }

    public void attack(Player otherPlayer) {
        if (System.currentTimeMillis() - this.lastAttackTime < this.character.getAttackCooldown()) {
            return;
        }

        if (!this.canHit(otherPlayer)) {
            return;
        }

        this.lastAttackTime = System.currentTimeMillis();
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("characters/" + this.character.getId() + "/attack.mp3"));
        sound.play(0.3f);
        otherPlayer.gotHit(this);
    }

    public void gotHit(Player otherPlayer) {
        this.lastGotHitTime = System.currentTimeMillis();

        int baseDamage = otherPlayer.getCharacter().getDamage();
        int knockback;

        if (otherPlayer.canCrit) {
            this.health -= baseDamage * 2;
            knockback = 100;
            this.canCrit = false;

            Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/punch.mp3"));
            sound.play(0.3f);
        } else {
            this.health -= baseDamage;
            knockback = 50;
        }

        if (otherPlayer.isFacingRight) {
            this.targetX = this.x + knockback;
        } else {
            this.targetX = this.x - knockback;
        }

        this.knockbackAnimationProgress = 0;
    }

    public String getState() {
        if (System.currentTimeMillis() - this.lastAttackTime < this.character.getAttackCooldown()) {
            return "attack";
        } else {
            return "normal";
        }
    }

    public boolean canHit(Player otherPlayer) {
        float leftEdge = this.x;
        float rightEdge = this.x + this.character.getWidth();
        float otherLeftEdge = otherPlayer.getX();
        float otherRightEdge = otherPlayer.getX() + otherPlayer.getCharacter().getWidth();

        return (leftEdge < otherRightEdge && rightEdge > otherLeftEdge);
    }

    public void updateSprite() {
        Sprite sprite = new Sprite(this.getCharacter().getTexture(this.getState()));

        if (this.auraTime == 0) {
            if (this.knockbackAnimationProgress < 1) {
                float t = this.knockbackAnimationProgress;
                this.knockbackAnimationProgress += 0.01f;

                float c1 = 1.70158f;
                float c3 = c1 + 1;
                float easeOutBack = 1 + c3 * (float)Math.pow((t - 1), 3) + c1 * (float)Math.pow((t - 1), 2);

                float oldX = this.x;
                this.x = (1 - easeOutBack) * oldX + easeOutBack * this.targetX;
            }

        } else {
            this.x = this.auraX;
            this.y = this.auraY + ((float) this.auraTime / 100);
            sprite.setColor(1, 1, 1, 1.0f - ((float) this.auraTime / 8000));
        }

        if (System.currentTimeMillis() - this.lastAuraTime < 1000) {
            float auraIntensity = 1.f - ((float) (System.currentTimeMillis() - this.lastAuraTime) / 1000);
            sprite.setColor(1-auraIntensity, 1, 1-auraIntensity, 1-auraIntensity);
            this.auraSound.setVolume(this.auraSoundId, auraIntensity);
            this.health += (int) ((System.currentTimeMillis() - this.lastAuraTime) / 100);
        }

        sprite.setPosition(this.x, this.y);
        sprite.setSize(this.character.getWidth(), this.character.getHeight());

        if (sprite.isFlipX() == this.isFacingRight()) {
            sprite.flip(true, false);
        }

        if (this.lastGotHitTime > System.currentTimeMillis() - 300) {
            sprite.setColor(1, 0, 0, 1);
        }

        this.sprite = sprite;
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            jumpVelocity = INITIAL_JUMP_VELOCITY;
        }
    }

    public void applyPhysics(float deltaTime) {
        this.canCrit = false;
        if (isJumping) {
            jumpVelocity += GRAVITY * deltaTime;
            y += jumpVelocity * deltaTime;
            this.canCrit = jumpVelocity < 0;

            if (y <= 80) {
                y = 80;
                isJumping = false;
            }
        }
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void updateSpritePosition() {
        this.stayInBounds();
        this.updateSprite();

        if (this.auraTime != 0) {
            return;
        }

        if (System.currentTimeMillis() - this.lastMoveSoundTime > (int) (Math.random() * 500 + 190)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/step_" + (int) (Math.random() * 2 + 1) + ".mp3"));
            sound.play(Math.min(0.5f, (float) Math.random() * 0.2f + 0.1f));
            this.lastMoveSoundTime = System.currentTimeMillis();
        }
    }

    public void moveLeft(float timeDelta) {
        this.x -= this.character.getMovementSpeed() * timeDelta;
        this.isFacingRight = false;
        this.updateSpritePosition();
    }

    public void moveRight(float timeDelta) {
        this.x += this.character.getMovementSpeed() * timeDelta;
        this.isFacingRight = true;
        this.updateSpritePosition();
    }

    public void stayInBounds() {
        if (this.x < 0) {
            this.x = 0;
        }

        float rightEdgePosition = this.x + this.character.getWidth();
        if (rightEdgePosition > 800) {
            this.x = 800 - this.character.getWidth();
        }
    }

    public int getHealth() {
        return this.health;
    }

    public float getX() {
        return this.x;
    }
    public float getY() {
        return this.y;
    }
    public BaseCharacter getCharacter() {
        return this.character;
    }

    public boolean isPlayerOne() {
        return this.isPlayerOne;
    }
    public boolean isJumping() {
        return this.isJumping;
    }
    public String getName() {
        return this.name;
    }
    public boolean isFacingRight() {
        return this.isFacingRight;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public long getLastGotHitTime() {
        return lastGotHitTime;
    }
}
