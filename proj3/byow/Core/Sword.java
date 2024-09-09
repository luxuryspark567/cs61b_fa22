package byow.Core;

public class Sword extends Weapon{
    private static final int DAMAGE_DFT = 2;
    private static final int RANGE_DFT = 1;

    private static final int ATTACK_SPEED_DFT = 2;
    private static final int DURABILITY_DFT = 100;
    private static final int WEIGHT_DFT = 8;
    private static final int VALUE_DFT = 200;

    public Sword() {
        super(DAMAGE_DFT, ATTACK_SPEED_DFT, RANGE_DFT, DURABILITY_DFT, WEIGHT_DFT, VALUE_DFT);
    }
    public Sword(int damage, int attackSpeed, int range, int dur, int weight, int value) {
        super(damage, attackSpeed, range, dur, weight, value);
    }
}
