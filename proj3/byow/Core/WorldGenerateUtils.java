package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.Comparator;
//import static byow.Core.Main.gameState;
import static byow.Core.Engine.*;
import static byow.Core.TileUtils.*;

public class WorldGenerateUtils {

    private DiggerAvatar digger;

    public WorldGenerateUtils() {
    }

    private Position getRandomPosition() {
        Position ret = new Position();
        ret.setX(RandomUtils.uniform(RANDOM, WIDTH));
        ret.setY(RandomUtils.uniform(RANDOM, HEIGHT));
        return ret;
    }

    private Size getRandomSize() {
        Size ret = new Size();
        // random is [a,b), but when we say a room is size w x h, means that height is [1, h]
        // and here size is limit to MIN to MAX, [MIN, MAX] which is [MIN, MAX + 1) for the function.
        ret.w = RandomUtils.uniform(RANDOM, ROOM_WIDTH_MIN, ROOM_WIDTH_MAX + 1);
        ret.h = RandomUtils.uniform(RANDOM, ROOM_HEIGHT_MIN, ROOM_HEIGHT_MAX + 1);
        return ret;
    }

    public void generateRooms(GameState gameState) {
        //gameState.roomLut = new ArrayList<>(ROOM_NUM);

        int looper = 0;
        int looperLimit = LOOP_LIMIT;
        while (looper < ROOM_NUM && looperLimit > 0) {
            Room newRoom = generateRoom(gameState.world);
            if (newRoom != null) {
                looper++;
                gameState.roomLut.add(newRoom);
                System.out.println("Create a room success!");
            }
            //else {
                //System.out.println("Create a room fail!");
            //}
            looperLimit--;
        }

        // save rooms to Graph;
        //for (Room r:gameState.roomLut) {
        //    gameState.ewg.adj(gameState.roomLut.indexOf(r));
        //}
    }

    private Door checkAndGetDoor(Position pos, Direction dir, TETile[][] world) {
        // if the door is besides wall, then this door is invalid
        Position posShift1 = Direction.getShiftPosition(pos, dir);
        Position posShift2 = Direction.getShiftPosition(posShift1, dir);

        if (isInCanvas(posShift1) && isInCanvas(posShift2)) {
            // created a wall at least 2 tiles away from the edge, which is OK.
            if (isTileType(posShift1, Tileset.WALL, world)) {
                System.out.println("Create a door facing a wall!");
                return null;
            }
            else {
                return new Door(pos, null, null, dir);
            }
        }
        else{
            System.out.println("Created a door at the edge!");
            return null;
        }
    }
    private Door generateRandomDoor(Position posRoom,  Size sizeRoom, TETile[][] world) {

        int looperLimit = LOOP_LIMIT;
        Door newDoor = null;
        while (newDoor == null && looperLimit > 0) {
            // get door side
            Direction side = Direction.getDirectionByIndex(RandomUtils.uniform(RANDOM, 4));
            if (side == Directionset.NORTH) {
                // pick a random position on the north side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.w - 1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.getX() + index, posRoom.getY() + sizeRoom.h - 1, WIDTH, HEIGHT);
                newDoor = checkAndGetDoor(doorPos, Directionset.NORTH, world);
            }
            else if (side == Directionset.WEST) {
                // pick a random position on the west side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.h - 1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.getX(), posRoom.getY() + index, WIDTH, HEIGHT);
                newDoor = checkAndGetDoor(doorPos, Directionset.WEST, world);
            }
            else if (side == Directionset.SOUTH) {
                // pick a random position on the south side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.w -1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.getX() + index, posRoom.getY(), WIDTH, HEIGHT);
                newDoor = checkAndGetDoor(doorPos, Directionset.SOUTH, world);
            }
            else if (side == Directionset.EAST) {
                // pick a random position on the east side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.h - 1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.getX() + sizeRoom.w - 1, posRoom.getY() + index, WIDTH, HEIGHT);
                newDoor = checkAndGetDoor(doorPos, Directionset.EAST, world);
            }
            looperLimit--;
        }

        //System.out.println(looperLimit);
        return newDoor;
    }
    public void generateDoors(GameState gameState) {
        for (Room r: gameState.roomLut) {
            // generate a random door
            Door door = generateRandomDoor(r.getPosition(), r.getSize(), gameState.world);
            if (door != null)
            {
                r.setDoor(door);
                //world[door.getPosition().getX()][door.getPosition().getY()] = Tileset.UNLOCKED_DOOR;

                // generate locked or unlocked randomly
                int index = RandomUtils.uniform(RANDOM, 2);
                if (index == 0) {
                    paintTile(door.getPosition(), Tileset.UNLOCKED_DOOR, gameState.world);
                    gameState.tmDB.put(door.getPosition(), door); // add to database
                    //gameState.doorLut.add(door);
                }
                else {
                    paintTile(door.getPosition(), Tileset.LOCKED_DOOR, gameState.world);
                    gameState.tmDB.put(door.getPosition(), door);// add to database
                    //gameState.doorLut.add(door);
                }

            }
        }

    }

