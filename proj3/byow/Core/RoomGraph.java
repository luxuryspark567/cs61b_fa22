package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static byow.Core.Engine.*;
import static byow.Core.TileUtils.*;

public class RoomGraph extends EdgeWeightedGraph {

    private List<Room> roomLut;
    // 0, creat an avatar
    DiggerAvatar digger;
    public RoomGraph() {
        super(ROOM_NUM);
    }

    public void initiate(TETile[][] world) {
        // randomly generate all rooms
        // roomArray is a lookup table;
        // 1, generate rooms, and each room have a random door.
        generateRooms(world);
/*
        Room tmp = new Room(new Position(0, 0),new Size(0,0));
        for (Room r: roomLut) {
            if (tmp.getPosition().x < r.getPosition().x) {
                tmp.setPosition(Position.copyOf(r.getPosition()));
            }
        }
        System.out.println(tmp);
*/
        generateDoors(world);

        //debug
        for (Room r: roomLut) {
            System.out.println(r);
        }
        //System.out.println(roomLut);

        generateHallways(world);

        System.out.println(this);

    }

    private Position getRandomPosition() {
        Position ret = new Position();
        ret.x = RandomUtils.uniform(RANDOM, WIDTH);
        ret.y = RandomUtils.uniform(RANDOM, HEIGHT);
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

    private void generateRooms(TETile[][] world) {
        roomLut = new ArrayList<>(ROOM_NUM);

        int looper = 0;
        int looperLimit = LOOP_LIMIT;
        while (looper < ROOM_NUM && looperLimit > 0) {
            Room newRoom = generateRoom(world);
            if (newRoom != null) {
                looper++;
                roomLut.add(newRoom);
                System.out.println("Create a room success!");
            }
            //else {
                //System.out.println("Create a room fail!");
            //}
            looperLimit--;
        }

        // save rooms to Graph;
        for (Room r:roomLut) {
            this.adj(roomLut.indexOf(r));
        }
    }

    boolean isRoomBesidesNorthMargin(Position pos, Size size, Direction side) {
        return ((pos.y + size.h - 1 == HEIGHT - 2 || pos.y + size.h - 1 == HEIGHT - 1) && side == Directionset.NORTH);
    }

    boolean isRoomBesidesWestMargin(Position pos, Direction side) {
        return ((pos.x == 0 || pos.x == 1) && side == Directionset.WEST);
    }

    boolean isRoomBesidesSouthMargin(Position pos, Direction side) {
        return ((pos.y == 0 || pos.y == 1) && side == Directionset.SOUTH);
    }

    boolean isRoomBesidesEastMargin(Position pos, Size size, Direction side) {
        return ((pos.x + size.w - 1 == WIDTH - 2 || pos.x + size.w - 1 == WIDTH - 1) && side == Directionset.EAST);
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
                return new Door(pos, null, null, null, dir);
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
                Position doorPos = new Position(posRoom.x + index, posRoom.y + sizeRoom.h - 1);
                newDoor = checkAndGetDoor(doorPos, Directionset.NORTH, world);
            }
            else if (side == Directionset.WEST) {
                // pick a random position on the west side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.h - 1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.x, posRoom.y + index);
                newDoor = checkAndGetDoor(doorPos, Directionset.WEST, world);
            }
            else if (side == Directionset.SOUTH) {
                // pick a random position on the south side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.w -1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.x + index, posRoom.y);
                newDoor = checkAndGetDoor(doorPos, Directionset.SOUTH, world);
            }
            else if (side == Directionset.EAST) {
                // pick a random position on the east side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.h - 1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.x + sizeRoom.w - 1, posRoom.y + index);
                newDoor = checkAndGetDoor(doorPos, Directionset.EAST, world);
            }
            looperLimit--;
        }

