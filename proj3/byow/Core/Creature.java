package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;

import static byow.Core.TileUtils.isTileType;

public class Creature extends RoadSearch{
    private int damage;
    private int health;
    private int age;
    private int weight;
    private Size size;
    private int affection;
    private Position position;
    LinkedList<Item> inventory;

    public TETile[][] world;
    public TETile[][] refWorld;
    private RoomGraph rg; // just a pointer
    private Position bakPos;
    private TETile bakTile;

    public Creature() {
        this.damage = 0;
        this.health = 0;
        this.age = 0;
        this.weight = 0;
        this.size = new Size(0, 0);
        this.affection = 0;
        this.position = new Position(0, 0);
        this.inventory = new LinkedList<>();

        this.rg = null;
        this.world = null;
        this.refWorld = null;
        this.bakPos = null;
        this.bakTile = null;
    }

    public Creature(int damage, int health, int age, int weight, Size size, int affection, Position pos, RoomGraph rg, TETile[][] world) {
        super();
        this.damage = damage;
        this.health = health;
        this.age = age;
        this.weight = weight;
        this.size = Size.copyOf(size);
        this.affection = affection;
        this.position = Position.copyOf(pos);
        this.inventory = new LinkedList<>();
        this.rg = rg;
        rg.tm.put(this.getPosition(), this);
        this.world = world;
        this.refWorld = TETile.copyOf(world);// back up the world, and use the backup to search for routes.
        this.bakPos = Position.copyOf(pos);
        this.bakTile = Tileset.NOTHING;
    }

    public boolean MoveOneStep(Direction dir) {

        // shift one sep
        Position pos = this.getPosition();
        this.setBackedPosition(Position.copyOf(pos));
        this.setBackedTile(refWorld[pos.getX()][pos.getY()]);

        Direction.shiftPosition(pos, dir);

        // check if the new position is OK
        if (isTileType(pos, Tileset.WALL, this.world)) {
            System.out.println("you a running into a wall, it is not allowed!");
            return false;
        }
        else if (isTileType(pos, Tileset.FLOOR, this.world)) {
            this.getRoomGraph().tm.remove(this.getBackedPosition(), this);
            this.setPosition(pos);
            this.getRoomGraph().tm.put(this.getPosition(), this);
            return true;
        }
        else if (isTileType(pos, Tileset.UNLOCKED_DOOR, this.world)) {
            this.getRoomGraph().tm.remove(this.getBackedPosition(), this);
            this.setPosition(pos);
            this.getRoomGraph().tm.put(this.getPosition(), this);
            return true;
        }
        else {
            // rule 1: can not run into a wall
            // rule 2: can only run onto a floor
            System.out.println("undefined!");
            return false;
        }
    }
    public int getDamage() {
        return this.damage;
    }
    public int getHealth() {
        return this.health;
    }
    public int getAge() {
        return this.age;
    }
    public int getWeight() {
        return this.weight;
    }

    public Size getSize() {
        return Size.copyOf(this.size);
    }
    public int getAffection() {
        return this.affection;
    }

    public Position getPosition() {
        return Position.copyOf(this.position);
    }

    public LinkedList<Item> getInventory() {
        return this.inventory;
    }



    public void setDamage(int damage) {
        this.damage = damage;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setSize(Size size) {
        this.size.w = size.w;
        this.size.h = size.h;
    }
    public void setAffection(int affection) {
        this.affection = affection;
    }

    public void setPosition(Position pos) {
        this.position.setX(pos.getX());
        this.position.setY(pos.getY());
    }

    public void addInventory(Item item) {
        this.inventory.add(item);
    }

    public void removeInventory(Item item) {
        this.inventory.remove(item);
    }


    public Position getBackedPosition() {
        return this.bakPos;
    }

    public void setBackedPosition(Position pos) {
        this.bakPos.setX(pos.getX());
        this.bakPos.setY(pos.getY());
    }

    public TETile getBackedTile() {
        return this.bakTile;
    }

    public void setBackedTile(TETile tile) {
        this.bakTile = tile;
    }

    public RoomGraph getRoomGraph() {
        return this.rg;
    }

    public void setRoomGraph(RoomGraph rg) {
        this.rg = rg;
    }
}
