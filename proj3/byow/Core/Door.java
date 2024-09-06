package byow.Core;

import edu.princeton.cs.algs4.Edge;

public class Door{
    //room pr;
    private Position pos; // position of a door
    private DoorType type; // is it a closed door or open door, or door to a hallway;

    private Edge edge;// if a door is chosen, hero could take an edge (route) to another room

    private Hallway hw;

    private int dir;
    private boolean[] expandable;

    //DoorDirection direct;
    private static class DoorType {
        private final String type;
        public DoorType(String c) {
            this.type = c;
        }
    }

    public static class Doorset {
        public static final DoorType INVISIBLE = new DoorType("invisible");
        public static final DoorType CLOSE = new DoorType("close");
        public static final DoorType OPEN = new DoorType("open");
        public static final DoorType INVALID = new DoorType("invalid");
    }
    public Door() {
        //this.pr = null;
        this.pos = null;
        this.type = null;
        this.edge = null;
        this.expandable = null;
    }
    public Door(Position pos, DoorType type, Edge e, Hallway hw, int dir) {
        //this.pr = r;
        this.pos = pos;
        this.type = type;
        this.edge = e;
        this.hw = hw;
        this.dir = dir;
        //this.expandable = new boolean[4];
        //this.expandable[0] = dir[0]; //north;
        //this.expandable[1] = dir[1]; //west;
        //this.expandable[2] = dir[2]; //south;
        //this.expandable[3] = dir[3]; //east;
    }

    public Position getPosition() {
        return this.pos;
    }

    public DoorType getDoorType(){
        return type;
    }

    public Edge getEdge(){
        return edge;
    }

    public Hallway getHallway(){
        return this.hw;
    }

    public int getDir() {
        return this.dir;
    }

    public boolean[] getDirection() {
        return expandable;
    }

    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public void setDoorType(DoorType dt){
        this.type = dt;
    }
    public void setEdge(Edge edge){
        this.edge = edge;
    }

    public void setHallway(Hallway hw){
        this.hw = hw;
    }
    public void setDir(int dir) {
        this.dir = dir;
    }

    public void setDirection(boolean[] dir) {
        this.expandable[0] = dir[0];
        this.expandable[1] = dir[1];
        this.expandable[2] = dir[2];
        this.expandable[3] = dir[3];
    }
}
