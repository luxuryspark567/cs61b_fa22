package byow.Core;

import byow.TileEngine.TETile;

import java.util.LinkedList;

import static byow.Core.Engine.LOOP_LIMIT;

public class Bear extends Creature{
    private static final int DAMAGE_DFT = 30;
    private static final int HEALTH_DFT = 100;
    private static final int AGE_DFT = 8;
    private static final int WEIGHT_DFT = 2;
    private static final Size SIZE = new Size(2, 2);
    private static final int AFFECTION_DFT = -100;
    // arms at hand
    public Bear(Position pos, RoomGraph rg, TETile[][] world, TETile[][] refWorld) {
        super(DAMAGE_DFT, HEALTH_DFT, AGE_DFT, WEIGHT_DFT, SIZE, AFFECTION_DFT, pos, rg, world, refWorld);
    }
    public Bear(int damage, int health, int age, int weight, Size size, int affection, Position pos, RoomGraph rg, TETile[][] world, TETile[][] refWorld) {
        super(damage, health, age, weight, size, affection, pos, rg, world, refWorld);
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