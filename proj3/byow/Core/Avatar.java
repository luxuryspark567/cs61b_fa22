package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;

import static byow.Core.TileUtils.isTileType;
import static byow.Core.TileUtils.paintTile;

public class Avatar extends Creature{
    private static final int DAMAGE_DFT = 10;

    private static final int HEALTH_DFT = 100;
    private static final int AGE_DFT = 20;
    private static final int WEIGHT_DFT = 90;
    private static final Size SIZE = new Size(1, 1);
    private static final int AFFECTION_DFT = 100;

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

    public Avatar(Position pos, RoomGraph rg, TETile[][] world) {

        super(DAMAGE_DFT, HEALTH_DFT, AGE_DFT, WEIGHT_DFT, SIZE, AFFECTION_DFT, pos, rg, world);

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

    public Avatar(int damage, int health, int age, int weight, Size size, int affection, Position pos, TETile[][] world, RoomGraph rg) {
        super(damage, health, age, weight, size, affection, pos, rg, world);
    }
}
