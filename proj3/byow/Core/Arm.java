package byow.Core;

public class Arm extends Item {

    private int defense;
    private int durability;

    private int buff;

    public Arm() {
        super();
        this.defense = 0;
        this.durability = 0;
        this.buff = 0;
    }

    public Arm(int weight, int value, int defense, int dur, int buff) {
        super(weight, value);
        this.defense = defense;
        this.durability = dur;
        this.buff = buff;
    }
}
