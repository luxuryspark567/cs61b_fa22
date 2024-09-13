package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;

import static byow.Core.Direction.getRevertDir;
import static byow.Core.Direction.getShiftPosition;
import static byow.Core.Directionset.SOUTH;
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
    private Position bakPos;
    private TETile bakTile;
    private GameState mgs;

    public Creature() {
        super(null, null, null, null, null);
        this.damage = 0;
        this.health = 0;
        this.age = 0;
        this.weight = 0;
        this.size = new Size(0, 0);
        this.affection = 0;
        this.position = new Position(0, 0);
        this.inventory = new LinkedList<>();
        this.world = null;
        this.refWorld = null;
        this.bakPos = null;
        this.bakTile = null;
    }

    public Creature(int damage, int health, int age, int weight, Size size, int affection, Position pos, GameState gameState) {
        super(null, null, null, gameState.world, gameState.refWorld);
        this.damage = damage;
        this.health = health;
        this.age = age;
        this.weight = weight;
        this.size = Size.copyOf(size);
        this.affection = affection;
        this.position = Position.copyOf(pos);
        this.inventory = new LinkedList<>();
        gameState.tmDB.put(this.getPosition(), this);
        this.bakPos = Position.copyOf(pos);
        this.bakTile = Tileset.NOTHING;
        this.mgs = gameState;
    }

    public void arrangeSearchJob(Position posSrc, Position posDst) {
        initRoadSearch(posSrc, posDst, null);
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
            this.mgs.tmDB.remove(this.getBackedPosition(), this);
            this.setPosition(pos);
            this.mgs.tmDB.put(this.getPosition(), this);
            return true;
        }
        else if (isTileType(pos, Tileset.UNLOCKED_DOOR, this.world)) {
            this.mgs.tmDB.remove(this.getBackedPosition(), this);
            this.setPosition(pos);
            this.mgs.tmDB.put(this.getPosition(), this);
            return true;
        }
        else {
            // rule 1: can not run into a wall
            // rule 2: can only run onto a floor
            System.out.println("undefined!");
            return false;
        }
    }

    @Override
    public boolean[] checkSurroundings() {

        boolean[] dirBool = new boolean[] {true, true, true, true};

        Direction MoveBackDir = getRevertDir(this.getLastMoveDir());
        // 1, check every direction except the one you come from
        // to start with, you should not take the back direction where you just come from;
        Direction.setFalseBoolArrayByDirection(dirBool, MoveBackDir);

        Position posCurNorth = getShiftPosition(this.getPosCur(), Directionset.NORTH);
        Position posCurWest = getShiftPosition(this.getPosCur(), Directionset.WEST);
        Position posCurSouth = getShiftPosition(this.getPosCur(), SOUTH);
        Position posCurEast = getShiftPosition(this.getPosCur(), Directionset.EAST);

        // new rule 1: creature should not not run into a locked door
        // check Door

        if (isTileType(posCurNorth, Tileset.LOCKED_DOOR, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.NORTH);
        }
        if (isTileType(posCurWest, Tileset.LOCKED_DOOR, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.WEST);
        }
        if (isTileType(posCurSouth, Tileset.LOCKED_DOOR, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, SOUTH);
        }
        if (isTileType(posCurEast, Tileset.LOCKED_DOOR, this.refWorld)) {
            Direction.setFalseBoolArrayByDirection(dirBool, Directionset.EAST);
        }

        return Direction.mergeDirection(dirBool, super.checkSurroundings());

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

    /**
     * how this item handle other objects, such as a key to a door.
     * */
    boolean handle(Object o) {
        return true;
    }
}
