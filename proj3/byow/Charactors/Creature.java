package byow.Charactors;

import byow.Articles.Item;
import byow.Attribute.Direction;
import byow.Attribute.Position;
import byow.Attribute.Size;
import byow.Core.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.LinkedList;

import static byow.Utils.TileUtils.isTileType;

public class Creature implements Serializable {
    private int damage;
    private int health;
    private int age;
    private int weight;
    private Size size;
    private int affection;
    private Position position;
    LinkedList<Item> inventory;
    //private Position bakPos;
    //private TETile bakTile;
    private GameState mgs;

    public Creature() {
        this.damage = 0;
        this.health = 0;
        this.age = 0;
        this.weight = 0;
        this.size = new Size(0, 0);
        this.affection = 0;
        this.position = new Position(0, 0);
        this.inventory = new LinkedList<>();
        //this.refWorld = null;
        //this.bakPos = null;
        //this.bakTile = null;
    }

    public Creature(GameState gameState) {
        this.damage = 0;
        this.health = 0;
        this.age = 0;
        this.weight = 0;
        this.size = null;
        this.affection = 0;
        this.position = null;
        this.inventory = new LinkedList<>();
        //gameState.tmDB.put(this.getPosition(), this);
        //this.bakPos = Position.copyOf(pos);
        //this.bakTile = Tileset.NOTHING;
        this.mgs = gameState;
    }

    public Creature(Position posSrc, Position posDst, Direction dir, GameState gameState) {
        this.damage = 0;
        this.health = 0;
        this.age = 0;
        this.weight = 0;
        this.size = null;
        this.affection = 0;
        this.position = null;
        this.inventory = new LinkedList<>();
        //gameState.tmDB.put(this.getPosition(), this);
        //this.bakPos = Position.copyOf(pos);
        //this.bakTile = Tileset.NOTHING;
        this.mgs = gameState;
    }

    public Creature(int damage, int health, int age, int weight, Size size, int affection, Position pos, GameState gameState) {
        this.damage = damage;
        this.health = health;
        this.age = age;
        this.weight = weight;
        this.size = Size.copyOf(size);
        this.affection = affection;
        this.position = Position.copyOf(pos);
        this.inventory = new LinkedList<>();
        gameState.tmDB.put(this.getPosition(), this);
        //this.bakPos = Position.copyOf(pos);
        //this.bakTile = Tileset.NOTHING;
        this.mgs = gameState;
    }

    public boolean MoveOneStep(Direction dir, TETile[][] mWorld) {

        // shift one sep
        Position pos = this.getPosition();
        //this.setBackedPosition(Position.copyOf(pos));
        //this.setBackedTile(refWorld[pos.getX()][pos.getY()]);

        Direction.shiftPosition(pos, dir);

        // check if the new position is OK
        if (isTileType(pos, Tileset.WALL, mWorld)) {
            System.out.println("you a running into a wall, it is not allowed!");
            return false;
        }
        else if (isTileType(pos, Tileset.FLOOR, mWorld)) {
            //this.mgs.tmDB.remove(this.getBackedPosition(), this);
            this.setPosition(pos);
            //this.mgs.tmDB.put(this.getPosition(), this);
            return true;
        }
        else if (isTileType(pos, Tileset.UNLOCKED_DOOR, mWorld)) {
            //this.mgs.tmDB.remove(this.getBackedPosition(), this);
            this.setPosition(pos);
            //this.mgs.tmDB.put(this.getPosition(), this);
            return true;
        }
        else {
            // rule 1: can not run into a wall
            // rule 2: can only run onto a floor
            System.out.println("undefined!");
            return false;
        }
    }

    public void MoveTo(Position posDst, TETile[][] mWorld) {

        if (posDst == null) {
            return;
        }

        // check if the new position is OK
        if (isTileType(posDst, Tileset.WALL, mWorld)) {
            System.out.println("you a running into a wall, it is not allowed!");
        }
        else if (isTileType(posDst, Tileset.FLOOR, mWorld)) {
            //this.mgs.tmDB.remove(this.getBackedPosition(), this);
            this.setPosition(posDst);
            //this.mgs.tmDB.put(this.getPosition(), this);
        }
        else if (isTileType(posDst, Tileset.UNLOCKED_DOOR, mWorld)) {
            //this.mgs.tmDB.remove(this.getBackedPosition(), this);
            this.setPosition(posDst);
            //this.mgs.tmDB.put(this.getPosition(), this);
        }
        else {
            // rule 1: can not run into a wall
            // rule 2: can only run onto a floor
            System.out.println("undefined!");
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

    public void setSize(Size s) {
        this.size.setW(s.getW());
        this.size.setH(s.getH());
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

    /**
     * how this item handle other objects, such as a key to a door.
     * */
    boolean handle(Object o) {
        return true;
    }
}
