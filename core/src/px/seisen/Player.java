package px.seisen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import px.seisen.characters.BaseCharacter;
import px.seisen.logic.Aura;
import px.seisen.logic.Attacks;
import px.seisen.logic.Movement;

public class Player {

    private final int stageHeight;
    private int health;
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
    private boolean canCrit;
    private int auraTime;
    private long lastAuraTime;
    private final Sound auraSound;
    private float auraX;
    private float auraY;
    private final long auraSoundId;

    private final BaseCharacter character;
    private final Movement movement;
    private final Attacks attacks;
    private final Aura aura;

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
        this.stageHeight = stageHeight;

        if (isPlayerOne) {
            this.x = 100;
        } else {
            this.x = 600;
        }
        this.y = 80;

        this.movement = new Movement(this);
        this.attacks = new Attacks(this);
        this.aura = new Aura(this);
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
            this.health += (int) (auraIntensity * 2);
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

    public void updateSpritePosition() {
        this.movement.stayInBounds();
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

    public Sprite getSprite() {
        return this.sprite;
    }

    public Movement getMovement() {
        return movement;
    }

    public Aura getAura() {
        return aura;
    }

    public Attacks getAttacks() {
        return attacks;
    }

    public boolean isCanCrit() {
        return canCrit;
    }

    public float getAuraX() {
        return auraX;
    }

    public float getAuraY() {
        return auraY;
    }

    public float getTargetX() {
        return targetX;
    }

    public int getAuraTime() {
        return auraTime;
    }

    public float getKnockbackAnimationProgress() {
        return knockbackAnimationProgress;
    }

    public long getLastAuraTime() {
        return lastAuraTime;
    }

    public void setLastAuraTime(long lastAuraTime) {
        this.lastAuraTime = lastAuraTime;
    }

    public void setKnockbackAnimationProgress(float knockbackAnimationProgress) {
        this.knockbackAnimationProgress = knockbackAnimationProgress;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAuraTime(int auraTime) {
        this.auraTime = auraTime;
    }

    public void setAuraX(float auraX) {
        this.auraX = auraX;
    }

    public void setAuraY(float auraY) {
        this.auraY = auraY;
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }

    public void setCanCrit(boolean canCrit) {
        this.canCrit = canCrit;
    }

    public int getStageHeight() {
        return this.stageHeight;
    }

    public void setJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public void setFacingRight(boolean isFacingRight) {
        this.isFacingRight = isFacingRight;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public void setLastGotHitTime(long lastGotHitTime) {
        this.lastGotHitTime = lastGotHitTime;
    }

    public void setLastMoveSoundTime(long lastMoveSoundTime) {
        this.lastMoveSoundTime = lastMoveSoundTime;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public Sound getAuraSound() {
        return auraSound;
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

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }
}
