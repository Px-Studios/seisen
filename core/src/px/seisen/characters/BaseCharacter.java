package px.seisen.characters;

import com.badlogic.gdx.graphics.Texture;

public abstract class BaseCharacter {

    private final String id;
    private final String name;
    private final int width;
    private final int height;
    private final int movementSpeed;
    private final int attackCooldown;
    private int health;
    private final int damage;

    protected BaseCharacter(String id, String name, int width, int height, int movementSpeed, int attackCooldown, int health, int damage) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.movementSpeed = movementSpeed;
        this.attackCooldown = attackCooldown;
        this.health = health;
        this.damage = damage;
    }

    // Getters for the fields that are final and hence won't change. Other setters/getters can be added based on needs.
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public Texture getTexture(String state) {
        return new Texture("characters/" + this.id + "/" + state + ".png");
    }
}