    public void generateHallways(GameState gameState) {

        MinPQ<distanceNode> mpq = getRoomDistanceMPQ(gameState);

        // create a digger to dig tunnels
        this.digger = new DiggerAvatar(gameState);

        WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(gameState.roomLut.size());

        int safeProofLooper = LOOP_LIMIT;
        //int safeProofLooper = 2;
        // 2, generate hallways
        while (wqu.count() > 1 && safeProofLooper > 0 && (!mpq.isEmpty())) {//not all rooms are connected

            // 1, pick the closest two rooms to make a hallway
            distanceNode dsNode = mpq.delMin();

            // 2. do them all have door?
            if (dsNode.value.r1.getDoor() != null && dsNode.value.r2.getDoor() != null )
            {
                // 2, are they already connected?
                if (!wqu.connected(gameState.roomLut.indexOf(dsNode.value.r1), gameState.roomLut.indexOf(dsNode.value.r2))) {
                    //if (dsNode.value.r1.getDoor().getPosition().y == 38
                    //    || dsNode.value.r2.getDoor().getPosition().y == 38) {
                    //    System.out.println("Gocha!!!");
                    //}
                    // 3, connect them
                    Hallway hw = connectRoomsWithHallway(dsNode.value.r1, dsNode.value.r2);
                    //if (hw == null) {
                    //    hw = connectRoomsWithHallway(dsNode.value.r2, dsNode.value.r1);
                    //}
                    if (hw != null) {
                        //union them
                        wqu.union(gameState.roomLut.indexOf(dsNode.value.r1), gameState.roomLut.indexOf(dsNode.value.r2));
                        //Edge e = new Edge(gameState.roomLut.indexOf(dsNode.value.r1), gameState.roomLut.indexOf(dsNode.value.r2), hw.getWeight());

                        // update edge to door.
                        //hw.getSrc().setEdge(e);
                        //hw.getDst().setEdge(e);
                        ///hw.getSrc().setHallway(hw);
                        //hw.getDst().setHallway(hw);

                        //gameState.ewg.addEdge(e);
                    }
                }
            }
            safeProofLooper--;
        }

        //System.out.println(mpq);
        //System.out.println(wqu.count());
    }

    // generate a hallway between two doors
    private Hallway connectRoomsWithHallway(Room srcRoom, Room dstRoom) {
        //src to dst
        Door srcDoor = srcRoom.getDoor();
        Door dstDoor = dstRoom.getDoor();

        // arrange a digging job to the digger
        this.digger.arrangeDiggingJob(srcDoor, dstDoor);
        return this.digger.digATunnel();
    }

    // generate a room:
    // 1, it overlaps existed objects, should return false;
    // 2, if not, create a room and return true.
    private Room generateRoom(TETile[][] world) {
        Position pos = getRandomPosition();
        Size size = getRandomSize();
        //check if room location is out of canvas
        if ((pos.getX() + size.w - 1) > (WIDTH - 1) || (pos.getY() + size.h - 1) > (HEIGHT - 1)) {
            return null;
        }
        //check if overlaps
        for (int i = 0; i < size.w; i++) {
            for (int j = 0; j < size.h; j++) {
                int localX = pos.getX() + i;
                int localY = pos.getY() + j;

                if (world[localX][localY] != null) {
                    return null; // overlaps
                }
            }
        }

        // reach here means it is a valid room

        // paint the room on the canvas
        // create wall
        for (int i = 0; i < size.w; i++) {
            world[pos.getX() + i][pos.getY()] = Tileset.WALL;
            world[pos.getX() + i][pos.getY() + size.h - 1] = Tileset.WALL;
        }

        for (int j = 0; j < size.h; j++) {
            world[pos.getX()][pos.getY() + j] = Tileset.WALL;
            world[pos.getX() + size.w - 1][pos.getY() + j] = Tileset.WALL;
        }
        //create tile
        for (int i = 1; i < size.w - 1; i++) {
            for (int j = 1; j < size.h - 1; j++) {
                int localX = pos.getX() + i;
                int localY = pos.getY() + j;
                world[localX][localY] = Tileset.FLOOR;
            }
        }

        return new Room(pos,size);
    }

    // don't care about the room distance, room distance is measured by door distance.
    private double distanceOfRooms(Room r1, Room r2) {
        return Math.sqrt(Math.pow(r1.getDoor().getPosition().getX()
                - r2.getDoor().getPosition().getX(), 2)
                + Math.pow(r1.getDoor().getPosition().getY()
                - r2.getDoor().getPosition().getY(), 2));
    }

    private static class valueRoomPair {
        Room r1;
        Room r2;
        public valueRoomPair(Room r1, Room r2) {
            this.r1 = r1;
            this.r2 = r2;
        }
    }

    private static class distanceNode {
        double key;
        valueRoomPair value;

        public distanceNode(double dis, valueRoomPair vrp) {
            this.key = dis;
            this.value = vrp;
        }
    }

    private static class nodeDistanceComparator implements Comparator<distanceNode> {
        @Override
        public int compare(distanceNode o1, distanceNode o2) {
            return (int)(o1.key - o2.key);
        }
    }

    private MinPQ<distanceNode> getRoomDistanceMPQ (GameState gameState) {
        // 1, calc a distance, and use pq to save the distance, because a MinPQ will always O(1) to get
        // minimum value
        MinPQ<distanceNode> mpq = new MinPQ<>(new nodeDistanceComparator());
        for (int i = 0; i < gameState.roomLut.size(); i++) {
            for (int j = i + 1; j < gameState.roomLut.size(); j++) {
                // calc
                // calc distance and save to a MPQ;
                if (gameState.roomLut.get(i).getDoor() != null && gameState.roomLut.get(i).getDoor() != null ) {
                    double dis = distanceOfRooms(gameState.roomLut.get(i), gameState.roomLut.get(j));
                    valueRoomPair vrp = new valueRoomPair(gameState.roomLut.get(i), gameState.roomLut.get(j));
                    mpq.insert(new distanceNode(dis, vrp));
                }
            }
        }
        return mpq;
    }
}