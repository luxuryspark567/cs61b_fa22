package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;

import static byow.Core.TileUtils.isTileType;
import static byow.Core.TileUtils.paintTile;

public class Avatar extends Creature{

    TETile[][] world;
    TETile[][] refWorld;
    private static final int DAMAGE_DFT = 10;

    private static final int HEALTH_DFT = 100;
    private static final int AGE_DFT = 20;
    private static final int WEIGHT_DFT = 90;
    private static final Size SIZE = new Size(1, 1);
    private static final int AFFECTION_DFT = 100;

    private Position bakPos;
    private TETile bakTile;
    // arms at hand
    private Arm helmet;
    private Arm chestPlate;
    private Arm leggings;
    private Arm boots;

    // weapon at hand
    private Weapon weapon;

    private LinkedList<Arm> helmetInventory;
    private LinkedList<Arm> chestPlateInventory;
    private LinkedList<Arm> leggingsInventory;
    private LinkedList<Arm> bootsInventory;
    private LinkedList<Weapon> weaponInventory;

    public Avatar(TETile[][] world, Position pos) {

        super(DAMAGE_DFT, HEALTH_DFT, AGE_DFT, WEIGHT_DFT, SIZE, AFFECTION_DFT, pos);

        this.world = world;

        this.refWorld = TETile.copyOf(world);// back up the world, and use the backup to search for routes.

        this.bakPos = null;
        this.bakTile = null;

        helmet = null;
        chestPlate = null;
        leggings = null;
        boots = null;

        // weapon at hand
        weapon = null;

        helmetInventory = new LinkedList<>();
        chestPlateInventory = new LinkedList<>();
        leggingsInventory = new LinkedList<>();
        bootsInventory = new LinkedList<>();
        weaponInventory = new LinkedList<>();
    }

    public Avatar(int damage, int health, int age, int weight, Size size, int affection, Position pos) {
        super(damage, health, age, weight, size, affection, pos);
    }

    public boolean MoveOneStep(Direction dir) {

        // shift one sep
        Position pos = this.getPosition();
        this.bakPos = Position.copyOf(pos);
        this.bakTile = refWorld[pos.x][pos.y];

        Direction.shiftPosition(pos, dir);

        //this.setPosition(pos);
        //return true;

        // check if the new position is OK
        if (isTileType(pos, Tileset.WALL, this.refWorld)) {
            System.out.println("you a running into a wall, it is not allowed!");
            return false;
        }
        else if (isTileType(pos, Tileset.FLOOR, this.refWorld)) {
            this.setPosition(pos);
            return true;
        }
        else if (isTileType(pos, Tileset.UNLOCKED_DOOR, this.refWorld)) {
            this.setPosition(pos);
            return true;
        }
        else {
            // rule 1: can not run into a wall
            // rule 2: can only run onto a floor
            System.out.println("undefined!");
            return false;
        }
    }
    public Position getBackedPosition() {
        return bakPos;
    }

    public TETile getBakedTile() {
        return bakTile;
    }
}
