package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Engine.*;
import static byow.Core.Engine.DIRECTION_NUM;

public class DiggerAvatar {

    TETile[][] world;
    Door srcDoor;
    Door dstDoor;

    Position posCur; // the current position of our digger;
    Position posPre; // previous position;

    Position posDst; // dst position;

    int lastMoveDir; // last move direction, 0-north, 1-west, 2-south, 3-east
    int curMoveDir;

    public DiggerAvatar(Door srcDoor, Door dstDoor, TETile[][]world) {

        this.world = world;

        this.srcDoor = srcDoor;
        this.dstDoor = dstDoor;

        // must create new object,
        // because position will be modified, thus the original data will be modified, which is not right.
        this.posCur = new Position(srcDoor.getPosition().x, srcDoor.getPosition().y);
        this.curMoveDir = -1;

        this.posPre = new Position(-1, -1);
        this.posDst = dstDoor.getPosition();
        // take a first step, out of the door first

        // back up current status
        backUpCurrentStatus();
        //this.posPre.x = this.posCur.x;
        //this.posPre.y = this.posCur.y;
        //this.lastMoveDir = this.curMoveDir;

        // move
        this.curMoveDir = srcDoor.getDir();
        moveOneStep();
    }

    public Hallway digATunnel() {

        // digger got a campus, he walks towards the dst door, until reaching it.
        int looperLimit = LOOP_LIMIT;
        while (!posCur.equals(posDst) && looperLimit > 0) {

            // back status before update everything
            backUpCurrentStatus();

            // decide should move to which direction;
            curMoveDir = decideTheNextMove();
            if (curMoveDir < 0) {
                return null;
            }
            moveOneStep();

            looperLimit--;
        }

        return new Hallway(srcDoor, dstDoor, null);
    }

    private void backUpCurrentStatus() {
        this.posPre.x = this.posCur.x;
        this.posPre.y = this.posCur.y;
        this.lastMoveDir = this.curMoveDir;
    }

