package byow.Charactors;

import byow.Articles.Door;
import byow.Articles.Hallway;
import byow.Attribute.Direction;
import byow.Attribute.Directionset;
import byow.Attribute.Position;
import byow.Core.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Utils.RoadSearch;

import static byow.Attribute.Direction.*;
import static byow.Attribute.Directionset.SOUTH;
import static byow.Core.Engine.*;
import static byow.Utils.TileUtils.*;

public class DiggerAvatar extends Creature {

    //TETile[][] world;
    //TETile[][] refWorld;
    Door srcDoor;
    Door dstDoor;

    TETile[][] refWorld;
    int len;// how many tiles are dug for this tunnel

    public DiggerAvatar(GameState gameState) {
        super(gameState);
        this.refWorld = TETile.copyOf(gameState.world);
    }

    public DiggerAvatar(Door srcDoor, Door dstDoor, GameState gameState) {
        super(srcDoor.getPosition(), dstDoor.getPosition(), srcDoor.getDir(), gameState);
        this.srcDoor = srcDoor;
        this.dstDoor = dstDoor;
        this.len = 0;
        this.refWorld = TETile.copyOf(gameState.world);
        // back up the world, and use the backup to search for routes.
    }
    public void arrangeDiggingJob(Door srcDoor, Door dstDoor) {
        initRoadSearch(srcDoor.getPosition(), dstDoor.getPosition(), srcDoor.getDir());
        this.srcDoor = srcDoor;
        this.dstDoor = dstDoor;
        this.len = 0;
        //this.world = world;
        //this.refWorld = TETile.copyOf(world);// back up the world, and use the backup to search for routes.
    }
    public Hallway digATunnel() {

        // digger got a campus, he walks towards the dst door, until reaching it.
        int looperLimit = LOOP_LIMIT;
        while (!this.getPosCur().equals(this.getPosDst()) && looperLimit > 0) {
            backUpCurrentStatus();// back status before update everything
            //curMoveDir = decideTheNextMove();// decide should move to which direction;
            this.setCurMoveDir(getNextMoveDirection());// decide should move to which direction;
            if (this.getCurMoveDir() == null) {
                return null;
            }
            digOneTile(this.getCurMoveDir());
            looperLimit--;
        }
        if (looperLimit == 0) {
            return null;
        }
        else {
            return new Hallway(srcDoor, dstDoor, len);
        }
    }
    private void digOneTile(Direction dir) {

        Direction.shiftPosition(this.getPosCur(), dir); // shift the next position

        //updateWalkStraightCounts();

        if (!isTileType(this.getPosCur(), Tileset.UNLOCKED_DOOR, this.refWorld)  //do not paint unlocked door
                && !isTileType(this.getPosCur(), Tileset.LOCKED_DOOR, this.refWorld)) {//do not paint locked door
            paintTile(this.getPosCur(),Tileset.FLOOR, this.world);// Update canvas
            this.len++;
        }

        // paint wall along the way
        // 1, paint the side
        paintSideWall(this.getPosCur(), dir, this.world);

        // 2, if turned 90 degree
        if (isTurned90Degree(this.getLastMoveDir(), this.getCurMoveDir())) {
            // get position if moved in the lastMoveDir direction
            Position posFake = getShiftPosition(this.getPosPre(), this.getLastMoveDir());
            paintWallTile(posFake, this.world);
            paintSideWall(posFake, this.getLastMoveDir(), this.world);
        }

        // 3, if turned 180 degree
        if (isTurned180Degree(this.getLastMoveDir(), this.getCurMoveDir())) {
            // get position if moved in the lastMoveDir direction
            Position posFake = getShiftPosition(this.getPosPre(), this.getLastMoveDir());
            paintWallTile(posFake, this.world);
            paintSideWall(posFake, this.getLastMoveDir(), this.world);
        }
    }
    @Override
    public boolean MoveOneStep(Direction dir, TETile[][] mWorld) {

        if (dir == null) {
            return false;
        }

        return (super.MoveOneStep(dir, this.refWorld));
    }

    @Override
    public boolean[] checkSurroundings() {

        boolean[] dirBool = new boolean[] {true, true, true, true};

        Direction MoveBackDir = getRevertDir(this.getLastMoveDir());
        // 1, check every direction except the one you come from
        // to start with, you should not take the back direction where you just come from;
        Direction.setFalseBoolArrayByDirection(dirBool, MoveBackDir);

        Position posCurNorth = getShiftPosition(this.getPosCur(), Directionset.NORTH);
        Position posCurWest = getShiftPosition(this.getPosCur(), Directionset.WEST);
        Position posCurSouth = getShiftPosition(this.getPosCur(), SOUTH);
        Position posCurEast = getShiftPosition(this.getPosCur(), Directionset.EAST);

        // 3.1 digger should not run into a door if the door is not the destination;
        // check Door
        if (isTileType(posCurNorth, Tileset.UNLOCKED_DOOR, this.refWorld)
                || isTileType(posCurNorth, Tileset.LOCKED_DOOR, this.refWorld)) {
            // if an unlocked door is the destination door, is OK to enter
            if (this.getPosDst().equals(posCurNorth)) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
            }
        }

        if (isTileType(posCurWest, Tileset.UNLOCKED_DOOR, this.refWorld)
                || isTileType(posCurNorth, Tileset.LOCKED_DOOR, this.refWorld)) {
            // if a unlocked door is the destination door, is OK to enter
            if (this.getPosDst().equals(posCurWest)) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
            }
        }
        if (isTileType(posCurSouth, Tileset.UNLOCKED_DOOR, this.refWorld)
                || isTileType(posCurNorth, Tileset.LOCKED_DOOR, this.refWorld)) {
            // door is unlocked door

            // if a unlocked door is the destination door, is OK to enter
            if (this.getPosDst().equals(posCurSouth)) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, SOUTH);
            }
        }
        if (isTileType(posCurEast, Tileset.UNLOCKED_DOOR, this.refWorld)
                || isTileType(posCurNorth, Tileset.LOCKED_DOOR, this.refWorld)) {
            // if a unlocked door is the destination door, is OK to enter
            if (this.getPosDst().equals(posCurEast)) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
            }
        }

        return Direction.mergeDirection(dirBool, super.checkSurroundings());
    }
}