package byow.Core;

import byow.TileEngine.TETile;

import java.util.Arrays;

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
}
