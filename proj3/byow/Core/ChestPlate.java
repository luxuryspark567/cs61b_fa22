package byow.Core;

public class ChestPlate extends Arm{
    private static final int BUF_DFT = 1;

    private static final int DEFENSE_DFT = 2;
    private static final int DURABILITY_DFT = 100;
    private static final int WEIGHT_DFT = 8;
    private static final int VALUE_DFT = 200;

    public ChestPlate() {
        super(WEIGHT_DFT, VALUE_DFT, DEFENSE_DFT, DURABILITY_DFT, BUF_DFT);
    }
    public ChestPlate(int weight, int value, int defense, int dur, int buff) {
        super(weight, value, defense, dur, buff);
    }
}
