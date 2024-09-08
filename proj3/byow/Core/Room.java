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

}
