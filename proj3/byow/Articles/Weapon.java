package byow.Articles;

public class Weapon extends Item {

    private int damage;
    private int attackSpeed;

    private int range;


    public Weapon(int damage, int attackSpeed, int range, int dur, int weight, int value) {
        super(weight, value, dur);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
    }
}
