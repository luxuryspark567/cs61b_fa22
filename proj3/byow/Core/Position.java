package byow.Core;

import java.io.Serializable;
import java.util.Comparator;

import static byow.Core.Engine.*;

// position of an object on canvas
public class Position implements Serializable {
    private int x;
    private int y;
    private int tileWidth; // WIDTH;
    private int tileHeight; // HEIGHT;

    public Position() {
        this.x = 0;
        this.y = 0;
    }
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(int x, int y, int WIDTH, int HEIGHT) {
        this.x = x;
        this.y = y;
        this.tileWidth = WIDTH;
        this.tileHeight = HEIGHT;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Position pos){
            return this.x == pos.x && this.y == pos.y;
        }
        return false;
    }
    public static class PositionComparator implements Comparator, Serializable {
        @Override
        public int compare(Object o1, Object o2) {
            return ((Position)o1).getIndex() - ((Position)o2).getIndex();
        }
    }
    public static Position copyOf(Position pos) {
        if (pos == null) {
            return null;
        }
        return new Position(pos.x, pos.y, pos.tileWidth, pos.tileHeight);
    }

    public static void copyValue(Position posSrc, Position posDst) {
        if (posSrc == null || posDst == null) {
            return;
        }
        posDst.x = posSrc.x;
        posDst.y = posSrc.y;
        posDst.tileWidth = posSrc.tileWidth;
        posDst.tileHeight = posSrc.tileHeight;
    }

    public void setTileWidth(int W) {
        this.tileWidth = W;
    }

    public void setTileHeight(int H) {
        this.tileHeight = H;
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public int getTileHeight() {
        return this.tileHeight;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getIndex() {
        return (this.getX() * this.tileHeight + this.getY());
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Position(");
        s.append(this.x);
        s.append("/");
        s.append(this.tileWidth);
        s.append(", ");
        s.append(this.y);
        s.append("/");
        s.append(this.tileHeight);
        s.append(")");
        return s.toString();
    }

    public static Position getRandomPositionInRandomRoom(GameState gameState) {
        int roomIndex = RandomUtils.uniform(RANDOM, gameState.roomLut.size());
        Room r = gameState.roomLut.get(roomIndex);

        // should not take wall into account
        int offX = RandomUtils.uniform(RANDOM, r.getSize().w - 2);
        int offY = RandomUtils.uniform(RANDOM, r.getSize().h - 2);

        return new Position(r.getPosition().x + offX + 1,
                r.getPosition().y + offY + 1,
                TileUtils.getTileWorldWidth(),
                TileUtils.getTileWorldHeight());
    }
}
