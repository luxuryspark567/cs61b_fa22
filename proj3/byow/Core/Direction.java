package byow.Core;

import java.awt.*;

public class Direction {
    private final String description;

    public Direction(String description) {
        this.description = description;
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
}