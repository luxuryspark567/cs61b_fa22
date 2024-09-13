package byow.Charactors;

import byow.Articles.ChestPlate;
import byow.Core.GameState;
import byow.Attribute.Position;
import byow.Attribute.Size;

public class Bear extends Creature{
    private static final int DAMAGE_DFT = 30;
    private static final int HEALTH_DFT = 100;
    private static final int AGE_DFT = 8;
    private static final int WEIGHT_DFT = 2;
    private static final Size SIZE = new Size(2, 2);
    private static final int AFFECTION_DFT = -100;
    // arms at hand
    public Bear(Position pos, GameState gameState) {
        super(DAMAGE_DFT, HEALTH_DFT, AGE_DFT, WEIGHT_DFT, SIZE, AFFECTION_DFT, pos, gameState);
    }
    public Bear(int damage, int health, int age, int weight, Size size, int affection, Position pos, GameState gameState) {
        super(damage, health, age, weight, size, affection, pos, gameState);
        this.inventory.add(new ChestPlate());
    }

    //
    public void huntHero(Position posAvatar) {

        initRoadSearch(this.getPosition(), posAvatar, null);
        backUpCurrentStatus();// back status before update everything
        //curMoveDir = decideTheNextMove();// decide should move to which direction;
        setCurMoveDir(getNextMoveDirection());// decide should move to which direction;
        MoveOneStep(getCurMoveDir());
        /*
        // digger got a campus, he walks towards the dst door, until reaching it.
        int looperLimit = LOOP_LIMIT;
        while (!this.getPosCur().equals(this.getPosDst()) && looperLimit > 0) {
            backUpCurrentStatus();// back status before update everything
            //curMoveDir = decideTheNextMove();// decide should move to which direction;
            this.setCurMoveDir(getNextMoveDirection());// decide should move to which direction;
            if (this.getCurMoveDir() == null) {
                break;
            }
            MoveOneStep(getCurMoveDir());
            looperLimit--;
        }
         */
    }
}