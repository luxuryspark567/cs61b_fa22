package byow.Core;

import java.util.LinkedList;
public class Bear extends Creature{
    private static final int DAMAGE_DFT = 30;
    private static final int HEALTH_DFT = 100;
    private static final int AGE_DFT = 8;
    private static final int WEIGHT_DFT = 2;
    private static final Size SIZE = new Size(2, 2);
    private static final int AFFECTION_DFT = -100;
    // arms at hand
    public Bear() {
        super(DAMAGE_DFT, HEALTH_DFT, AGE_DFT, WEIGHT_DFT, SIZE, AFFECTION_DFT, new Position(0, 0));
    }
    public Bear(int damage, int health, int age, int weight, Size size, int affection, Position pos) {
        super(damage, health, age, weight, size, affection, pos);

        this.inventory.add(new ChestPlate());
    }
}
