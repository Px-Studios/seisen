package px.seisen.characters;

public class Samurai extends BaseCharacter {
    public Samurai() {
        this.id = "samurai";
        this.name = "Samurai";
        this.width = (int) (84*1.3);
        this.height = (int) (154*1.3);
        this.attackCooldown = 500;
    }
}
