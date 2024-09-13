package byow.Utils;

import byow.Articles.Lamp;
import byow.Articles.Room;
import byow.Attribute.Direction;
import byow.Attribute.Directionset;
import byow.Attribute.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

import static byow.Attribute.Direction.getShiftPosition;
import static byow.Core.Engine.*;
import static byow.Core.Main.gameState;

public class TileUtils {

    public static class CanvasCoordinate {
        private double x;
        private double y;

        public CanvasCoordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }


    public static boolean isInCanvas(int x, int y) {
        return x >= 0 && y >= 0 && x < CANVAS_WIDTH && y < CANVAS_HEIGHT;
    }

    // switch a tile world position to a canvas coordinate
    public static CanvasCoordinate getCanvasCoordFromTilePos(Position pos) {
        int x = pos.getX() + X_OFF;
        int y = pos.getY() + Y_OFF;
        if (isInCanvas(x, y)) {
            return new CanvasCoordinate(x, y);
        }
        else {
            return null;
        }
    }

    // switch a canvas coordinate to a tile world position
    public static Position getTilePosFromCanvasCoord(CanvasCoordinate cc) {

        int x = (int)cc.getX() - X_OFF;
        int y = (int)cc.getY() - Y_OFF;

        Position pos = new Position(x, y);

        if (isInTileWorld(pos)) {
            return pos;
        }
        else {
            return null;
        }
    }

    public static int getCanvasWidth() {
        return CANVAS_WIDTH;
    }

    public static int getCanvasHeight() {
        return CANVAS_HEIGHT;
    }

    public static int getTileWorldWidth() {
        return WIDTH;
    }

    public static int getTileWorldHeight() {
        return HEIGHT;
    }

    public static boolean isInTileWorld(Position pos) {
        if (pos == null) {
            return false;
        }
        return pos.getX() >= 0
                && pos.getY() >= 0
                && pos.getX() < WIDTH
                && pos.getY() < HEIGHT;
    }
    public static boolean isOutOffWorldNorth(Position pos) {
        if (pos == null) {
            return false;
        }
        return pos.getY() >= HEIGHT;
    }

    public static boolean isOutOffWorldWest(Position pos) {
        if (pos == null) {
            return false;
        }
        return pos.getX() < 0;
    }

    public static boolean isOutOffWorldSouth(Position pos) {
        if (pos == null) {
            return false;
        }
        return pos.getY() < 0;
    }

    public static boolean isOutOffWorldEast(Position pos) {
        if (pos == null) {
            return false;
        }
        return pos.getX() >= WIDTH;
    }

    public static boolean isTileType(Position pos, TETile type, TETile[][] world) {
        if (isInTileWorld(pos)) {
            if (type == null) {
                return world[pos.getX()][pos.getY()] == null;
            }
            else {
                return type.equals(world[pos.getX()][pos.getY()]);
            }
        }
        else {
            return false; // out of canvas, then it is surely not a "type"
        }
    }

    public static boolean isNullTile(Position pos, TETile[][] world) {
        if (isInTileWorld(pos)) {
            return world[pos.getX()][pos.getY()] == null;
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
        if (isInTileWorld(pos)) {
            world[pos.getX()][pos.getY()] = type;
        }
        else {
            System.out.println("out of canvas!!!");
        }
    }

    public static CanvasCoordinate getMouseCoord() {

        return new CanvasCoordinate(StdDraw.mouseX(), StdDraw.mouseY());
    }
/*
    public static String getTileDescription(Position pos, TETile[][] world) {

        if (isInTileWorld(pos)) {
            if (world[pos.getX()][pos.getY()] == null) {
                return null;
            }
            return world[pos.getX()][pos.getY()].description();
        }
        return null;
    }

 */
    private static int getLineDistance(Position pos1, Position pos2) {
        return Math.max(Math.abs(pos1.getX() - pos2.getX()), Math.abs(pos1.getY() - pos2.getY()));
    }

    private static Color getNewColor(Color c, int decrease) {
        int newR = (c.getRed() - decrease);
        if (newR < 0) {
            newR = 0;
        }

        int newG = (c.getGreen() - decrease);
        if (newG < 0) {
            newG = 0;
        }

        int newB = (c.getBlue() - decrease);
        if (newB < 0) {
            newB = 0;
        }

        return new Color(newR, newG, newB);
    }
    public static void paintLampInRoom(Lamp lamp, TETile[][] world) {
        //calc the distance of a tile from the lamp in a room, and set the lumen according to the distance
        int testLooper = 0;
        System.out.println("start to paint lamp");
        for (Position pos: lamp.getRoom()) {
            System.out.println(testLooper++);
            int dis = getLineDistance(pos, lamp.getPosition());
            // 1 distance correspond to 10 points loss in R & G & B
            TETile tile = world[pos.getX()][pos.getY()];
            if (tile != null) {
                world[pos.getX()][pos.getY()] = TETile.TETileBackGround(tile, getNewColor(Color.GRAY, dis * 20));
            }
        }
    }

    public static void paintAllLamps() {
        for (Room r: gameState.roomLut) {
            Lamp lamp = r.getLamp();
            if (lamp != null)
            {
                paintTile(lamp.getPosition(), Tileset.LAMP, gameState.world);
                TileUtils.paintLampInRoom(lamp, gameState.world);// paint the lamp and the room
            }
        }
    }

}
