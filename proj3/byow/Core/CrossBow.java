package byow.Core;

public class CrossBow extends Weapon {
    private static final int DAMAGE_DFT = 1;
    private static final int RANGE_DFT = 10;

    private static final int ATTACK_SPEED_DFT = 1;
    private static final int DURABILITY_DFT = 100;
    private static final int WEIGHT_DFT = 5;
    private static final int VALUE_DFT = 100;

    public CrossBow() {
        super(DAMAGE_DFT, ATTACK_SPEED_DFT, RANGE_DFT, DURABILITY_DFT, WEIGHT_DFT, VALUE_DFT);
    }
    public CrossBow(int damage, int attackSpeed, int range, int dur, int weight, int value) {
        super(damage, attackSpeed, range, dur, weight, value);
    }
}
