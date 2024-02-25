package px.seisen.characters;

import com.badlogic.gdx.graphics.Texture;

public class BaseCharacter {
    protected final int movementSpeed;
    protected String id, name;
    protected int width, height;
    protected int attackCooldown;
    protected int health;
    protected int damage;

    public BaseCharacter() {
        this.id = "base_demo";
        this.name = "BaseDemoCharacter";
        this.width = 100;
        this.height = 200;
        this.movementSpeed = 400;
        this.attackCooldown = 500;
        this.health = 200;
        this.damage = 10;
    }

    public int getHealth() {
        return this.health;
    }
    public int getDamage() {
        return this.damage;
    }

    public Texture getTexture(String state) {
        return new Texture("characters/" + this.id + "/" + state + ".png");
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
    public int getMovementSpeed() {
        return this.movementSpeed;
    }

    public int getAttackCooldown() {
        return this.attackCooldown;
    }
}
