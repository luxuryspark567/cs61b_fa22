package byow.Utils;

import byow.Articles.Door;
import byow.Attribute.Direction;
import byow.Attribute.Directionset;
import byow.Attribute.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;

import static byow.Attribute.Direction.*;
import static byow.Attribute.Direction.setTrueBoolArrayByDirection;
import static byow.Core.Engine.*;
import static byow.Utils.TileUtils.*;
import static byow.Utils.TileUtils.isTileType;

public class DiggerRouteSearch implements RouteSearch{

    private Direction srcDir; // last move direction, 0-north, 1-west, 2-south, 3-east
    private Direction dstDir;

    public DiggerRouteSearch(Door doorSrc, Door doorDst) {
        srcDir = doorSrc.getDir();
        dstDir = doorDst.getDir();
    }
    private boolean[] checkSurroundings(Position curPos, Position dstPos, Direction lastDir, TETile[][] mWorld) {

        Position posCurNorth = getShiftPosition(curPos, Directionset.NORTH);
        Position posCurWest = getShiftPosition(curPos, Directionset.WEST);
        Position posCurSouth = getShiftPosition(curPos, Directionset.SOUTH);
        Position posCurEast = getShiftPosition(curPos, Directionset.EAST);

        boolean[] dirBool = new boolean[] {false, false, false, false};

        // if the destination is at reach, go that direction
        if (posCurNorth.equals(dstPos)) {
            Direction.setTrueBoolArrayByDirection(dirBool, Directionset.NORTH);
            return dirBool;
        }
        else if (posCurWest.equals(dstPos)) {
            Direction.setTrueBoolArrayByDirection(dirBool, Directionset.WEST);
            return dirBool;
        }
        else if (posCurSouth.equals(dstPos)) {
            Direction.setTrueBoolArrayByDirection(dirBool, Directionset.SOUTH);
            return dirBool;
        }
        else if (posCurEast.equals(dstPos)) {
            Direction.setTrueBoolArrayByDirection(dirBool, Directionset.EAST);
            return dirBool;
        }

        dirBool = new boolean[] {true, true, true, true};

        // 3.1, check every direction except the one you come from
        // to start with, you should not take the back direction where you just come from;
        Direction MoveBackDir = getRevertDir(lastDir);
        Direction.setFalseBoolArrayByDirection(dirBool, MoveBackDir);

        // 3.2 you should not run into a wall;
        if (isTileType(posCurNorth, Tileset.WALL, mWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
        }
        if (isTileType(posCurWest, Tileset.WALL, mWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
        }
        if (isTileType(posCurSouth, Tileset.WALL, mWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.SOUTH);
        }
        if (isTileType(posCurEast, Tileset.WALL, mWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
        }

        // 3.3 you should not run into a door;
        if (isTileType(posCurNorth, Tileset.UNLOCKED_DOOR, mWorld)
                || isTileType(posCurNorth, Tileset.LOCKED_DOOR, mWorld) ) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
        }
        if (isTileType(posCurWest, Tileset.UNLOCKED_DOOR, mWorld)
                || isTileType(posCurWest, Tileset.LOCKED_DOOR, mWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
        }
        if (isTileType(posCurSouth, Tileset.UNLOCKED_DOOR, mWorld)
                || isTileType(posCurSouth, Tileset.LOCKED_DOOR, mWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.SOUTH);
        }
        if (isTileType(posCurEast, Tileset.UNLOCKED_DOOR, mWorld)
                || isTileType(posCurEast, Tileset.LOCKED_DOOR, mWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
        }

        // 3.4 you should not run out of the canvas ;
        if (isOutOffWorldNorth(posCurNorth)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
        }
        if (isOutOffWorldWest(posCurWest)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
        }
        if (isOutOffWorldSouth(posCurSouth)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.SOUTH);
        }
        if (isOutOffWorldEast(posCurEast)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
        }

        // 3.5 you should not run to last line of the canvas, because there is no space to build walls;
        if (isOutOffWorldNorth(Direction.getShiftPosition(posCurNorth, Directionset.NORTH))) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
        }
        if (isOutOffWorldWest(Direction.getShiftPosition(posCurWest, Directionset.WEST))) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
        }
        if (isOutOffWorldSouth(Direction.getShiftPosition(posCurSouth, Directionset.SOUTH))) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.SOUTH);
        }
        if (isOutOffWorldEast(Direction.getShiftPosition(posCurEast, Directionset.EAST))) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
        }

        // last step, if there is no place to go, move back
        if (!(dirBool[0] || dirBool[1] || dirBool [2] || dirBool[3])) {
            Direction.setTrueBoolArrayByDirection(dirBool,MoveBackDir);
        }
        return dirBool;
    }

    // Manhattan Distance compass
    private boolean[] checkTheCompass(Position posSrc, Position posDst) {
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

    private int getLastWallCounts(Position posFake, Direction dir, TETile[][] mWorld) {
        int counter = 0;
        Position pos = Position.copyOf(posFake);
        // 1, check 90
        while(isInTileWorld(pos) && isTileType(pos, Tileset.WALL, mWorld)) {
            counter++;
            shiftPosition(pos, dir);
        }
        return counter;
    }

    private boolean[] checkExperienceOrientation(Position curPos, Direction lastDir, TETile[][] mWorld) {

        boolean[] dirExp;
        // experience 1: check the last moving direction,
        // and move action should better be consistent
        dirExp = getBooleanArrayFromDir(lastDir);

        // experience 3: when walk into a wall, check the wall distance of each side,
        // and add the shorter side direction to the result.
        Position posFake = getShiftPosition(curPos, lastDir);
        if (isTileType(posFake, Tileset.WALL, mWorld)) {
            // check the position on ether side of the fake position,
            // and make the shorter one's direction as a candidate.
            int dirIndex = getIndexFromDir(lastDir);
            int dirIndexTurn90 = Math.floorMod(dirIndex + 1, DIRECTION_NUM);
            Direction dirTurn90 = getDirectionByIndex(dirIndexTurn90);
            int wallCount90 = getLastWallCounts(posFake, dirTurn90, mWorld);

            int dirIndexTurn270 = Math.floorMod(dirIndex + 3, DIRECTION_NUM);
            Direction dirTurn270 = getDirectionByIndex(dirIndexTurn270);
            int wallCount270 = getLastWallCounts(posFake, dirTurn270, mWorld);

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
    public Direction getNextMoveDirection(Position curPos, Position dstPos, Direction lastDir, TETile[][] mWorld) {
        // 3, check your surroundings, which direction is the right direction?
        // 3.1 you should not take the back direction where you just come from, UNLESS YOU HAVE NOWHERE ELSE TO GO;
        // 3.2 you must not run into a wall;
        boolean[] dirSur = checkSurroundings(curPos, dstPos, lastDir, mWorld);

        // 1, check the compass, get a direction guide
        boolean[] dirCom = checkTheCompass(curPos, dstPos);

        // Use your experience to dig, or other might think you are not an experienced digger
        boolean[] dirExp = checkExperienceOrientation(curPos, lastDir, mWorld);

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

    @Override
    public List<Position> getRoute(Position posSrc, Position posDst, TETile[][] mWorld) {

        List<Position> route = new LinkedList<>();

        Position curPos = posSrc;
        Position lastPos = posSrc;
        Position dstPos = posDst;

        Direction lastDir = this.srcDir;
        Direction curDir = this.srcDir;

        // 1, back up world
        //refWorld = TETile.copyOf(mWorld);

        // 2, loop to find the route
        int looperLimit = 300;

        while (!curPos.equals(dstPos) && looperLimit > 0) {

            // back status before update everything
            lastPos = Position.copyOf(curPos);
            lastDir = curDir;

            // get direction
            curDir = getNextMoveDirection(curPos, dstPos, lastDir, mWorld);// decide should move to which direction;

            if (curDir == null) { // not place to go means a failed route found, return.
                return null;
            }

            // update position and save to route
            curPos = getShiftPosition(curPos, curDir);;
            route.add(curPos);

            looperLimit--;
            System.out.println(curDir);
            System.out.println(route);
        }

        if (looperLimit == 0) {
            return null;
        }
        else {
            return route;
        }
    }
}
