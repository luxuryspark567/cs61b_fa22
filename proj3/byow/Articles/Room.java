package byow.Articles;

import byow.Attribute.Position;
import byow.Attribute.Size;
import byow.Core.*;
import byow.TileEngine.TETile;
import byow.Utils.RandomUtils;
import byow.Utils.TileUtils;

import java.io.Serializable;
import java.util.Iterator;

import static byow.Core.Engine.LOOP_LIMIT;
import static byow.Core.Main.engine;
import static byow.Core.Main.gameState;
import static byow.Core.Engine.RANDOM;

public class Room implements Serializable, Iterable<Position>{
    private Position pos; // position
    private Size si; //size
    private Door door; // north door, west door, south door and east door
    private Lamp lamp;

    private TETile[][] world;
    public Room(Position pos, Size si, TETile[][] world) {
        this.pos = pos;
        this.si = si;
        this.door = null;
        this.world = world;
        boolean[] dir;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Room is: ");
        if (this.getPosition() != null) {
            s.append(this.getPosition());
            s.append(", ");
        }

        if (this.getSize() != null) {
            s.append(this.getSize());
            s.append(", ");
        }

        if (this.getDoor() != null) {
            s.append(this.getDoor());
        }

        return s.toString();
    }
    public Position getPosition() {
        return this.pos;
    }

    public Size getSize() {
        return this.si;
    }

    public Door getDoor() {
        return this.door;
    }

    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public void setSize(Size size) {
        this.si = size;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public Lamp getLamp() {
        return this.lamp;
    }

    public void setLamp(Lamp lamp) {
        this.lamp = lamp;
    }

    public static Position getWorldPosFromRoomOffSet(Position posRoom, int offX, int offY) {
        return new Position(posRoom.getX() + offX,
                            posRoom.getY() + offY,
                                TileUtils.getTileWorldWidth(),
                                TileUtils.getTileWorldHeight());
    }

    public static Position getRandomPositionInARoom(Room r) {
        // should not take wall into account
        int offX = RandomUtils.uniform(RANDOM,1, r.getSize().getW() - 1);
        int offY = RandomUtils.uniform(RANDOM,1, r.getSize().getH() - 1);
        return getWorldPosFromRoomOffSet(r.getPosition(), offX, offY);
        // if there is a valid object at the position, should not overlap

        /*
        return new Position(r.getPosition().getX() + offX + 1,
                r.getPosition().getY() + offY + 1,
                TileUtils.getTileWorldWidth(),
                TileUtils.getTileWorldHeight());

         */
    }

    public static Position getRandomPositionInRandomRoom(GameState gameState) {
        int roomIndex = RandomUtils.uniform(RANDOM, gameState.roomLut.size());
        Room r = gameState.roomLut.get(roomIndex);

        Position pos = null;
        int looperLimit = LOOP_LIMIT;
        Object o = null;

        while (looperLimit > 0) {

            pos = getRandomPositionInARoom(r);

            // find a empty spot
            o = gameState.tmDB.get(pos);

            if (o == null) {
                break;
            }

            looperLimit--;
        }

        return pos;
    }

    @Override
    public Iterator<Position> iterator() {
        return new RoomFloorPositionIterator();
    }

    private class RoomFloorPositionIterator implements Iterator<Position> {

        int offX = 1;
        int offY = 0;
        //Position posMagic = new Position(0, -1);

        @Override
        public boolean hasNext() {

            //update y
            offY = offY + 1;
            if (offY >= getSize().getH() - 1) {
                offY = 1;

                //update x
                offX = offX + 1;
                if (offX >= getSize().getW() - 1) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Position next() {
            return getWorldPosFromRoomOffSet(pos, offX, offY);
        }
    }
}