        //System.out.println(looperLimit);
        return newDoor;
    }
    private void generateDoors(TETile[][] world) {
        for (Room r: this.roomLut) {
            // generate a random door
            Door door = generateRandomDoor(r.getPosition(), r.getSize(), world);
            if (door != null)
            {
                // paint door
                r.setDoor(door);
                world[door.getPosition().x][door.getPosition().y] = Tileset.UNLOCKED_DOOR;
            }
        }

    }

    public void generateHallways(TETile[][] world) {

        MinPQ<distanceNode> mpq = getRoomDistanceMPQ();

        // create a digger to dig tunnels
        this.digger = new DiggerAvatar(world);

        WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(roomLut.size());

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
                if (!wqu.connected(roomLut.indexOf(dsNode.value.r1), roomLut.indexOf(dsNode.value.r2))) {
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
                        wqu.union(roomLut.indexOf(dsNode.value.r1), roomLut.indexOf(dsNode.value.r2));
                        Edge e = new Edge(roomLut.indexOf(dsNode.value.r1), roomLut.indexOf(dsNode.value.r2), hw.getWeight());

                        // update edge to door.
                        hw.getSrc().setEdge(e);
                        hw.getDst().setEdge(e);
                        hw.getSrc().setHallway(hw);
                        hw.getDst().setHallway(hw);

                        this.addEdge(e);
                    }
                }
            }
            safeProofLooper--;
        }

        System.out.println(mpq);
        System.out.println(wqu.count());
    }

    // generate a hallway between two doors
    private Hallway connectRoomsWithHallway(Room srcRoom, Room dstRoom) {
        //src to dst
        Door srcDoor = srcRoom.getDoor();
        Door dstDoor = dstRoom.getDoor();

        // arrange a digging job to the digger
        this.digger.arrangeDiggingJog(srcDoor, dstDoor);
        return this.digger.digATunnel();
    }

    // generate a room:
    // 1, it overlaps existed objects, should return false;
    // 2, if not, create a room and return true.
    private Room generateRoom(TETile[][] world) {
        Position pos = getRandomPosition();
        Size size = getRandomSize();
        //check if room location is out of canvas
        if ((pos.x + size.w - 1) > (WIDTH - 1) || (pos.y + size.h - 1) > (HEIGHT - 1)) {
            return null;
        }
        //check if overlaps
        for (int i = 0; i < size.w; i++) {
            for (int j = 0; j < size.h; j++) {
                int localX = pos.x + i;
                int localY = pos.y + j;

                if (world[localX][localY] != null) {
                    return null; // overlaps
                }
            }
        }

        // reach here means it is a valid room

        // paint the room on the canvas
        // create wall
        for (int i = 0; i < size.w; i++) {
            world[pos.x + i][pos.y] = Tileset.WALL;
            world[pos.x + i][pos.y + size.h - 1] = Tileset.WALL;
        }

        for (int j = 0; j < size.h; j++) {
            world[pos.x][pos.y + j] = Tileset.WALL;
            world[pos.x + size.w - 1][pos.y + j] = Tileset.WALL;
        }
        //create tile
        for (int i = 1; i < size.w - 1; i++) {
            for (int j = 1; j < size.h - 1; j++) {
                int localX = pos.x + i;
                int localY = pos.y + j;
                world[localX][localY] = Tileset.FLOOR;
            }
        }

        return new Room(pos,size);
    }

    // don't care about the room distance, room distance is measured by door distance.
    private double distanceOfRooms(Room r1, Room r2) {
        return Math.sqrt(Math.pow(r1.getDoor().getPosition().x - r2.getDoor().getPosition().x, 2) + Math.pow(r1.getDoor().getPosition().y - r2.getDoor().getPosition().y, 2));
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

    private MinPQ<distanceNode> getRoomDistanceMPQ () {
        // 1, calc a distance, and use pq to save the distance, because a MinPQ will always O(1) to get
        // minimum value
        MinPQ<distanceNode> mpq = new MinPQ<>(new nodeDistanceComparator());
        for (int i = 0; i < this.roomLut.size(); i++) {
            for (int j = i + 1; j < this.roomLut.size(); j++) {
                // calc
                // calc distance and save to a MPQ;
                if (this.roomLut.get(i).getDoor() != null && this.roomLut.get(i).getDoor() != null ) {
                    double dis = distanceOfRooms(this.roomLut.get(i), this.roomLut.get(j));
                    valueRoomPair vrp = new valueRoomPair(this.roomLut.get(i), this.roomLut.get(j));
                    mpq.insert(new distanceNode(dis, vrp));
                }
            }
        }
        return mpq;
    }
}