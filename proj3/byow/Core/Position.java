package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.Arrays;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.RANDOM;

// position of an object on canvas
public class Position implements Serializable {
    private int x;
    private int y;
    private int canvasWidth;//WIDTH;
    private int canvasHeight;//HEIGHT;

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
        this.canvasWidth = WIDTH;
        this.canvasHeight = HEIGHT;
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
        return new Position(pos.x, pos.y, pos.canvasWidth, pos.canvasHeight);
    }

    public static void copyValue(Position posSrc, Position posDst) {
        if (posSrc == null || posDst == null) {
            return;
        }
        posDst.x = posSrc.x;
        posDst.y = posSrc.y;
        posDst.canvasWidth = posSrc.canvasWidth;
        posDst.canvasHeight = posSrc.canvasHeight;
    }

    public void setCanvasWidth(int canvasW) {
        this.canvasWidth = canvasW;
    }

    public void setCanvasHeight(int canvasH) {
        this.canvasHeight = canvasH;
    }

    public int getCanvasWidth() {
        return this.canvasWidth;
    }

    public int getCanvasHeight() {
        return this.canvasHeight;
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
        return (this.getX() * this.getCanvasHeight() + this.getY());
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Position(");
        s.append(this.x);
        s.append("/");
        s.append(this.canvasWidth);
        s.append(", ");
        s.append(this.y);
        s.append("/");
        s.append(this.canvasHeight);
        s.append(")");
        return s.toString();
    }

    public static Position getRandomPositionInRandomRoom(RoomGraph rg) {
        int roomIndex = RandomUtils.uniform(RANDOM, rg.roomLut.size());
        Room r = rg.roomLut.get(roomIndex);

        // should not take wall into account
        int offX = RandomUtils.uniform(RANDOM, r.getSize().w - 2);
        int offY = RandomUtils.uniform(RANDOM, r.getSize().h - 2);

        return new Position(r.getPosition().x + offX + 1, r.getPosition().y + offY + 1, rg.canvasWidth, rg. canvasHeight);

    }
}