    private void moveOneStep() {
        if (this.curMoveDir == 0) {
            walkNorth();
        }
        else if (this.curMoveDir == 1) {
            walkWest();
        }
        else if (this.curMoveDir == 2) {
            walkSouth();
        }
        else { //(this.curMoveDir == 3) {
            walkEast();
        }
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

    private boolean[] checkLastMove() {
        return getBooleanArrayFromDir(lastMoveDir);
            /*
            if (lastMoveDir == 0) {
                return new boolean[]{true, false, false, false};
            }
            else if (lastMoveDir == 1) {
                return new boolean[]{false, true, false, false};
            }
            else if (lastMoveDir == 2) { //(diffX < 0 && diffY >= 0)
                return new boolean[]{false, false, true, false};
            }
            else { //(lastMoveDir == 3)
                return new boolean[]{false, false, false, true};
            }

             */
    }

    private void paintTile(int x, int y, TETile type) {
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT) {
            world[x][y] = type;
        }
        else {
            System.out.println("out of canvas!!!");
        }
    }
    private boolean checkTile(int x, int y, TETile type) {
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT) {
            return world[x][y] == type;
        }
        else {
            return true;
        }
    }

    private boolean isWallTile(int x, int y) {
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT) {
            return world[x][y] == Tileset.WALL;
        }
        else {
            return true; // if out of bound, should return true, because it means this direction is not doable
        }
    }

    private boolean isUnlockedDoorTile(int x, int y) {
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT) {
            return world[x][y] == Tileset.UNLOCKED_DOOR;
        }
        else {
            return true; // if out of bound, should return true, because it means this direction is not doable
        }
    }

    private boolean isFloorTile(int x, int y) {
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT) {
            return world[x][y] == Tileset.FLOOR;
        }
        else {
            return true; // if out of bound, should return true, because it means this direction is not doable
        }
    }
    private int revertDir(int dir) {
        return Math.floorMod(lastMoveDir + 2, DIRECTION_NUM);
    }

    private boolean[] getBooleanArrayFromDir(int dir) {
        if (dir == 0) {
            return new boolean[]{true, false, false, false};
        }
        else if (dir == 1) {
            return new boolean[]{false, true, false, false};
        }
        else if (dir == 2) { //(diffX < 0 && diffY >= 0)
            return new boolean[]{false, false, true, false};
        }
        else { //(lastMoveDir == 3)
            return new boolean[]{false, false, false, true};
        }
    }
    /*
            private int getDirFromBooleanArray(boolean[] dirA) {
                for (int looper = 0; looper < DIRECTION_NUM; looper++) {
                    if ()
                }
                if (dir == 0) {
                    return new boolean[]{true, false, false, false};
                }
                else if (dir == 1) {
                    return new boolean[]{false, true, false, false};
                }
                else if (dir == 2) { //(diffX < 0 && diffY >= 0)
                    return new boolean[]{false, false, true, false};
                }
                else { //(lastMoveDir == 3)
                    return new boolean[]{false, false, false, true};
                }
            }

     */
    private boolean[] checkSurroundings() {

        boolean[] dir = new boolean[] {true, true, true, true};

        int MoveBackDir = revertDir(lastMoveDir);
        // 1, check every direction except the one you come from
        // to start with, you should not take the back direction where you just come from;
        dir[MoveBackDir] = false;

        // 3.1 you should not run into a wall;
        // check Wall
        if (isWallTile(posCur.x, posCur.y + 1)) {
            dir[0] = false;
        }
        else if (isWallTile(posCur.x - 1, posCur.y)) {
            dir[1] = false;
        }
        else if (isWallTile(posCur.x, posCur.y - 1)) {
            dir[2] = false;
        }
        else if (isWallTile(posCur.x + 1, posCur.y)) {
            dir[3] = false;
        }

        // 3.1 you should not run out of the canvas;
        // check Wall
        if (posCur.y + 1 >= HEIGHT) {
            dir[0] = false;
        }
        else if (posCur.x - 1 < 0) {
            dir[1] = false;
        }
        else if (posCur.y - 1 < 0) {
            dir[2] = false;
        }
        else if (posCur.x + 1 >= WIDTH) {
            dir[3] = false;
        }

        // last step, if there is no place to go, move back
        if (!(dir[0] || dir[1] || dir [2] || dir[3])) {
            dir[MoveBackDir] = true;
        }
        return dir;

    }

    private boolean[] mergeDirection(boolean[] dir1, boolean[] dir2) {
        boolean[] dirRet = new boolean[4];
        for (int i = 0; i < 4; i++) {
            dirRet[i] = dir1[i] && dir2[i];
        }
        return dirRet;
    }

    private int getDirNum(boolean[] dir) {
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            if (dir[i]) {
                counter++;
            }
        }
        return counter;
    }

    private int decideTheNextMove() {

        // 3, check your surroundings, which direction is the right direction?
        // 3.1 you should not take the back direction where you just come from, UNLESS YOU HAVE NOWHERE ELSE TO GO;
        // 3.2 you must not run into a wall;
        boolean[] dirSur = checkSurroundings();

        // 1, check the compass, get a direction guide
        boolean[] dirCom = checkTheCompass();

        // 2, check the last moving direction, and move action should better be consistent
        // or other might think you are not an experienced digger
        boolean[] dirLast = checkLastMove();

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
            return -1;
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
            return looper;
        }
    }

    private void walkNorth() {
        // Get the next position
        posCur.y = posCur.y + 1;

        // Update canvas
        if (!isUnlockedDoorTile(posCur.x,posCur.y)) {
            paintTile(posCur.x,posCur.y,Tileset.AVATAR);
        }
        //world[posNew.x + 1][posNew.y] = Tileset.WALL;
        //world[posNew.x - 1][posNew.y] = Tileset.WALL;
    }

    private void walkWest() {
        // Get the next position
        posCur.x = posCur.x - 1;

        // Update canvas
        //world[posNew.x][posNew.y + 1] = Tileset.WALL;
        if (!isUnlockedDoorTile(posCur.x,posCur.y)) {
            paintTile(posCur.x,posCur.y,Tileset.AVATAR);
        }
        //world[posNew.x][posNew.y - 1] = Tileset.WALL;
    }

    private void walkSouth() {
        // Get the next position
        posCur.y = posCur.y - 1;

        // Update canvas
        //world[posNew.x + 1][posNew.y] = Tileset.WALL;
        if (!isUnlockedDoorTile(posCur.x,posCur.y)) {
            paintTile(posCur.x,posCur.y,Tileset.AVATAR);
        }
        //world[posNew.x - 1][posNew.y] = Tileset.WALL;
    }

    private void walkEast() {
        // Get the next position
        posCur.x = posCur.x + 1;

        // Update canvas
        //world[posNew.x][posNew.y + 1] = Tileset.WALL;
        if (!isUnlockedDoorTile(posCur.x,posCur.y)) {
            paintTile(posCur.x,posCur.y,Tileset.AVATAR);
        }
        //world[posNew.x][posNew.y - 1] = Tileset.WALL;
    }
}
