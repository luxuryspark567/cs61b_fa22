package byow.Utils;

import byow.Articles.Door;
import byow.Articles.Hallway;
import byow.Articles.Lamp;
import byow.Articles.Room;
import byow.Attribute.Direction;
import byow.Attribute.Directionset;
import byow.Attribute.Position;
import byow.Attribute.Size;
import byow.Core.GameState;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
//import static byow.Core.Main.gameState;
import static byow.Attribute.Direction.getShiftPosition;
import static byow.Attribute.Directionset.SOUTH;
import static byow.Core.Engine.*;
import static byow.Core.Main.gameState;
import static byow.Utils.TileUtils.*;

public class WorldGenerateUtils {


    public WorldGenerateUtils() {
    }

    private Position getRandomPosition() {
        Position ret = new Position();
        ret.setX(RandomUtils.uniform(RANDOM, TileUtils.getTileWorldWidth()));
        ret.setY(RandomUtils.uniform(RANDOM, TileUtils.getTileWorldHeight()));
        return ret;
    }

    private Size getRandomSize() {
        Size ret = new Size();
        // random is [a,b), but when we say a room is size w x h, means that height is [1, h]
        // and here size is limit to MIN to MAX, [MIN, MAX] which is [MIN, MAX + 1) for the function.
        ret.setW(RandomUtils.uniform(RANDOM, ROOM_WIDTH_MIN, ROOM_WIDTH_MAX + 1));
        ret.setH(RandomUtils.uniform(RANDOM, ROOM_HEIGHT_MIN, ROOM_HEIGHT_MAX + 1));
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

        //gameState.refWorld = TETile.copyOf(gameState.world);
    }

