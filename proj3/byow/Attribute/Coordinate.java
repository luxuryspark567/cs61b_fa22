package byow.Attribute;

import byow.Utils.TileUtils;

import java.io.Serializable;
import java.util.Comparator;

import static byow.Attribute.Direction.getShiftPosition;
import static byow.Core.Engine.X_OFF;
import static byow.Core.Engine.Y_OFF;
import static byow.Utils.TileUtils.isInCanvas;

public class Coordinate implements Serializable {
    private double x;
    private double y;
    private double canvasWidth; // canvas WIDTH;
    private double canvasHeight; // canvas HEIGHT;

    public Coordinate() {
        this.x = 0;
        this.y = 0;
    }
    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(double x, double y, double WIDTH, double HEIGHT) {
        this.x = x;
        this.y = y;
        this.canvasWidth = WIDTH;
        this.canvasHeight = HEIGHT;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinate coord){
            return this.x == coord.x && this.y == coord.y;
        }
        return false;
    }

    // ATTENTION: resolution is 0.01;
    public static class CoordinateComparator implements Comparator, Serializable {
        @Override
        public int compare(Object o1, Object o2) {
            return (int) (100 * (((Coordinate)o1).getSerial() - ((Coordinate)o2).getSerial()));
        }
    }
    public static Coordinate copyOf(Coordinate coord) {
        if (coord == null) {
            return null;
        }
        return new Coordinate(coord.x, coord.y, coord.canvasWidth, coord.canvasHeight);
    }

    public static void copyValue(Coordinate coordSrc, Coordinate coordDst) {
        if (coordSrc == null || coordDst == null) {
            return;
        }
        coordDst.x = coordSrc.x;
        coordDst.y = coordSrc.y;
        coordDst.canvasWidth = coordSrc.canvasWidth;
        coordDst.canvasHeight = coordSrc.canvasHeight;
    }

    public void setCanvasWidth(double W) {
        this.canvasWidth = W;
    }

    public void setCanvasHeight(double H) {
        this.canvasHeight = H;
    }

    public double getCanvasWidth() {
        return this.canvasWidth;
    }

    public double getCanvasHeight() {
        return this.canvasHeight;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getSerial() {
        return (this.getX() * this.canvasHeight + this.getY());
    }

    // switch a tile world position to a canvas coordinate
    public static Coordinate getCanvasCoordFromTilePos(Position pos) {
        double x = pos.getX() + X_OFF;
        double y = pos.getY() + Y_OFF;
        if (isInCanvas(x, y)) {
            return new Coordinate(x, y);
        }
        else {
            return null;
        }
    }

    public Coordinate getCoordinateFromShiftedBase(Coordinate base) {
        double x = base.getX() + this.getX();
        double y = base.getY() + this.getY();
        if (isInCanvas(x, y)) {
            return new Coordinate(x, y);
        }
        else {
            return null;
        }
    }

    public static double getDistance(Coordinate coord1, Coordinate coord2) {
        if (coord1 == null || coord2 == null) {
            return 0;
        }
        return Math.sqrt(Math.pow(coord1.getX() - coord2.getX(), 2) + Math.pow(coord1.getY() - coord2.getY(), 2));
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Coordinate(");
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
}

