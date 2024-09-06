package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Direction.*;
import static byow.Core.Engine.*;
import static byow.Core.TileUtils.*;

public class DiggerAvatar {
    TETile[][] world;
    TETile[][] refWorld;
    Door srcDoor;
    Door dstDoor;
    Position posCur; // the current position of our digger;
    Position posPre; // previous position;
    Position posDst; // dst position;
    Direction lastMoveDir; // last move direction, 0-north, 1-west, 2-south, 3-east
    Direction curMoveDir;

    public DiggerAvatar(TETile[][]world) {

        this.world = world;

        this.refWorld = TETile.copyOf(world);// back up the world, and use the backup to search for routes.
    }

    public void arrangeDiggingJog(Door srcDoor, Door dstDoor) {

        this.srcDoor = srcDoor;
        this.dstDoor = dstDoor;

        // must create new object,
        // because position will be modified, thus the original data will be modified, which is not right.
        this.posCur = Position.copyOf(srcDoor.getPosition());
        this.curMoveDir = null;

        this.posPre = new Position(-1, -1);
        this.posDst = dstDoor.getPosition();

        backUpCurrentStatus();// back up current status

        this.curMoveDir = srcDoor.getDir();// take a first step, out of the door first

        walkOneTile(this.curMoveDir);
    }
    public Hallway digATunnel() {

        // digger got a campus, he walks towards the dst door, until reaching it.
        int looperLimit = LOOP_LIMIT;
        while (!posCur.equals(posDst) && looperLimit > 0) {
            backUpCurrentStatus();// back status before update everything
            curMoveDir = decideTheNextMove();// decide should move to which direction;
            if (curMoveDir == null) {
                return null;
            }
            walkOneTile(this.curMoveDir);
            looperLimit--;
        }
        return new Hallway(srcDoor, dstDoor);
    }

    private void backUpCurrentStatus() {
        this.posPre.x = this.posCur.x;
        this.posPre.y = this.posCur.y;
        this.lastMoveDir = this.curMoveDir;
    }
    // Manhattan Distance compass
    private boolean[] checkTheCompass() {

        int diffX = posDst.x - posCur.x;
        int diffY = posDst.y - posCur.y;
        /*
         *         |    x
         *         |
         * --------o-------
         *         |
         *         |
         * */
        if (diffX > 0 && diffY > 0) {
            return new boolean[]{true, false, false, true};
        }
        /*
         *         |
         *         |
         * --------o-------
         *         |
         *         |    x
         * */
        else if (diffX > 0 && diffY < 0) {
            return new boolean[]{false, false, true, true};
        }
        /*
         *     x   |
         *         |
         * --------o-------
         *         |
         *         |
         * */
        else if (diffX < 0 && diffY > 0){ //
            return new boolean[]{true, true, false, false};
        }
        /*
         *         |
         *         |
         * --------o-------
         *         |
         *     x   |
         * */
        else if (diffX < 0 && diffY < 0){ //
            return new boolean[]{false, true, true, false};
        }
        /*
         *         |
         *         x
         *         |
         * --------o-------
         *         |
         *         |
         * */
        else if (diffX == 0 && diffY > 0){ //
            return new boolean[]{true, false, false, false};
        }
        /*
         *         |
         *         |
         * ---x----o-------
         *         |
         *         |
         * */
        else if (diffX < 0 && diffY == 0){ //
            return new boolean[]{false, true, false, false};
        }
        /*
         *         |
         *         |
         * --------o-------
         *         |
         *         x
         *         |
         * */
        else if (diffX == 0 && diffY < 0){ //
            return new boolean[]{false, false, true, false};
        }
        /*
         *         |
         *         |
         * --------o----x---
         *         |
         *         |
         * */
        else if (diffX > 0 && diffY == 0){ //
            return new boolean[]{false, false, false, true};
        }

        return new boolean[]{false, false, false, false};
    }

