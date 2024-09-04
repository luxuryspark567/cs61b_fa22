package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.*;
import freemarker.core.NonExtendedHashException;
import freemarker.core.NonNodeException;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Engine {
    TERenderer ter = new TERenderer();
    EdgeWeightedGraph meg;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int ROOM_NUM = 15;
    public static final int ROOM_WIDTH_LIMIT = 20;
    public static final int ROOM_HEIGHT_LIMIT = 10;
    private static final long SEED = 19873;

    private static final int LOOP_LIMIT = 1000;
    //private static final long SEED = 98733;
    private static final Random RANDOM = new Random(SEED);

    //public void engine() {
    //}
    // size of an object
    private class size {
        int width;
        int height;

        public size () {
            this.width = 0;
            this.height = 0;
        }
        public size (int w, int h) {
            this.width = w;
            this.height = h;
        }
    }

    // position of an object on canvas
    private class position {
        int x;
        int y;

        public position() {
            this.x = 0;
            this.y = 0;
        }
        public position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof position){
                position pos = (position)o;
                return this.x == pos.x && this.y == pos.y;
            }
            return false;
        }
    }
/*
    private static class DoorDirection {
        private final String type;
        private final int value;

        public DoorDirection(String c, int v) {
            this.type = c;
            this.value = v;
        }
    }
    private static class DoorDirectionSet {
        public static final DoorDirection NORTH = new DoorDirection("north", 0);
        public static final DoorDirection WEST = new DoorDirection("west", 1);
        public static final DoorDirection SOUTH = new DoorDirection("south", 2);
        public static final DoorDirection EAST = new DoorDirection("east", 3);
    }
*/
    private static class DoorType {
        private final String type;

        public DoorType(String c) {
            this.type = c;
        }
    }
    private static class Doorset {
        public static final DoorType INVISIBLE = new DoorType("invisible");
        public static final DoorType CLOSE = new DoorType("close");
        public static final DoorType OPEN = new DoorType("open");
        public static final DoorType INVALID = new DoorType("invalid");
    }

    private static class DoorPairType {
        private final String type;

        public DoorPairType(String c) {
            this.type = c;
        }
    }
    private static class DoorPairSet {
        public static final DoorPairType SAME = new DoorPairType("same direction");
        public static final DoorPairType CROSS = new DoorPairType("cross");
        public static final DoorPairType FACE_TO_FACE = new DoorPairType("face to face");
        public static final DoorPairType BACK_TO_BACK = new DoorPairType("back to back");
    }
    private class door{
        room pr;
        position pos; // position of a door
        DoorType type; // is it a closed door or open door, or door to a hallway;

        boolean[] expandable;
        //DoorDirection direct;

        public door() {
            this.pr = null;
            this.pos = null;
            this.type = null;
            this.expandable = null;
        }
        public door(room r, position pos, DoorType type, boolean north, boolean west, boolean south, boolean east) {
            this.pr = r;
            this.pos = pos;
            this.type = type;
            this.expandable = new boolean[4];
            this.expandable[0] = north;
            this.expandable[1] = west;
            this.expandable[2] = south;
            this.expandable[3] = east;
        }
    }

    private class turn {
        position pos;
        int totalWidth;
        int totalHeight;
        boolean[] expandable;

        public turn(int w, int h, position pos, boolean north, boolean west, boolean south, boolean east) {
            this.totalWidth = w;
            this.totalHeight = h;
            this.pos = pos;
            this.expandable = new boolean[4];
            this.expandable[0] = north;
            this.expandable[1] = west;
            this.expandable[2] = south;
            this.expandable[3] = east;
        }
    }

    private class room {
        position pos; // position
        size si; //size

        door[] doors; // north door, west door, south door and east door

        public room(position pos, size si) {
            this.pos = pos;
            this.si = si;
            this.doors = new door[4];
            this.doors[0] = new door(this, new position(pos.x + (int)(si.width / 2), pos.y + si.height), Doorset.INVISIBLE, true, false, false, false);
            this.doors[1] = new door(this, new position(pos.x + (int)(si.width / 2), pos.y), Doorset.INVISIBLE, false, true, false, false);
            this.doors[2] = new door(this, new position(pos.x, pos.y + (int)(si.height / 2)), Doorset.INVISIBLE, false, false, true, false);
            this.doors[3] = new door(this, new position(pos.x + si.width, pos.y + (int)(si.height / 2)), Doorset.INVISIBLE, false, false, false, true);
        }
    }

    // used to connect two rooms
    private class hallway {
        door src;
        door dst;

        int weight;

        public hallway(door d1, door d2) {
            // get most adjacent doors for two rooms, suppose the answer is r1.sd and r2.ed
            src = d1;
            dst = d2;
        }
    }
    private position getRandomPosition() {
        position ret = new position();
        ret.x = RandomUtils.uniform(RANDOM, WIDTH - 3);
        ret.y = RandomUtils.uniform(RANDOM, HEIGHT - 3);
        return ret;
    }

    private size getRandomSize() {
        size ret = new size();
        ret.width = RandomUtils.uniform(RANDOM, 3, ROOM_WIDTH_LIMIT);
        ret.height = RandomUtils.uniform(RANDOM, 3, ROOM_HEIGHT_LIMIT);
        return ret;
    }

    // generate a room:
    // 1, it overlaps existed objects, should return false;
    // 2, if not, create a room and return true.
    private room generateRoom(position p, size s, TETile[][] world) {
        //check if room location is out of canvas
        if (p.x < 0 || p.y < 0 || p.x >= WIDTH || p.y >= HEIGHT) {
            return null;
        }
        if ((p.x + s.width) >= WIDTH || (p.y + s.height) >= HEIGHT) {
            return null;
        }

        //check if the size is wrong, not able to create a room

        if (s.width < 3 || s.height < 3) {
            return null;
        }

        //check if overlaps
        for (int i = 0; i <= s.width; i++) {
            for (int j = 0; j <= s.height; j++) {
                int localX = p.x + i;
                int localY = p.y + j;

                if (world[localX][localY] != null) {
                    return null; // overlaps
                }
            }
        }

        //generate room
        // create wall
        for (int i = 0; i <= s.width; i++) {
            world[p.x + i][p.y] = Tileset.WALL;
            world[p.x + i][p.y + s.height] = Tileset.WALL;
        }
        for (int j = 0; j <= s.height; j++) {
            world[p.x][p.y + j] = Tileset.WALL;
            world[p.x + s.width][p.y + j] = Tileset.WALL;
        }
        //create tile
        for (int i = 1; i < s.width; i++) {
            for (int j = 1; j < s.height; j++) {
                int localX = p.x + i;
                int localY = p.y + j;
                world[localX][localY] = Tileset.FLOOR;
            }
        }

        return new room(p,s);
    }

    //get the distance of two doors
    private double distanceOfDoors(door d1, door d2) {
        return Math.sqrt(Math.pow(d1.pos.x - d2.pos.x, 2) + Math.pow(d1.pos.y - d2.pos.y, 2));
    }

    private position checkIntersectableInOneDirection(turn t1, turn t2) {
        return null;
    }
    /*
    private class turn {
        position pos;

        boolean[] expandable;

        public turn(position pos, boolean north, boolean west, boolean south, boolean east) {
            this.pos = pos;
            this.expandable = new boolean[4];
            this.expandable[0] = north;
            this.expandable[1] = west;
            this.expandable[2] = south;
            this.expandable[3] = east;
        }
    }
    */
    private turn rotateTurn(turn t, int num) {
        // rotate coordinate
        int width;
        int height;
        position pos;
        switch (num) {
            case 1:
                pos = new position(t.pos.y, t.totalWidth - t.pos.x);
                width = t.totalHeight;
                height = t.totalWidth;
                break;
            case 2:
                pos = new position(t.totalWidth - t.pos.x, t.totalHeight - t.pos.y);
                width = t.totalWidth;
                height = t.totalHeight;
                break;
            case 3:
                pos = new position(t.totalHeight - t.pos.y, t.pos.x);
                width = t.totalHeight;
                height = t.totalWidth;
                break;
            default:
                pos = new position(t.pos.x, t.pos.y);
                width = t.totalWidth;
                height = t.totalHeight;
                break;
        };

        // rotate expand direction
        boolean newNorth = t.expandable[Math.floorMod(num,     4)];
        boolean newWest  = t.expandable[Math.floorMod(1 + num, 4)];
        boolean newSouth = t.expandable[Math.floorMod(2 + num, 4)];
        boolean newEast  = t.expandable[Math.floorMod(3 + num, 4)];

        return new turn(width, height, pos, newNorth, newWest, newSouth, newEast);

    }

    // check t1 and t2 if they could be connected
    // and return the turn point if they could be connected by just one turn point.
    private turn checkIntersectable(turn t1, turn t2) {
        turn trans = null;
        // 1, calc how much to rotate north
        // rotate t1 direction to north; and rotate t2 accordingly
        for (int i = 0; i < 4; i++) {
            // find an expand direction of t1 which is not false
            if (t1.expandable[i]) {
                // 2, rotate north
                turn t1New = rotateTurn(t1, i);
                turn t2New = rotateTurn(t2, i);

                // 3, check if two turns could intersect after expand in expandable direction.
                // after rotation, t1 is always pointing to the NORTH!!!!!!!!
                for (int j = 0; j < 4; j++) {
                    if (t2New.expandable[j]) {
                        // find an expand direction of t2 which is not false
                        // check if there could be valid turn-point

                        // in the picture below, x is the turn-point
                        /*
                        * <case1>
                        *   t2-->   x
                        *
                        *           ^
                        *           |
                        *           t1
                        *
                        * <case2>
                        *           x     <--t2
                        *
                        *           ^
                        *           |
                        *           t1
                        * */
                        if ((t2New.expandable[3] && t1New.pos.x > t2New.pos.x && t1New.pos.y < t2New.pos.y) // case 1
                            || (t2New.expandable[1] && t1New.pos.x < t2New.pos.x && t1New.pos.y < t2New.pos.y)) { // case 2
                            trans = new turn(t1New.totalWidth, t1New.totalHeight, new position(t1New.pos.x, t2New.pos.y), false, true, true, true);
                        }
                    }
                }
                // 4, rotate the coordinate back to normal
                if (trans != null) {
                    return rotateTurn(trans,Math.floorMod(4 - i, 4));
                }
                else {
                    return null;
                }
            }
        }
        return null;
    }

    private turn transDoorToTurn(door d, int width, int height) {
        return new turn(width, height, d.pos, d.expandable[0], d.expandable[1], d.expandable[2], d.expandable[3]);
    }
    private turn getTurnPoint(door d1, door d2) {
        turn t1 = transDoorToTurn(d1, WIDTH, HEIGHT);
        turn t2 = transDoorToTurn(d2, WIDTH, HEIGHT);
        return checkIntersectable(t1, t2);
    }

    private void setPixelAfterCheckIfTile(int x, int y, TETile style, TETile[][] world) {
        if (x < WIDTH && y < HEIGHT) {
            if (world[x][y] != Tileset.FLOOR) {
                world[x][y] = style;
            }
        }
        else {
            System.out.println("out of canvas!!!!!");
        }

    }
    private void connectTurnPoints(turn t1, turn t2, TETile[][] world) {
        int dir;
        for (int i = 0; i < 4; i ++) {
            if (t1.expandable[i]) {
                dir = i;
                break;
            }
        }
        position posSrc = new position(t1.pos.x, t1.pos.y);
        position posDst = new position(t2.pos.x, t2.pos.y);

        int safeProofLooper = LOOP_LIMIT;

        while (safeProofLooper > 0 && !posSrc.equals(posDst)) {
            if (t1.expandable[0]) {
                setPixelAfterCheckIfTile(posSrc.x - 1, posSrc.y, Tileset.WALL, world);
                setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.FLOOR, world);
                setPixelAfterCheckIfTile(posSrc.x + 1, posSrc.y, Tileset.WALL, world);
                posSrc.y++;
            }
            else if (t1.expandable[1]) {
                setPixelAfterCheckIfTile(posSrc.x, posSrc.y + 1, Tileset.WALL, world);
                setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.FLOOR, world);
                setPixelAfterCheckIfTile(posSrc.x, posSrc.y - 1, Tileset.WALL, world);
                posSrc.x--;
            }
            else if (t1.expandable[2]) {
                setPixelAfterCheckIfTile(posSrc.x - 1, posSrc.y, Tileset.WALL, world);
                setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.FLOOR, world);
                setPixelAfterCheckIfTile(posSrc.x + 1, posSrc.y, Tileset.WALL, world);
                posSrc.y--;
            }
            else if (t1.expandable[3]) {
                setPixelAfterCheckIfTile(posSrc.x, posSrc.y + 1, Tileset.WALL, world);
                setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.FLOOR, world);
                setPixelAfterCheckIfTile(posSrc.x, posSrc.y - 1, Tileset.WALL, world);
                posSrc.x++;
            }

            safeProofLooper--;
        }

        // act if posSrc.equals(posDst)
        if (t1.expandable[0]) {
            setPixelAfterCheckIfTile(posSrc.x - 1, posSrc.y, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.FLOOR, world);
            setPixelAfterCheckIfTile(posSrc.x + 1, posSrc.y, Tileset.WALL, world);
            posSrc.y++;
        }
        else if (t1.expandable[1]) {
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y + 1, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.FLOOR, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y - 1, Tileset.WALL, world);
            posSrc.x--;
        }
        else if (t1.expandable[2]) {
            setPixelAfterCheckIfTile(posSrc.x - 1, posSrc.y, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.FLOOR, world);
            setPixelAfterCheckIfTile(posSrc.x + 1, posSrc.y, Tileset.WALL, world);
            posSrc.y--;
        }
        else if (t1.expandable[3]) {
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y + 1, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.FLOOR, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y - 1, Tileset.WALL, world);
            posSrc.x++;
        }

        // to close a tunnel
        if (t1.expandable[0]) {
            setPixelAfterCheckIfTile(posSrc.x - 1, posSrc.y, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x + 1, posSrc.y, Tileset.WALL, world);
            //posSrc.y++;
        }
        else if (t1.expandable[1]) {
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y + 1, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y - 1, Tileset.WALL, world);
            //posSrc.x--;
        }
        else if (t1.expandable[2]) {
            setPixelAfterCheckIfTile(posSrc.x - 1, posSrc.y, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x + 1, posSrc.y, Tileset.WALL, world);
            //posSrc.y--;
        }
        else if (t1.expandable[3]) {
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y + 1, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y, Tileset.WALL, world);
            setPixelAfterCheckIfTile(posSrc.x, posSrc.y - 1, Tileset.WALL, world);
            //posSrc.x++;
        }

    }
    // connect two doors and update the word
    private boolean connectDoors(door d1, door d2, TETile[][] world) {

        // 1, if d1 and d2 is directly connected
        if (d1.pos.x == d2.pos.x) {
            if (d1.expandable[0] && d2.expandable[2]) {
                // d1 is right down and d2 is right up
                // connect d1 and d2 directly
                connectTurnPoints(transDoorToTurn(d1, WIDTH, HEIGHT), transDoorToTurn(d2, WIDTH, HEIGHT), world);
                return true;
            }
            else if (d1.expandable[2] && d2.expandable[0]) {
                // d1 is right up and d2 is right down
                // connect d1 and d2 directly
                connectTurnPoints(transDoorToTurn(d1, WIDTH, HEIGHT), transDoorToTurn(d2, WIDTH, HEIGHT), world);
                return true;
            }
        }
        else if (d1.pos.y == d2.pos.y) {
            if (d1.expandable[1] && d2.expandable[3]) {
                // d1 is right right and d2 is right left
                // connect d1 and d2 directly
                connectTurnPoints(transDoorToTurn(d1, WIDTH, HEIGHT), transDoorToTurn(d2, WIDTH, HEIGHT), world);
                return true;
            }
            else if (d1.expandable[3] && d2.expandable[1]) {
                // d1 is right left and d2 is right right
                // connect d1 and d2 directly
                connectTurnPoints(transDoorToTurn(d1, WIDTH, HEIGHT), transDoorToTurn(d2, WIDTH, HEIGHT), world);
                return true;
            }
        }

        // 2, if d1 and d2 is not directly connected, check every direction, to search for a turn point
        // get turn point
        turn t = getTurnPoint(d1, d2);
        if (t != null) {
            // connect door1 to turn point
            connectTurnPoints(transDoorToTurn(d1, WIDTH, HEIGHT), t, world);
            // connect door2 to turn point
            connectTurnPoints(transDoorToTurn(d2, WIDTH, HEIGHT), t, world);
            return true;
        }
        /*
        else {
            // if one turn is not enough, should try another strategy in document
            // it seems enough to connect all rooms after first two connect strategy, so I don't implement this strategy.

        }
        */

        return false;
    }

    // generate a hallway between two doors
    private hallway generateHallway(room r1, room r2, TETile[][] world) {
        double dis = 65536.0;
        door d_src = null;
        door d_dst = null;

        // get the most adjacent pair of doors, and the door should be invisible to create a hallway;
        for (door d1: r1.doors) {
            for (door d2: r2.doors){
                // only invisible doors could make hallways
                if (d1.type == Doorset.INVISIBLE && d2.type == Doorset.INVISIBLE) {
                    double tmpDis = distanceOfDoors(d1, d2);
                    if (tmpDis < dis) {
                        dis = tmpDis;
                        d_src = d1;
                        d_dst = d2;
                    }
                }
            }
        }

        // debug
        /*
        if (d_src != null) {
            world[d_src.pos.x][d_src.pos.y] = Tileset.AVATAR;
            world[d_dst.pos.x][d_dst.pos.y] = Tileset.FLOWER;
        }
        return null;
         */

        // create a hallway which is only 1 brick wide
        if (d_src != null) {
            if (connectDoors(d_src, d_dst, world)) {
                d_src.type = Doorset.INVALID;
                d_dst.type = Doorset.INVALID;
                return new hallway(d_src, d_dst);
            }
            else {
                // could not connect straight, then reach out one pixel, and check if it is possible to set up a tunnel
                // 1, reach out d_src
                position pos = new position();

                // the original door, only one direction is true;
                boolean[] tmpExpandable1 = new boolean[4];
                boolean[] tmpExpandable2 = new boolean[4];
                if (d_src.expandable[0]) {//NORTH
                    pos.x = d_src.pos.x;
                    pos.y = d_src.pos.y + 1;
                    tmpExpandable1[0] = false;
                    tmpExpandable1[1] = false;
                    tmpExpandable1[2] = true;
                    tmpExpandable1[3] = false;

                    tmpExpandable2[0] = true;
                    tmpExpandable2[1] = true;
                    tmpExpandable2[2] = false;
                    tmpExpandable2[3] = true;
                }
                else if (d_src.expandable[1]) {//WEST
                    pos.x = d_src.pos.x - 1;
                    pos.y = d_src.pos.y;
                    tmpExpandable1[0] = false;
                    tmpExpandable1[1] = false;
                    tmpExpandable1[2] = false;
                    tmpExpandable1[3] = true;

                    tmpExpandable2[0] = true;
                    tmpExpandable2[1] = true;
                    tmpExpandable2[2] = true;
                    tmpExpandable2[3] = false;
                }
                else if (d_src.expandable[2]) {//SOUTH
                    pos.x = d_src.pos.x;
                    pos.y = d_src.pos.y - 1;
                    tmpExpandable1[0] = true;
                    tmpExpandable1[1] = false;
                    tmpExpandable1[2] = false;
                    tmpExpandable1[3] = false;

                    tmpExpandable2[0] = false;
                    tmpExpandable2[1] = true;
                    tmpExpandable2[2] = true;
                    tmpExpandable2[3] = true;
                }
                else if (d_src.expandable[3]) {//EAST
                    pos.x = d_src.pos.x + 1;
                    pos.y = d_src.pos.y;
                    tmpExpandable1[0] = false;
                    tmpExpandable1[1] = true;
                    tmpExpandable1[2] = false;
                    tmpExpandable1[3] = false;

                    tmpExpandable2[0] = true;
                    tmpExpandable2[1] = false;
                    tmpExpandable2[2] = true;
                    tmpExpandable2[3] = true;
                }

                // connect the original src door and the expanded door 1
                door d_src_expand1 = new door(d_src.pr, pos, Doorset.INVISIBLE, tmpExpandable1[0], tmpExpandable1[1], tmpExpandable1[2], tmpExpandable1[3]);
                boolean result1 = connectDoors(d_src, d_src_expand1, world);

                // connect the expanded door 2 and the dst
                door d_src_expand2 = new door(d_src.pr, pos, Doorset.INVISIBLE, tmpExpandable2[0], tmpExpandable2[1], tmpExpandable2[2], tmpExpandable2[3]);
                boolean result2 = connectDoors(d_src_expand2, d_dst, world);
                if (result1 && result2) {
                    d_src.type = Doorset.INVALID;
                    d_dst.type = Doorset.INVALID;
                    return new hallway(d_src_expand2, d_dst);
                }
                else {
                    return null;
                }
            }
        }
        else {
            return null;
        }
    }
    
    private double distanceOfRooms(room r1, room r2) {
        return Math.sqrt(Math.pow(r1.pos.x - r2.pos.x, 2) + Math.pow(r1.pos.y - r2.pos.y, 2));
    }

    private class valueRoomPair {
        room r1;
        room r2;
        
        public valueRoomPair(room r1, room r2) {
            this.r1 = r1;
            this.r2 = r2;
        }
    }
    
    private class distanceNode {
        
        double key;
        valueRoomPair value;
        
        public distanceNode(double dis, valueRoomPair vrp) {
            this.key = dis;
            this.value = vrp;
        }
    }
    
    public class nodeDistanceComparator implements Comparator<distanceNode> {

        @Override
        public int compare(distanceNode o1, distanceNode o2) {
            return (int)(o1.key - o2.key);
        }
    }
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        // parse the input

        // generate world
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];

        // 1, generate rooms
        int looper = 0;
        int looperLimit = LOOP_LIMIT;
        // roomArray is a lookup table;
        List<room> roomArray = new ArrayList<>();
        while (looper < ROOM_NUM && looperLimit > 0) {
            size s = getRandomSize();
            position p = getRandomPosition();
            room newRoom = generateRoom(p, s, finalWorldFrame);
            if (newRoom != null) {
                looper++;
                roomArray.add(newRoom);
                System.out.println("Create a room success!");
            }
            else {
                System.out.println("Create a room fail!");
            }
            looperLimit--;
        }
        
        // save rooms to a graph with edge;
        meg = new EdgeWeightedGraph(roomArray.size());
        for (room r:roomArray) {
            meg.adj(roomArray.indexOf(r));
        }

        // 1, calc a distance, and use pq to save the distance, because a MinPQ will always O(1) to get
        // minimum value
        MinPQ<distanceNode> mpq = new MinPQ<>(new nodeDistanceComparator());
        for (int i = 0; i < roomArray.size(); i++) {
            for (int j = i + 1; j < roomArray.size(); j++) {
                
                // calc distance and save to a MPQ;
                double dis = distanceOfRooms(roomArray.get(i), roomArray.get(j));
                valueRoomPair vrp = new valueRoomPair(roomArray.get(i), roomArray.get(j));
                mpq.insert(new distanceNode(dis, vrp));
            }
        }
        //Debug
        //System.out.println(mpq.toString());
        WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(roomArray.size());

        int safeProofLooper = LOOP_LIMIT;

        // 2, generate hallways
        while (wqu.count() > 1 && safeProofLooper > 0 && (!mpq.isEmpty())) {//not all rooms are connected

            // 1, pick the closest two rooms to make a hallway
            distanceNode dsNode = mpq.delMin();

            // 2, are they already connected?
            if (!wqu.connected(roomArray.indexOf(dsNode.value.r1), roomArray.indexOf(dsNode.value.r2))) {
                // 3, connect them
                hallway hw = generateHallway(dsNode.value.r1, dsNode.value.r2, finalWorldFrame);

                if (hw != null) {
                    //union them
                    wqu.union(roomArray.indexOf(dsNode.value.r1), roomArray.indexOf(dsNode.value.r2));
                    meg.addEdge(new Edge(roomArray.indexOf(hw.src.pr), roomArray.indexOf(hw.dst.pr), hw.weight));
                }
            }

            safeProofLooper--;
        }

        // 3, generate a door
        safeProofLooper = LOOP_LIMIT;
        while(safeProofLooper > 0) {

            // get a random door of a random room
            int roomNum = RandomUtils.uniform(RANDOM, roomArray.size());
            int doorNum = RandomUtils.uniform(RANDOM, 4);

            // check if this invisible door could be open, break out if opened
            // get door
            door d = roomArray.get(roomNum).doors[doorNum];

            // TODO: this not totally right, because a door might be in a place not reachable!!
            if (d.type == Doorset.INVISIBLE && finalWorldFrame[d.pos.x][d.pos.y] == Tileset.WALL) {
                d.type = Doorset.CLOSE;
                finalWorldFrame[d.pos.x][d.pos.y] = Tileset.LOCKED_DOOR;
                break;
            }
            safeProofLooper--;
        }


        //debug, fill empty
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (finalWorldFrame[i][j] == null) {
                    finalWorldFrame[i][j] = Tileset.NOTHING;
                }
            }
        }

        return finalWorldFrame;
    }
    public static void main(String[] s) {
        // get engine, it would generate a world save in 2D array
        Engine eng = new Engine();
        TETile[][] tr = eng.interactWithInputString("n1234s");

        // initial default size render and print the world on that canvas (renderer)
        eng.ter.initialize(WIDTH,HEIGHT);
        eng.ter.renderFrame(tr);
    }
}
