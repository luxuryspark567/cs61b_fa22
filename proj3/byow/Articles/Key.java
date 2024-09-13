package byow.Articles;

import byow.Core.GameState;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.Serializable;

import static byow.Core.Main.engine;

public class Key extends Item implements Serializable {

    private static final int WEIGHT_DFT = 0;
    private static final int VALUE_DFT = 0;
    private static final int DURATION_DFT = 100;
    private static final int SIGNATURE_DFT = 1;

    private final int signature; // only if the key signature matches the door signature will it be able to open it

    TERenderer ter;
    TETile[][] world;
    TETile[][] refWorld;

    //Engine engine;
    GameState gameState;

    public Key(GameState gameState) {
        super(WEIGHT_DFT, VALUE_DFT, DURATION_DFT);
        this.signature = SIGNATURE_DFT;
        this.ter = engine.ter;
        this.world = gameState.world;
        this.refWorld = gameState.refWorld;
        this.gameState = gameState;
        //this.engine = engine;
    }

    public Key(int signature, GameState gameState) {
        super(WEIGHT_DFT, VALUE_DFT, DURATION_DFT);
        this.signature = signature;
        this.ter = engine.ter;
        this.world = gameState.world;
        this.refWorld = gameState.refWorld;
        this.gameState = gameState;
        //this.engine = engine;
    }

    public Key(int weight, int value, int dur, int signature, GameState gameState) {
        super(weight, value, dur);
        this.signature = signature;
        this.ter = engine.ter;
        this.world = gameState.world;
        this.refWorld = gameState.refWorld;
        this.gameState = gameState;
        //this.engine = engine;
    }

    public int getSignature() {
        return this.signature;
    }
    /**
     * a key to handle a door
     * @true:  could open the door
     * @false: cannot open the door
     */
/*
    @Override
    boolean handle(Object o) {
        if (o instanceof Door d){
            if (this.signature == d.getSiginature()) {
                // add monitor, which will listen commands typed by user;
                CommandMonitor cMonitor = new CommandMonitor(engine, gameState);
                cMonitor.initiate();

                // 1, pops up action selection menu
                ter.renderInitialize(); //re init the menu
                ter.renderKeyMenu();

                // 2, listening user's option, for user to choose an action;
                cMonitor.monitorKeyMenu(ter);

                // 3, perform the action, and render the result
                cMonitor.executeKeyCommands(this, d, world, refWorld, ter);

                // 4, return result
                // if a new menu is popped ,should re-initiate the canvas
                ter.renderInitialize();
                return true;
            }
            else {
                System.out.println("unmatched key!");
                return false;
            }

        }

        return false;
    }

 */
}

