package byow.Core;

public class Room {
    private Position pos; // position
    private Size si; //size
    private Door door; // north door, west door, south door and east door
    public Room(Position pos, Size si) {
        this.pos = pos;
        this.si = si;
        this.door = null;
        boolean[] dir;
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
}
