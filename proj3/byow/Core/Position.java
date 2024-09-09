package byow.Core;

import byow.TileEngine.TETile;

import java.util.Arrays;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.RANDOM;

// position of an object on canvas
public class Position {
    int x;
    int y;

    public Position() {
        this.x = 0;
        this.y = 0;
    }
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position pos){
            return this.x == pos.x && this.y == pos.y;
        }
        return false;
    }

    public static Position copyOf(Position pos) {
        if (pos == null) {
            return null;
        }

        return new Position(pos.x, pos.y);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Position(");
        s.append(this.x);
        s.append(", ");
        s.append(this.y);
        s.append(")");
        return s.toString();
    }

    public static Position getRandomPositionInRandomRoom(RoomGraph rg) {
        int roomIndex = RandomUtils.uniform(RANDOM, rg.roomLut.size());
        Room r = rg.roomLut.get(roomIndex);

        // should not take wall into account
        int offX = RandomUtils.uniform(RANDOM, r.getSize().w - 2);
        int offY = RandomUtils.uniform(RANDOM, r.getSize().h - 2);

        return new Position(r.getPosition().x + offX, r.getPosition().y + offY);

    }
}
