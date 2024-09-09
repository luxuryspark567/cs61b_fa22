package byow.Core;

public class Weapon extends Item {

    private int damage;
    private int attackSpeed;

    private int range;

    private int durability;

    public Weapon(int damage, int attackSpeed, int range, int dur, int weight, int value) {
        super(weight, value);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.durability = dur;
    }
}
