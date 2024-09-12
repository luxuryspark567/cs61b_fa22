package byow.Core;

import byow.TileEngine.Tileset;

import java.util.LinkedList;

import static byow.Core.TileUtils.isTileType;

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

    private LinkedList<Key> keyInventory;
    private GameState mgs;

    public Avatar(Position pos, GameState gameState) {

        super(DAMAGE_DFT, HEALTH_DFT, AGE_DFT, WEIGHT_DFT, SIZE, AFFECTION_DFT, pos, gameState);

        this.helmet = null;
        this.chestPlate = null;
        this.leggings = null;
        this.boots = null;

        // weapon at hand
        this.weapon = null;

        this.helmetInventory = new LinkedList<>();
        this.chestPlateInventory = new LinkedList<>();
        this.leggingsInventory = new LinkedList<>();
        this.bootsInventory = new LinkedList<>();
        this.weaponInventory = new LinkedList<>();
        this.keyInventory = new LinkedList<>();
        this.mgs = gameState;
    }

    public Avatar(int damage, int health, int age, int weight, Size size, int affection, Position pos, GameState gameState) {
        super(damage, health, age, weight, size, affection, pos, gameState);
        this.helmet = null;
        this.chestPlate = null;
        this.leggings = null;
        this.boots = null;

        // weapon at hand
        this.weapon = null;

        this.helmetInventory = new LinkedList<>();
        this.chestPlateInventory = new LinkedList<>();
        this.leggingsInventory = new LinkedList<>();
        this.bootsInventory = new LinkedList<>();
        this.weaponInventory = new LinkedList<>();
        this.keyInventory = new LinkedList<>();
        this.mgs = gameState;
    }

    public boolean pickUpKey(Key key) {
        if (key == null) {
            return false;
        }
        this.keyInventory.add(key);
        return true;
    }

    @Override
    public boolean MoveOneStep(Direction dir) {

        if (dir == null) {
            return false;
        }
        // Add some extended abilities for Avtar
        // shift one sep
        Position pos = this.getPosition();
        this.setBackedPosition(Position.copyOf(pos));
        this.setBackedTile(refWorld[pos.getX()][pos.getY()]);

        Direction.shiftPosition(pos, dir);


        // action 1: if come to a locked door
        if (isTileType(pos, Tileset.LOCKED_DOOR, this.world)) {
            //get the door instance, corresponds to this locked door

            // 2, get the object at "pos" from database;
            Object o = this.mgs.tmDB.get(pos);
            if (o instanceof Door d) {

                // search the inventory for a matched key
                for (Key key :keyInventory) {
                    if (key.getSignature() == d.getSiginature()) {
                        System.out.println("found a matched key!");
                        key.handle(d);// let the key handle the door
                        return true;
                    }
                }
                return false;
            }
            else {
                System.out.println("there is no valid object at" + pos);
                return false;
            }
        }
        else {
            // call the general move
            return (super.MoveOneStep(dir));
        }
    }
}