    private Door checkAndGetDoor(Position pos, Direction dir, TETile[][] world) {
        // if the door is besides wall, then this door is invalid
        Position posShift1 = Direction.getShiftPosition(pos, dir);
        Position posShift2 = Direction.getShiftPosition(posShift1, dir);

        if (isInTileWorld(posShift1) && isInTileWorld(posShift2)) {
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
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.getW() - 1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.getX() + index, posRoom.getY() + sizeRoom.getH() - 1,
                        TileUtils.getTileWorldWidth(), TileUtils.getTileWorldHeight());
                newDoor = checkAndGetDoor(doorPos, Directionset.NORTH, world);
            }
            else if (side == Directionset.WEST) {
                // pick a random position on the west side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.getH() - 1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.getX(), posRoom.getY() + index,
                        TileUtils.getTileWorldWidth(), TileUtils.getTileWorldHeight());
                newDoor = checkAndGetDoor(doorPos, Directionset.WEST, world);
            }
            else if (side == Directionset.SOUTH) {
                // pick a random position on the south side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.getW() -1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.getX() + index, posRoom.getY(),
                        TileUtils.getTileWorldWidth(), TileUtils.getTileWorldHeight());
                newDoor = checkAndGetDoor(doorPos, Directionset.SOUTH, world);
            }
            else if (side == Directionset.EAST) {
                // pick a random position on the east side
                int index = RandomUtils.uniform(RANDOM, 1, sizeRoom.getH() - 1);
                //System.out.println(index);
                Position doorPos = new Position(posRoom.getX() + sizeRoom.getW() - 1,
                        posRoom.getY() + index, TileUtils.getTileWorldWidth(), TileUtils.getTileWorldHeight());
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
                gameState.tmDB.put(door.getPosition(), door); // add to database
                //world[door.getPosition().getX()][door.getPosition().getY()] = Tileset.UNLOCKED_DOOR;

                // generate locked or unlocked randomly
                int index = RandomUtils.uniform(RANDOM, 2);
                if (index == 0) {
                    door.setType(Tileset.UNLOCKED_DOOR);
                    // environment objects should be paint on the world.
                    paintTile(door.getPosition(), Tileset.UNLOCKED_DOOR, gameState.world);
                }
                else {
                    door.setType(Tileset.LOCKED_DOOR);
                    // environment objects should be paint on the world.
                    paintTile(door.getPosition(), Tileset.LOCKED_DOOR, gameState.world);
                }

            }
        }
        //gameState.refWorld = TETile.copyOf(gameState.world); // always remember when to initiate refWorld
    }

    // a new lamp should never overlap any existing objects
    private Lamp generateOneRandomLamp(Room r, TETile[][] world, GameState gameState) {

        int looperLimit = LOOP_LIMIT;
        Lamp newLamp = null;
        Object o;
        while (looperLimit > 0) {
            // Check if lamp overlaps any existing objects
            Position pos = Room.getRandomPositionInARoom(r);

            // Check the position is beside the door
            // get the door position, and shift the position one step reversed the door direction,
            // this is the position not allowed, because avatar won't be able to get out if a lamp
            // blocks the door;
            Position posBesideDoor = Direction.getShiftPosition(r.getDoor().getPosition(),
                    Direction.getRevertDir(r.getDoor().getDir()));

            // is it an empty spot?
            if (!posBesideDoor.equals(pos)) { // could not locate besides the door
                o = gameState.tmDB.get(pos);
                if (o == null) { // found an empty sport
                    newLamp = new Lamp(pos, r, gameState);
                    break;
                }
            }

/*
            if (isTileType(pos, Tileset.NOTHING, world)
                    || isTileType(pos, Tileset.FLOOR, world)) {
                newLamp = new Lamp(pos, r, gameState);
            }

 */
            looperLimit--;
        }

        return newLamp;
    }

    public void generateLamps(GameState gameState) {
        for (Room r: gameState.roomLut) {
            // generate a random door
            Lamp lamp = generateOneRandomLamp(r, gameState.world, gameState);
            if (lamp != null)
            {
                r.setLamp(lamp);
                gameState.tmDB.put(lamp.getPosition(), lamp); // add to database
                if (lamp.isSwitchOn()) {
                    paintTile(lamp.getPosition(), Tileset.LAMP, gameState.world);
                }
                else {
                    paintTile(lamp.getPosition(), new TETile(Tileset.LAMP, Color.black), gameState.world);
                }
            }
        }
        //gameState.refWorld = TETile.copyOf(gameState.world); // always remember when to initiate refWorld
    }

    public void paintRoad(List<Position> route, TETile[][] mWorld){
        for (Position pos: route) {
            paintTile(pos, Tileset.FLOOR, mWorld);

            Position posCurNorth = getShiftPosition(pos, Directionset.NORTH);
            Position posCurWest = getShiftPosition(pos, Directionset.WEST);
            Position posCurSouth = getShiftPosition(pos, SOUTH);
            Position posCurEast = getShiftPosition(pos, Directionset.EAST);

            if (isInTileWorld(posCurNorth) && isTileType(posCurNorth, null, mWorld)) {
                paintTile(posCurNorth, Tileset.WALL, mWorld);
            }
            if (isInTileWorld(posCurWest) && isTileType(posCurWest, null, mWorld)) {
                paintTile(posCurWest, Tileset.WALL, mWorld);
            }
            if (isInTileWorld(posCurSouth) && isTileType(posCurSouth, null, mWorld)) {
                paintTile(posCurSouth, Tileset.WALL,mWorld);
            }
            if (isInTileWorld(posCurEast) && isTileType(posCurEast, null, mWorld)) {
                paintTile(posCurEast, Tileset.WALL, mWorld);
            }
        }


    }
    public void generateHallways(GameState gameState) {

        MinPQ<distanceNode> mpq = getRoomDistanceMPQ(gameState);

        WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(gameState.roomLut.size());

        TETile[][] cpyWorld = TETile.copyOf(gameState.world);
        // 3.2 you should not run into a wall;
        // 3.3 you should not run into a unclosed door or locked door;
        List<TETile> forbidTile = new LinkedList<>();
        forbidTile.add(Tileset. WALL);
        forbidTile.add(Tileset. UNLOCKED_DOOR);
        forbidTile.add(Tileset. LOCKED_DOOR);

        int forbidEdgeDis = 2;
        //{Tileset. WALL, Tileset.UNLOCKED_DOOR, Tileset.LOCKED_DOOR};

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

                    Door srcDoor = dsNode.value.r1.getDoor();
                    Door dstDoor = dsNode.value.r2.getDoor();

                    RouteSearch rs = new DiggerRouteSearch(srcDoor.getDir(), forbidTile, forbidEdgeDis);
                    List<Position> route = rs.getRoute(srcDoor.getPosition(), dstDoor.getPosition(), cpyWorld);

                    if (route != null) {
                        //union them
                        wqu.union(gameState.roomLut.indexOf(dsNode.value.r1), gameState.roomLut.indexOf(dsNode.value.r2));
                        // paintRoad
                        paintRoad(route, gameState.world);
                    }
                }
            }
            safeProofLooper--;
        }



        //gameState.refWorld = TETile.copyOf(gameState.world);
    }

    // generate a room:
    // 1, it overlaps existed objects, should return false;
    // 2, if not, create a room and return true.
    private Room generateRoom(TETile[][] world) {
        Position pos = getRandomPosition();
        Size size = getRandomSize();

        //check if room location is out of canvas
        Position posRightCorner = new Position((pos.getX() + size.getW() - 1), (pos.getY() + size.getH() - 1));
        if (!isInTileWorld(posRightCorner)) {
            return null;
        }

        //check if overlaps
        for (int i = 0; i < size.getW(); i++) {
            for (int j = 0; j < size.getH(); j++) {
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
        for (int i = 0; i < size.getW(); i++) {
            world[pos.getX() + i][pos.getY()] = Tileset.WALL;
            world[pos.getX() + i][pos.getY() + size.getH() - 1] = Tileset.WALL;
        }

        for (int j = 0; j < size.getH(); j++) {
            world[pos.getX()][pos.getY() + j] = Tileset.WALL;
            world[pos.getX() + size.getW() - 1][pos.getY() + j] = Tileset.WALL;
        }
        //create tile
        for (int i = 1; i < size.getW() - 1; i++) {
            for (int j = 1; j < size.getH() - 1; j++) {
                int localX = pos.getX() + i;
                int localY = pos.getY() + j;
                world[localX][localY] = Tileset.FLOOR;
            }
        }

        return new Room(pos, size, world);
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