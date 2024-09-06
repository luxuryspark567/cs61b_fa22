package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Direction.getShiftPosition;
import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class TileUtils {


    public static boolean isInCanvas(Position pos) {
        return pos.x >= 0 && pos.y >= 0 && pos.x < WIDTH && pos.y < HEIGHT;
    }

    public static boolean isTileType(Position pos, TETile type, TETile[][] world) {
        if (isInCanvas(pos)) {
            return world[pos.x][pos.y] == type;
        }
        else {
            return false; // out of canvas, then it is surely not a "type"
        }
    }

    public static boolean isUnlockedDoorTile(int x, int y, TETile[][] world) {
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT) {
            return world[x][y] == Tileset.UNLOCKED_DOOR;
        }
        else {
            return true; // if out of bound, should return true, because it means this direction is not doable
        }
    }

    public static boolean isNullTile(Position pos, TETile[][] world) {
        if (isInCanvas(pos)) {
            return world[pos.x][pos.y] == null;
        }
        else {
            // already reach the end, and there is no Tile there, then this it definitely not a wanted Tile
            return false;
        }
    }

    public static void paintWallTile(Position pos, TETile[][] world) {

        if (isTileType(pos, null, world))
            paintTile(pos, Tileset.WALL, world);
    }

    public static void paintSideWall(Position pos, Direction dir, TETile[][] world){
        if (dir == Directionset.SOUTH || dir == Directionset.NORTH) {
            paintWallTile(getShiftPosition(pos, Directionset.WEST), world);
            paintWallTile(getShiftPosition(pos, Directionset.EAST), world);
        }
        else { //(dir == Directionset.WEST || dir == Directionset.EAST)
            paintWallTile(getShiftPosition(pos, Directionset.SOUTH), world);
            paintWallTile(getShiftPosition(pos, Directionset.NORTH), world);
        }
    }

    public static void paintTile(Position pos, TETile type, TETile[][] world) {
        if (isInCanvas(pos)) {
            world[pos.x][pos.y] = type;
        }
        else {
            System.out.println("out of canvas!!!");
        }
    }
}
