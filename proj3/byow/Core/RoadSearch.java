package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Direction.*;
import static byow.Core.Direction.setTrueBoolArrayByDirection;
import static byow.Core.Directionset.SOUTH;
import static byow.Core.Engine.*;
import static byow.Core.Engine.WIDTH;
import static byow.Core.TileUtils.isInCanvas;
import static byow.Core.TileUtils.isTileType;

public class RoadSearch {

    private Position posSrc;
    private Position posDst; // dst position;
    private Position posCur; // the current position of our digger;
    private Position posPre; // previous position;
    private Direction lastMoveDir; // last move direction, 0-north, 1-west, 2-south, 3-east
    private Direction curMoveDir;

    public TETile[][] world;
    public TETile[][] refWorld;

    public RoadSearch(Position posSrc, Position posDst, TETile[][] world) {
        this.posSrc = posSrc;
        this.posDst = posDst;
        this.world = world;
        this.refWorld = TETile.copyOf(world);
    }

    public void setPosCur(Position pos) {
        this.posCur = Position.copyOf(pos);
    }

    public void setPosPre(Position pos) {
        this.posPre = Position.copyOf(pos);
    }

    public void setPosDst(Position pos) {
        this.posDst = Position.copyOf(pos);
    }
    public void setCurMoveDir(Direction dir) {
        this.curMoveDir = dir;
    }

    public Position getPosCur() {
        return this.posCur;
    }

    public Position getPosPre() {
        return posPre;
    }
    Direction getLastMoveDir() {
        return this.lastMoveDir;
    }
    public Position getPosDst() {
        return this.posDst;
    }
    public Direction getCurMoveDir() {
        return this.curMoveDir;
    }
    public void backUpCurrentStatus() {
        this.posPre.setX(this.posCur.getX());
        this.posPre.setY(this.posCur.getY());
        this.lastMoveDir = this.curMoveDir;
    }

