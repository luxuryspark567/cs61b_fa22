package byow.Charactors;

import byow.Articles.*;
import byow.Attribute.Direction;
import byow.Attribute.Position;
import byow.Attribute.Size;
import byow.Core.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.CommandMonitor;

import java.util.LinkedList;

import static byow.Core.Main.gameState;
import static byow.Utils.TileUtils.isTileType;
import static byow.Core.Main.engine;
public class Avatar extends Creature{
    private static final int DAMAGE_DFT = 10;

    private static final int HEALTH_DFT = 10;
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


    private void checkObject(Position pos, TETile tileType) {
        if (isTileType(pos, tileType, gameState.world)) {
            // get the object at "pos" from database;
            Object o = this.mgs.tmDB.get(pos);
            if (o != null) {
                this.handle(o);
            }
            else {
                System.out.println("there is no valid object at" + pos);
            }
        }
    }

    @Override
    public boolean MoveOneStep(Direction dir, TETile[][] mWorld) {

        if (dir == null) {
            return false;
        }
        // Add some extended abilities for Avtar
        // shift one sep
        Position pos = this.getPosition();
        //this.setBackedPosition(Position.copyOf(pos));
        //this.setBackedTile(refWorld[pos.getX()][pos.getY()]);

        Direction.shiftPosition(pos, dir);

        checkObject(pos, Tileset.LOCKED_DOOR);
        checkObject(pos, Tileset.LAMP);

        return (super.MoveOneStep(dir, gameState.world));
        /*
        // action 1: if come to a locked door
        if (isTileType(pos, Tileset.LOCKED_DOOR, this.world)) {
            //get the door instance, corresponds to this locked door

            // 2, get the object at "pos" from database;
            Object o = this.mgs.tmDB.get(pos);
            if (o != null) {
                return this.handle(o);
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
        d
         */
    }

    @Override
    public boolean handle(Object o) {
        if (o instanceof Door d){
            // search the inventory for a matched key
            for (Key key :keyInventory) {
                if (key.getSignature() == d.getSiginature()) {
                    System.out.println("found a matched key!");

                    // add monitor, which will listen commands typed by user;
                    CommandMonitor cMonitor = new CommandMonitor(engine, mgs);
                    cMonitor.initiate();

                    // 1, pops up action selection menu
                    //engine.ter.renderInitialize(); //re init the menu
                    engine.ter.renderKeyMenu();

                    // 2, listening user's option, for user to choose an action;
                    cMonitor.monitorSubMenu(engine.ter);

                    // 3, perform the action, and render the result
                    cMonitor.executeKeyCommands(d, gameState.world, engine.ter);

                    // 4, return result
                    // if a new menu is popped ,should re-initiate the canvas
                    //engine.ter.renderInitialize();
                    return true;
                }
            }
            System.out.println("unmatched key!");
            return false;
        }

        else if (o instanceof Lamp lamp) {
            // TODO
            // add monitor, which will listen commands typed by user;
            CommandMonitor cMonitor = new CommandMonitor(engine, mgs);
            cMonitor.initiate();

            // 1, pops up action selection menu
            //engine.ter.renderInitialize(); //re init the menu
            engine.ter.renderLampMenu();

            // 2, listening user's option, for user to choose an action;
            cMonitor.monitorSubMenu(engine.ter);

            // 3, perform the action, and render the result
            cMonitor.executeLampCommands(lamp, gameState.world, engine.ter);

            // 4, return result
            return true;
        }

        return false;
    }
}
