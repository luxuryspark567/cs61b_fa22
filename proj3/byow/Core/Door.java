package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Edge;

public class Door extends Item {
    private static final String DESCRIPTION_DFT = "Door";
    private static final int WEIGHT_DFT = 100;
    private static final int VALUE_DFT = 0;
    private static final int DURATION_DFT = 100;
    private static final int SIGNATURE_DFT = 1;

    private static final TETile TYPE_DFT = Tileset.UNLOCKED_DOOR;

    //room pr;
    //private Position pos; // position of a door
    //private DoorType type; // is it a closed door or open door, or door to a hallway;

    private Edge edge;// if a door is chosen, hero could take an edge (route) to another room

    private Hallway hw;

    private Direction dir;

    private int siginature;

    private TETile type;
    //private boolean[] expandable;

    //DoorDirection direct;
    /*
    private static class DoorType {
        private final String type;
        public DoorType(String c) {
            this.type = c;
        }
    }
    */
/*
    public static class Doorset {
        public static final DoorType INVISIBLE = new DoorType("invisible");
        public static final DoorType CLOSE = new DoorType("close");
        public static final DoorType OPEN = new DoorType("open");
        public static final DoorType INVALID = new DoorType("invalid");
    }

 */
    public Door() {
        super();
        this.edge = null;
        this.hw = null;
        this.dir = null;
        this.siginature = SIGNATURE_DFT;
        this.type = Tileset.UNLOCKED_DOOR;
    }
    public Door(Position pos, Edge e, Hallway hw, Direction dir) {
        super(DESCRIPTION_DFT, pos);
        //this.type = type;
        this.edge = e;
        this.hw = hw;
        this.dir = dir;
        this.siginature = SIGNATURE_DFT;
        this.type = Tileset.UNLOCKED_DOOR;
    }

    //public Position getPosition() {
    //    return this.pos;
    //}

    //public DoorType getDoorType(){
    //    return type;
    //}

    public Edge getEdge(){
        return edge;
    }

    public Hallway getHallway(){
        return this.hw;
    }

    public Direction getDir() {
        return this.dir;
    }

    //public boolean[] getDirection() {
    //    return expandable;
    //}

    //public void setPosition(Position pos) {
    //    this.pos = pos;
    //}

    //public void setDoorType(DoorType dt){
    //    this.type = dt;
    //}
    public void setEdge(Edge edge){
        this.edge = edge;
    }

    public void setHallway(Hallway hw){
        this.hw = hw;
    }
    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public int getSiginature() {
        return this.siginature;
    }
/*
    public void setDirection(boolean[] dir) {
        this.expandable[0] = dir[0];
        this.expandable[1] = dir[1];
        this.expandable[2] = dir[2];
        this.expandable[3] = dir[3];
    }
*/
    public String toString() {
        StringBuilder s = new StringBuilder(this.getDescription());
        s.append(this.getPosition());
        s.append(", ");
        s.append(this.dir);
        return s.toString();
    }
}
