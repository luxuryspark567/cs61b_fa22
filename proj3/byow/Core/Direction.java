package byow.Core;

import java.awt.*;
import java.io.Serializable;

import static byow.Core.Engine.DIRECTION_NUM;

public class Direction implements Serializable {
    private final String description;

    public Direction(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return this.description;
    }
    public static void setFalseBoolArrayByDirection(boolean[] dirBool, Direction dir) {
        if (dir == null) {
            return;
        }
        if (dir == Directionset.NORTH) {
            dirBool[0] = false;
        }
        else if (dir == Directionset.WEST) {
            dirBool[1] = false;
        }
        else if (dir == Directionset.SOUTH) {
            dirBool[2] = false;
        }
        else {//if (dir == Directionset.EAST) {
            dirBool[3] = false;
        }
    }

    public static void setTrueBoolArrayByDirection(boolean[] dirBool, Direction dir) {
        if (dir == null) {
            return;
        }
        if (dir == Directionset.NORTH) {
            dirBool[0] = true;
        }
        else if (dir == Directionset.WEST) {
            dirBool[1] = true;
        }
        else if (dir == Directionset.SOUTH) {
            dirBool[2] = true;
        }
        else {//if (dir == Directionset.EAST) {
            dirBool[3] = true;
        }
    }

    public static Direction getDirectionByIndex(int d) {
        return switch (d) {
            case 0 -> Directionset.NORTH;
            case 1 -> Directionset.WEST;
            case 2 -> Directionset.SOUTH;
            case 3 -> Directionset.EAST;
            default -> null;
        };
    }

    public static void shiftPosition(Position pos, Direction dir) {
        if (pos == null) {
            return;
        }

        if (Directionset.NORTH == dir) {
            pos.setY(pos.getY() + 1);
        }
        else if (Directionset.WEST == dir) {
            pos.setX(pos.getX() - 1);
        }
        else if (Directionset.SOUTH == dir) {
            pos.setY(pos.getY() - 1);
        }
        else {//if (Directionset.EAST == dir) {
            pos.setX(pos.getX() + 1);
        }
    }

    public static boolean[] getBooleanArrayFromDir(Direction dir) {
        if (dir == null) {
            return new boolean[]{true, true, true, true};
        }
        if (dir == Directionset.NORTH) {
            return new boolean[]{true, false, false, false};
        }
        else if (dir == Directionset.WEST) {
            return new boolean[]{false, true, false, false};
        }
        else if (dir == Directionset.SOUTH) { //(diffX < 0 && diffY >= 0)
            return new boolean[]{false, false, true, false};
        }
        else { //(lastMoveDir == Directionset.EAST
            return new boolean[]{false, false, false, true};
        }
    }

    public static boolean isTurned90Degree(Direction lastDir, Direction curDir) {
        int lastIndexDir = getIndexFromDir(lastDir);
        int curIndexDir = getIndexFromDir(curDir);
        int absDiff = Math.abs(curIndexDir - lastIndexDir);

        return absDiff == 1 || absDiff == 3;
    }

    public static boolean isTurned180Degree(Direction lastDir, Direction curDir) {
        int lastIndexDir = getIndexFromDir(lastDir);
        int curIndexDir = getIndexFromDir(curDir);
        int absDiff = Math.abs(curIndexDir - lastIndexDir);

        return absDiff == 2;
    }

    public static int getIndexFromDir(Direction dir) {
        if (dir == null) {
            return -1;
        }
        if (dir == Directionset.NORTH) {
            return 0;
        }
        else if (dir == Directionset.WEST) {
            return 1;
        }
        else if (dir == Directionset.SOUTH) { //(diffX < 0 && diffY >= 0)
            return 2;
        }
        else { //(lastMoveDir == Directionset.EAST
            return 3;
        }
    }

    public static Direction getRevertDir(Direction dir) {
        if (dir == null) {
            return null;
        }
        if (dir == Directionset.NORTH) {
            return Directionset.SOUTH;
        }
        else if (dir == Directionset.WEST) {
            return Directionset.EAST;
        }
        else if (dir == Directionset.SOUTH) { //(diffX < 0 && diffY >= 0)
            return Directionset.NORTH;
        }
        else { //(lastMoveDir == Directionset.EAST
            return Directionset.WEST;
        }
    }

    public static Position getShiftPosition(Position pos, Direction dir) {

        if (pos == null) {
            return null;
        }

        if (Directionset.NORTH == dir) {
            return new Position(pos.getX(), pos.getY() + 1);
        }
        else if (Directionset.WEST == dir) {
            return new Position(pos.getX() - 1, pos.getY());
        }
        else if (Directionset.SOUTH == dir) {
            return new Position(pos.getX(), pos.getY() - 1);
        }
        else if (Directionset.EAST == dir) {
            return new Position(pos.getX() + 1, pos.getY());
        }
        else {
            return Position.copyOf(pos);
        }
    }

    public static boolean[] mergeDirection(boolean[] dir1, boolean[] dir2) {
        boolean[] dirRet = new boolean[DIRECTION_NUM];
        for (int i = 0; i < DIRECTION_NUM; i++) {
            dirRet[i] = dir1[i] && dir2[i];
        }
        return dirRet;
    }

    public static int getDirNum(boolean[] dir) {
        int counter = 0;
        for (int i = 0; i < DIRECTION_NUM; i++) {
            if (dir[i]) {
                counter++;
            }
        }
        return counter;
    }
}