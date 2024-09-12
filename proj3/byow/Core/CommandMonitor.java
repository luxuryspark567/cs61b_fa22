package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.TreeMap;

import static byow.Core.Engine.ROOM_NUM;
import static byow.Core.Main.engine;
import static byow.Core.Main.gameState;
import static byow.Core.TileUtils.paintTile;

public class CommandMonitor {
    int parseState;
    StringBuilder randomKey;
    CommandNode cn;
    Engine engine;
    GameState gameState;
    public CommandMonitor(Engine engine, GameState gameState) {
        this.parseState = 0;
        this.randomKey = new StringBuilder();
        this.cn = new CommandNode(ByowCommandSet.IDLE);
        this.gameState = gameState;
        this.engine = engine;
    }
    public void initiate() {
        this.parseState = 0;
        this.randomKey = new StringBuilder();
        this.cn = new CommandNode(ByowCommandSet.IDLE);
    }

    // monitor the menu page one time, return a valid command
    public void monitorMenuPage (TERenderer ter) {

        boolean looperFlag = true;

        while (looperFlag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                System.out.println(c);

                switch (parseState) {
                    case 0: // idle state
                        if (c == 'l' || c == 'L') {
                            cn = new CommandNode(ByowCommandSet.LOAD);
                            looperFlag = false;
                        }
                        else if (c == 'n' || c == 'N') {
                            randomKey.delete(0, randomKey.length());
                            //ter.renderClear(); // clear the canvas.
                            parseState = 1;
                        }
                        else if (c == 'q' || c == 'Q') {
                            cn = new CommandNode(ByowCommandSet.QUIT_AND_SAVE_GAME);
                            looperFlag = false;
                        }
                        break;
                    case 1: //create new world command analysis
                        if (c >= '0' && c <= '9') {
                            randomKey.append(c);
                        } else {
                            if (c == 's' || c == 'S') {
                                // got a valid random key
                                if (randomKey.isEmpty()) {
                                    cn = new CommandNode(ByowCommandSet.CREATE_NEW_WORLD);
                                    looperFlag = false;
                                } else {
                                    cn = new CommandNode(ByowCommandSet.CREATE_NEW_WORLD, Long.parseLong(randomKey.toString()));
                                    looperFlag = false;
                                }
                            } else {
                                randomKey.delete(0, randomKey.length());
                                System.out.println("invalid create new world command!");
                            }
                            parseState = 0;
                        }
                        break;
                    default:
                        parseState = 0;
                        break;
                }

                //render
                if (parseState == 1 && cn.bc == ByowCommandSet.IDLE) {
                    //ter.initialize(40, 40, 0, 0);
                    ter.renderRandomInputPage(randomKey.toString());
                    //ter.renderText();
                }
            }
        }
    }

    public void commandClear() {
        cn.bc = ByowCommandSet.IDLE;
        cn.rNum = -1;
    }
    // monitor game page on time, return a valid command
    public void monitorGamePage() {

        boolean looperFlag = true;

        while (looperFlag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                System.out.println(c);

                switch (parseState) {
                    case 0: // idle state
                        if (c == 'l' || c == 'L') {
                            cn = new CommandNode(ByowCommandSet.LOAD2);
                            looperFlag = false;
                        }
                        else if (c == 'w' || c == 'W') {
                            cn = new CommandNode(ByowCommandSet.MOVE_NORTH);
                            looperFlag = false;
                        }
                        else if (c == 'a' || c == 'A') {
                            cn = new CommandNode(ByowCommandSet.MOVE_WEST);
                            looperFlag = false;
                        }
                        else if (c == 's' || c == 'S') {
                            cn = new CommandNode(ByowCommandSet.MOVE_SOUTH);
                            looperFlag = false;
                        }
                        else if (c == 'd' || c == 'D') {
                            cn = new CommandNode(ByowCommandSet.MOVE_EAST);
                            looperFlag = false;
                        }
                        else if (c == ':') {
                            parseState = 2;
                        }
                        break;
                    case 2:
                        if (c == 'q' || c == 'Q') {
                            cn = new CommandNode(ByowCommandSet.QUIT_AND_SAVE_GAME2);
                            looperFlag = false;
                        } else {
                            System.out.println("invalid quit & save command!");
                        }
                        parseState = 0;
                        break;
                    default:
                        parseState = 0;
                        break;
                }
            }

            engine.ter.renderGamePage(gameState.world);
            engine.ter.renderTileInfo();
            engine.ter.renderPause(50);

        }
    }

    public void updateWorldAndRender(Engine engine, GameState gameState, Direction dir) {
        if (dir != null) {
            gameState.hero.MoveOneStep(dir);
            engine.ter.paintCreature(gameState.hero, Tileset.AVATAR, gameState.world);
            gameState.bear.huntHero(gameState.hero.getPosition());
            engine.ter.paintCreature(gameState.bear, Tileset.GANON, gameState.world);
        }
        engine.ter.renderGamePage(gameState.world);
    }

    /**
     * Uses serialization to create a copy of the given Random, needed for
     * repeatability in some tests.
     */

    // the boolean indicates if the command executed is a killer command: to terminate the program
    public boolean executeCommands(Engine engine, GameState gameState) {
        // return true means not a command to break out
        // return false means a command to break outer while loop, and no need to monitor anymore.

        if (cn == null) {
            return false;
        }

        if (cn.bc == ByowCommandSet.LOAD) { // in menu page
            if (Engine.loadGameState("savefile.txt")) {
                Engine.RANDOM = new Random(gameState.randomSeed);
                // in menu page, if game is reloaded should jump out of the menu loop
                gameState.readyToPlay = true;
                return true;
            }
            else {
                engine.ter.renderLoadFailPage();
                engine.ter.renderPause(500);
                engine.ter.renderMenuPage();//return to menu page
                return false;
            }
        }
        if (cn.bc == ByowCommandSet.LOAD2) { // in game page
            //ter.initialize(40, 40, 0, 0);
            //engine.ter.renderText("LOAD GAME");
            // TODO: this is not the most efficient way
            if (Engine.loadGameState("savefile.txt")) {
                Engine.RANDOM = new Random(gameState.randomSeed);
                // in game page, if game is reloaded should NOT jump out of the game loop
                gameState.readyToPlay = true;
                return false;
            }
            else {
                engine.ter.renderLoadFailPage();
                engine.ter.renderPause(500);
                engine.ter.renderInitialize();
                updateWorldAndRender(engine, gameState, null);
                return false;
            }
        }
        else if (cn.bc == ByowCommandSet.QUIT_AND_SAVE_GAME) { // in menu page

            //just quit
            //engine.ter.renderPause(500);
            engine.ter.renderText("GAME OVER");
            return true;
/*
            //Engine.GameState gState = new Engine.GameState(rg, world, refWorld, hero, bear);
            if (Engine.saveGameState("savefile.txt")) {
                engine.ter.renderText("GAME OVER");
                return true;
            }
            else {
                engine.ter.renderSaveFailPage();
                engine.ter.renderPause(500);
                engine.ter.renderMenuPage();//return to menu page
                return false;
            }

 */
        }
        else if (cn.bc == ByowCommandSet.QUIT_AND_SAVE_GAME2) { // in game page

            //Engine.GameState gState = new Engine.GameState(rg, world, refWorld, hero, bear);
            if (Engine.saveGameState("savefile.txt")) {
                engine.ter.renderText("GAME OVER");
                return true;
            }
            else {
                engine.ter.renderSaveFailPage();
                engine.ter.renderPause(500);
                updateWorldAndRender(engine, gameState, null);
                return false;
            }
        }
        else if (cn.bc == ByowCommandSet.CREATE_NEW_WORLD) {
            //ter.initialize(40, 40, 0, 0);
            engine.ter.renderText("CREATING NEW WORLD...");
            engine.ter.renderPause(500);
            // generate world data base (rooms and tunnels)
            //engine.SEED = cn.rNum;
            Engine.RANDOM = new Random(cn.rNum);
            gameState.randomSeed = cn.rNum;

            WorldGenerateUtils wgu = new WorldGenerateUtils();
            wgu.generateRooms(gameState);
            wgu.generateDoors(gameState);
            wgu.generateHallways(gameState);

            Avatar hero = new Avatar(Position.getRandomPositionInRandomRoom(gameState), gameState);
            Bear bear = new Bear(Position.getRandomPositionInRandomRoom(gameState), gameState);
            Key key1 = new Key(gameState);
            hero.pickUpKey(key1);

            gameState.hero = hero;
            gameState.bear = bear;
            gameState.readyToPlay = true;
            return true;
        }
        else if (cn.bc == ByowCommandSet.MOVE_NORTH) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                updateWorldAndRender(engine, gameState, Directionset.NORTH);
            }
        }
        else if (cn.bc == ByowCommandSet.MOVE_WEST) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                updateWorldAndRender(engine, gameState, Directionset.WEST);
            }
        }
        else if (cn.bc == ByowCommandSet.MOVE_SOUTH) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                updateWorldAndRender(engine, gameState, Directionset.SOUTH);
            }
        }
        else if (cn.bc == ByowCommandSet.MOVE_EAST) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                updateWorldAndRender(engine, gameState, Directionset.EAST);
            }
        }
        return false;
    }

    boolean isMoveCommand() {
        return this.cn.bc == ByowCommandSet.MOVE_NORTH
                || this.cn.bc== ByowCommandSet.MOVE_WEST
                || this.cn.bc == ByowCommandSet.MOVE_SOUTH
                || this.cn.bc == ByowCommandSet.MOVE_EAST;
    }

    boolean isIdleCommand() {
        return this.cn.bc == ByowCommandSet.IDLE;
    }



    public void monitorKeyMenu (TERenderer ter) {

        boolean looperFlag = true;

        while (looperFlag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                System.out.println(c);

                switch (parseState) {
                    case 0: // idle state
                        if (c == '1') {
                            cn = new CommandNode(ByowCommandSet.LOCK);
                            looperFlag = false;
                        }
                        else if (c == '2') {
                            cn = new CommandNode(ByowCommandSet.UNLOCK);
                            looperFlag = false;
                        }
                        else{
                            System.out.println("invalid option!!");
                        }
                        parseState = 0;
                        break;
                    default:
                        parseState = 0;
                        break;
                }
            }
        }
    }

    public void executeKeyCommands(Key key, Door door, TETile[][] world, TETile[][] refWorld, TERenderer ter) {
        // return true means not a command to break out
        // return false means a command to break outer while loop, and no need to monitor anymore.

        if (cn == null) {
            return;
        }

        if (cn.bc == ByowCommandSet.LOCK) {
            if (door != null) {
                // both the ref word and real world need to be checked
                paintTile(door.getPosition(), Tileset.LOCKED_DOOR, world);
                paintTile(door.getPosition(), Tileset.LOCKED_DOOR, refWorld);
                ter.renderGamePage(world);
            }
        }
        else if (cn.bc == ByowCommandSet.UNLOCK){
            if (door != null) {
                paintTile(door.getPosition(), Tileset.UNLOCKED_DOOR, world);
                paintTile(door.getPosition(), Tileset.UNLOCKED_DOOR, refWorld);
                ter.renderGamePage(world);
            }
        }
    }


}