    private boolean[] getDirectionFromCoordinatesDifference(Position posSrc, Position posDst) {
        int diffX = posDst.getX() - posSrc.getX();
        int diffY = posDst.getY() - posSrc.getY();
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

    // Manhattan Distance compass
    private boolean[] checkTheCompass() {
        return getDirectionFromCoordinatesDifference(posCur, posDst);
    }

    private boolean[] checkSurroundings() {

        boolean[] dirBool = new boolean[] {true, true, true, true};

        Direction MoveBackDir = getRevertDir(lastMoveDir);
        // 1, check every direction except the one you come from
        // to start with, you should not take the back direction where you just come from;
        Direction.setFalseBoolArrayByDirection(dirBool, MoveBackDir);

        Position posCurNorth = getShiftPosition(posCur, Directionset.NORTH);
        Position posCurWest = getShiftPosition(posCur, Directionset.WEST);
        Position posCurSouth = getShiftPosition(posCur, SOUTH);
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
            Direction.setFalseBoolArrayByDirection(dirBool, SOUTH);
        }
        if (isTileType(posCurEast, Tileset.WALL, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
        }

        // 3.1 you should not run into a door if the door is not the destination;
        // check Door

        if (isTileType(posCurNorth, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            // if a unlocked door is the destination door, is OK to enter
            if (posDst.getX() == posCurNorth.getX()
                    && posDst.getY() == posCurNorth.getY()) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
            }
        }

        if (isTileType(posCurWest, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            // if a unlocked door is the destination door, is OK to enter
            if (posDst.getX() == posCurWest.getX()
                    && posDst.getY() == posCurWest.getY()) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
            }
        }
        if (isTileType(posCurSouth, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            // door is unlocked door

            // if a unlocked door is the destination door, is OK to enter
            if (posDst.getX() == posCurSouth.getX()
                    && posDst.getY() == posCurSouth.getY()) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, SOUTH);
            }
        }
        if (isTileType(posCurEast, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            // if a unlocked door is the destination door, is OK to enter
            if (posDst.getX() == posCurEast.getX()
                    && posDst.getY() == posCurEast.getY()) {

            }
            else {
                Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
            }
        }

        // 3.1 you should not run out of the canvas ;
        // 3.2 you should not run to the frame the canvas, because there is no space to build walls;
        // check Wall
        if (posCurNorth.getY() >= HEIGHT - 1) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
        }
        if (posCurWest.getX() < 1) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
        }
        if (posCurSouth.getY() < 1) {
            Direction.setFalseBoolArrayByDirection(dirBool, SOUTH);
        }
        if (posCurEast.getX() >= WIDTH - 1) {
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
/*
    private int getStraightWalkingLimit() {
        if (lastMoveDir == NORTH || lastMoveDir == SOUTH) {
            return this.orgDistance.height;
        }
        else {
            return this.orgDistance.width;
        }
    }

    private Direction shiftDirectionRightAngle(Direction dir) {
        int dirIndex = getIndexFromDir(dir);
        int dirIndexTurn90 = Math.floorMod(dirIndex + 1, DIRECTION_NUM);
        int dirIndexTurn270 = Math.floorMod(dirIndex + 3, DIRECTION_NUM);
        if (this.dirOrg[dirIndexTurn90]) {
            return getDirectionByIndex(dirIndexTurn90);
        }
        else if (this.dirOrg[dirIndexTurn270]) {
            return getDirectionByIndex(dirIndexTurn270);
        }
        else {
            return dir;
        }
    }
*/

    private int getLastWallCounts(Position posFake, Direction dir) {
        int counter = 0;
        Position pos = Position.copyOf(posFake);
        // 1, check 90
        while(isInCanvas(pos) && isTileType(pos, Tileset.WALL, this.refWorld)) {
            counter++;
            shiftPosition(pos, dir);
        }
        return counter;
    }

    private boolean[] checkExperienceOrientation() {

        boolean[] dirExp;
        // experience 1: check the last moving direction,
        // and move action should better be consistent
        dirExp = getBooleanArrayFromDir(lastMoveDir);

        // experience 2: if move toward one direction more than
        // half the original distance in x or y, best to turn 90 toward the dst.
/*
        if (this.walkStraightCounts > getStraightWalkingLimit()) {
            dirExp = shiftDirectionRightAngle(lastMoveDir);

            // should clear distance counting
            // if it failed once, means it is not a right trying;
            // if not clear it, this check will keep trying to turn a right angle
            this.walkStraightCounts = 0;
        }
*/

        // experience 3: when walk into a wall, check the wall distance of each side,
        // and add the shorter side direction to the result.
        Position posFake = getShiftPosition(posCur, lastMoveDir);
        if (isTileType(posFake, Tileset.WALL, this.refWorld)) {
            // check the position on ether side of the fake position,
            // and make the shorter one's direction as a candidate.
            int dirIndex = getIndexFromDir(lastMoveDir);
            int dirIndexTurn90 = Math.floorMod(dirIndex + 1, DIRECTION_NUM);
            Direction dirTurn90 = getDirectionByIndex(dirIndexTurn90);
            int wallCount90 = getLastWallCounts(posFake, dirTurn90);

            int dirIndexTurn270 = Math.floorMod(dirIndex + 3, DIRECTION_NUM);
            Direction dirTurn270 = getDirectionByIndex(dirIndexTurn270);
            int wallCount270 = getLastWallCounts(posFake, dirTurn270);

            // choose the shorter side
            if (wallCount90 < wallCount270) {
                setTrueBoolArrayByDirection(dirExp, dirTurn90);
            }
            else {
                setTrueBoolArrayByDirection(dirExp, dirTurn270);
            }
        }

        return dirExp;
    }

    // get next direction based on the src, dst and world
    public Direction getNextMoveDirection() {
        // 3, check your surroundings, which direction is the right direction?
        // 3.1 you should not take the back direction where you just come from, UNLESS YOU HAVE NOWHERE ELSE TO GO;
        // 3.2 you must not run into a wall;
        boolean[] dirSur = checkSurroundings();

        // 1, check the compass, get a direction guide
        boolean[] dirCom = checkTheCompass();

        // Use your experience to dig, or other might think you are not an experienced digger
        boolean[] dirExp = checkExperienceOrientation();

        // 4, finally:
        // 4.1 if there are more than one direction to take, use a fucking dice, may the god guide you.
        // merge d1 and d2

        // move in to a new Tile, trying to move forward, walk left or walk right, but need to check surroundings,
        // because digger might run into a wall or something
        boolean[] dirMerge1 = mergeDirection(dirSur, dirExp);
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
}
