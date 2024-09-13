package byow.Articles;

public class Arm extends Item {
    private static final int WEIGHT_DFT = 0;
    private static final int VALUE_DFT = 0;
    private static final int DURATION_DFT = 100;
    private static final int SIGNATURE_DFT = 1;

    private int defense;

    private int buff;

    public Arm() {
        super();
        this.defense = 0;
        this.buff = 0;
    }

    public Arm(int weight, int value, int defense, int dur, int buff) {
        super(weight, value, dur);
        this.defense = defense;
        this.buff = buff;
    }
}
