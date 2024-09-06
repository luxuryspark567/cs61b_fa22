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
public class RoomGraph extends EdgeWeightedGraph {

    private List<Room> roomLut;

    public RoomGraph() {
        super(ROOM_NUM);
    }

    public void initiate(TETile[][] world) {
        // randomly generate all rooms
        // roomArray is a lookup table;
        // 1, generate rooms, and each room have a random door.
        generateRooms(world);

        generateDoors(world);

        generateHallways(world);

        //generateMainDoor(world);

        System.out.println(this.toString());

    }

    private Position getRandomPosition() {
        Position ret = new Position();
        ret.x = RandomUtils.uniform(RANDOM, WIDTH - 3);
        ret.y = RandomUtils.uniform(RANDOM, HEIGHT - 3);
        return ret;
    }

    private Size getRandomSize() {
        Size ret = new Size();
        ret.width = RandomUtils.uniform(RANDOM, 3, ROOM_WIDTH_LIMIT);
        ret.height = RandomUtils.uniform(RANDOM, 3, ROOM_HEIGHT_LIMIT);
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
            else {
                System.out.println("Create a room fail!");
            }
            looperLimit--;
        }

        // save rooms to Graph;
        for (Room r:roomLut) {
            this.adj(roomLut.indexOf(r));
        }
    }


    private Door generateRandomDoor(Position posRoom,  Size sizeRoom, TETile[][] world) {

        int looperLimit = LOOP_LIMIT;
        while (looperLimit > 0) {
            // get door side
            int side = RandomUtils.uniform(RANDOM, 4);

            if ((posRoom.y + sizeRoom.height == HEIGHT - 1 && side == 0) //room besides north margin
                    || (posRoom.x == 0 && side == 1) //room besides west margin
                    || (posRoom.y == 0 && side == 2) //room besides south margin
                    || (posRoom.x + sizeRoom.width == WIDTH - 1 && side == 3)) { //room besides east margin
                //a room without a valid door is also a failed room
                looperLimit--;
                System.out.println("Create a door fail!");
            }
            else{
                if (side == 0) {
                    // pick a random position on the north side
                    int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.width);

                    Position doorPos = new Position(posRoom.x + index, posRoom.y + sizeRoom.height);
                    // if the door is besides a NOTHING, means a valid wall
                    if (doorPos.y + 1 < HEIGHT && world[doorPos.x][doorPos.y + 1] != Tileset.WALL){
                        //boolean[] dir = {true, false, false, false};
                        return new Door(doorPos, null, null, null, 0);
                    }
                    else {
                        System.out.println("Create a door facing a wall or something!");
                    }
                }
                else if (side == 1) {
                    // pick a random position on the west side
                    int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.height);
                    Position doorPos = new Position(posRoom.x, posRoom.y +  + index);
                    // if the door is besides a NOTHING, means a valid wall
                    if ((doorPos.x - 1 >= 0) && world[doorPos.x - 1][doorPos.y] != Tileset.WALL){
                        //boolean[] dir = {false, true, false, false};
                        return new Door(doorPos, null, null, null, 1);
                    }
                    else {
                        System.out.println("Create a door facing a wall or something!");
                    }
                }
                else if (side == 2) {
                    // pick a random position on the south side
                    int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.width);
                    Position doorPos = new Position(posRoom.x + index, posRoom.y);
                    // if the door is besides a NOTHING, means a valid wall
                    if ((doorPos.y - 1 >= 0) && world[doorPos.x][doorPos.y - 1] != Tileset.WALL){
                        //boolean[] dir = {false, false, true, false};
                        return new Door(doorPos, null, null, null, 2);
                    }
                    else {
                        System.out.println("Create a door facing a wall or something!");
                    }
                }
                else if (side == 3) {
                    // pick a random position on the east side
                    int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.height);
                    Position doorPos = new Position(posRoom.x + sizeRoom.width, posRoom.y + index);
                    // if the door is besides a NOTHING, means a valid wall
                    if ((doorPos.x + 1 < WIDTH) && world[doorPos.x + 1][doorPos.y] != Tileset.WALL){
                        //boolean[] dir = {false, false, false, true};
                        return new Door(doorPos, null, null, null, 3);
                    }
                    else {
                        System.out.println("Create a door facing a wall or something!");
                    }
                }
                // this will never happen
                return null;
            }
        }
        return null;
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
    boolean isDoorAtEdge (Door d) {
        return (d.getPosition().x != 0 && d.getPosition().x != WIDTH && d.getPosition().y != 0 && d.getPosition().y != HEIGHT);
    }

    double distanceOfDoors(Door d1, Door d2) {
        return Math.sqrt(Math.pow(d1.getPosition().x - d2.getPosition().x, 2) + Math.pow(d1.getPosition().y - d2.getPosition().y, 2));
    }
    public void generateHallways(TETile[][] world) {

        MinPQ<distanceNode> mpq = getRoomDistanceMPQ();
        //Debug
        //System.out.println(mpq.toString());
        WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(roomLut.size());

        int safeProofLooper = LOOP_LIMIT;

        // 2, generate hallways
        while (wqu.count() > 1 && safeProofLooper > 0 && (!mpq.isEmpty())) {//not all rooms are connected

            // 1, pick the closest two rooms to make a hallway
            distanceNode dsNode = mpq.delMin();

            // 2, are they already connected?
            if (!wqu.connected(roomLut.indexOf(dsNode.value.r1), roomLut.indexOf(dsNode.value.r2))) {

                // 3, connect them
                Hallway hw = connectRoomsWithHallway(dsNode.value.r1, dsNode.value.r2, world);

                if (hw != null) {
                    //union them
                    wqu.union(roomLut.indexOf(dsNode.value.r1), roomLut.indexOf(dsNode.value.r2));
                    Edge e = new Edge(roomLut.indexOf(dsNode.value.r1), roomLut.indexOf(dsNode.value.r2), hw.getWeight());

                    // update edge to door.
                    hw.getSrc().setEdge(e);
                    hw.getDst().setEdge(e);
                    hw.getSrc().setHallway(hw);
                    hw.getDst().setHallway(hw);

                    //setPixelAfterCheckIfTile(hw.getSrc().getPosition().x, hw.getSrc().getPosition().y, Tileset.UNLOCKED_DOOR, world);
                    //setPixelAfterCheckIfTile(hw.getDst().getPosition().x, hw.getDst().getPosition().y, Tileset.UNLOCKED_DOOR, world);

                    this.addEdge(e);
                }
            }

            safeProofLooper--;
        }
    }

    private Hallway connectDoors(Door srcDoor, Door dstDoor, TETile[][] world) {
        //src to dst
        // get source coordinate and destination coordinate
        // an avatar walks from src to dst, with each step, avatar should be closer to dst;

        // 0, creat an avatar
        DiggerAvatar digger = new DiggerAvatar(srcDoor, dstDoor, world);

        // 1, Do the work bitch!!! digger a tunnel from srcDoor to dstDoor
        return digger.digATunnel();

    }
    // generate a hallway between two doors
    private Hallway connectRoomsWithHallway(Room srcRoom, Room dstRoom, TETile[][] world) {
        //src to dst
        Door srcDoor = srcRoom.getDoor();
        Door dstDoor = dstRoom.getDoor();

        return connectDoors(srcDoor, dstDoor, world);
    }

    // generate a room:
    // 1, it overlaps existed objects, should return false;
    // 2, if not, create a room and return true.
    private Room generateRoom(TETile[][] world) {
        Position p = getRandomPosition();
        Size s = getRandomSize();
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

        // reach here means it is a valid room

        // paint the room on the canvas
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

        return new Room(p,s);
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