    private boolean[] checkSurroundings() {

        boolean[] dirBool = new boolean[] {true, true, true, true};

        Direction MoveBackDir = getRevertDir(lastMoveDir);
        // 1, check every direction except the one you come from
        // to start with, you should not take the back direction where you just come from;
        Direction.setFalseBoolArrayByDirection(dirBool, MoveBackDir);

        Position posCurNorth = getShiftPosition(posCur, Directionset.NORTH);
        Position posCurWest = getShiftPosition(posCur, Directionset.WEST);
        Position posCurSouth = getShiftPosition(posCur, Directionset.SOUTH);
        Position posCurEast = getShiftPosition(posCur, Directionset.EAST);

        // 3.1 you should not run into a wall;
        // check Wall
        if (isTileType(posCurNorth, Tileset.WALL, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
        }
        if (isTileType(posCurWest, Tileset.WALL, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
        }
        if (isTileType(posCurSouth, Tileset.WALL, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.SOUTH);
        }
        if (isTileType(posCurEast, Tileset.WALL, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
        }

        // 3.1 you should not run into a door if the door is not the destination;
        // check Door

        if (isTileType(posCurNorth, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            // if a unlocked door is the destination door, is OK to enter
            if (dstDoor.getPosition().x == posCurNorth.x
                    && dstDoor.getPosition().y == posCurNorth.y) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
            }
        }

        if (isTileType(posCurWest, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            // if a unlocked door is the destination door, is OK to enter
            if (dstDoor.getPosition().x == posCurWest.x
                    && dstDoor.getPosition().y == posCurWest.y) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
            }
        }
        if (isTileType(posCurSouth, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            // door is unlocked door

            // if a unlocked door is the destination door, is OK to enter
            if (dstDoor.getPosition().x == posCurSouth.x
                    && dstDoor.getPosition().y == posCurSouth.y) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.SOUTH);
            }
        }
        if (isTileType(posCurEast, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            // if a unlocked door is the destination door, is OK to enter
            if (dstDoor.getPosition().x == posCurEast.x
                    && dstDoor.getPosition().y == posCurEast.y) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
            }
        }

        // 3.1 you should not run out of the canvas;
        // check Wall
        if (posCurNorth.y >= HEIGHT) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
        }
        if (posCurWest.x < 0) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
        }
        if (posCurSouth.y < 0) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.SOUTH);
        }
        if (posCurEast.x >= WIDTH) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
        }
        // last step, if there is no place to go, move back
        if (!(dirBool[0] || dirBool[1] || dirBool [2] || dirBool[3])) {
            Direction.setTrueBoolArrayByDirection(dirBool,MoveBackDir);
        }
        return dirBool;

    }

    private boolean[] mergeDirection(boolean[] dir1, boolean[] dir2) {
        boolean[] dirRet = new boolean[DIRECTION_NUM];
        for (int i = 0; i < DIRECTION_NUM; i++) {
            dirRet[i] = dir1[i] && dir2[i];
        }
        return dirRet;
    }

    private int getDirNum(boolean[] dir) {
        int counter = 0;
        for (int i = 0; i < DIRECTION_NUM; i++) {
            if (dir[i]) {
                counter++;
            }
        }
        return counter;
    }

    private Direction decideTheNextMove() {

        // 3, check your surroundings, which direction is the right direction?
        // 3.1 you should not take the back direction where you just come from, UNLESS YOU HAVE NOWHERE ELSE TO GO;
        // 3.2 you must not run into a wall;
        boolean[] dirSur = checkSurroundings();

        // 1, check the compass, get a direction guide
        boolean[] dirCom = checkTheCompass();

        // 2, check the last moving direction, and move action should better be consistent
        // or other might think you are not an experienced digger
        boolean[] dirLast = getBooleanArrayFromDir(lastMoveDir);

        // 4, finally:
        // 4.1 if there are more than one direction to take, use a fucking dice, may the god guide you.
        // merge d1 and d2

        // move in to a new Tile, trying to move forward, walk left or walk right, but need to check surroundings,
        // because digger might run into a wall or something
        boolean[] dirMerge1 = mergeDirection(dirSur, dirLast);
        if (getDirNum(dirMerge1) == 0) {
            // if last move is impossible while you check your surroundings, should stick to the current
            // environment, because you can't break in to a wall, at least you can't right now;
            dirMerge1 = dirSur;
        }
        //else: if there is match, should stick to the last move direction

        boolean[] dirMerge2 = mergeDirection(dirSur, dirCom);
        if (getDirNum(dirMerge2) == 0) {
            // if last move is impossible while you check your surroundings, should stick to the current
            // environment, because you can't break in to a wall, at least you can't right now;
            dirMerge2 = dirSur;
        }

        boolean[] dirMerge3 = mergeDirection(dirMerge1, dirMerge2);
        if (getDirNum(dirMerge3) == 0) {

            dirMerge3 = dirMerge2;
        }

        int dirNum = getDirNum(dirMerge3);
        if (dirNum == 0) {
            System.out.println("No step to take, and this should never occur");
            return null;
        }
        else {
            // toll the dice.
            int randomIndex = RandomUtils.uniform(RANDOM, dirNum);
            int looper;
            for (looper = 0; looper < DIRECTION_NUM; looper++) {
                if (dirMerge3[looper]) {
                    randomIndex--;
                    if (randomIndex == -1) {
                        break;
                    }
                }
            }
            return Direction.getDirectionByIndex(looper);
        }
    }

    private void walkOneTile(Direction dir) {

        Direction.shiftPosition(posCur, dir); // shift the next position

        if (!isTileType(posCur, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            paintTile(posCur,Tileset.FLOOR, this.world);// Update canvas
        }

        // paint wall along the way
        // 1, paint the side
        paintSideWall(posCur, dir, this.world);

        // 2, if turned 90 degree
        if (isTurned90Degree(lastMoveDir, curMoveDir)) {
            // get position if moved in the lastMoveDir direction
            Position posFake = getShiftPosition(posPre, lastMoveDir);
            paintWallTile(posFake, this.world);
            paintSideWall(posFake, lastMoveDir, this.world);
        }
        // 3, if turned 180 degree (must have run into a wall or out of canvas, should be OK not to process)
    }
}