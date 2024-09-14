package byow.Core;

import byow.Articles.Door;
import byow.Articles.Key;
import byow.Articles.Lamp;
import byow.Articles.Room;
import byow.Attribute.ByowCommandSet;
import byow.Attribute.CommandNode;
import byow.Attribute.Directionset;
import byow.Charactors.Avatar;
import byow.Charactors.Bear;
import byow.Core.Engine;
import byow.Core.GameState;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Utils.WorldGenerateUtils;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Random;

//import static byow.TileEngine.TERenderer.TILE_SIZE;


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
                if (parseState == 1 && cn.getByowCommand() == ByowCommandSet.IDLE) {
                    //ter.initialize(40, 40, 0, 0);
                    ter.renderRandomInputPage(randomKey.toString());
                    //ter.renderText();
                }
            }
        }
    }

    public void commandClear() {
        cn.setByowCommand(ByowCommandSet.IDLE);
        cn.setByowRandomNum(-1);
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
                        if ((c == 'k' || c == 'K')) {
                            cn = new CommandNode(ByowCommandSet.MIST_SWITCH);
                            looperFlag = false;
                        }
                        else if (c == 'l' || c == 'L') {
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


            gameState.getWid().updateMouseHoverTile();

            if (gameState.getWid().isTileInfoChanged()) {
                gameState.getWid().backUpMouseHoverTileInfo();
                gameState.increaseRefreshWorldFlag();
            }
            engine.ter.updateWorldAndRender(engine, gameState);
            //engine.ter.renderGamePage(gameState.world);
        }
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
        if (cn.getByowCommand() == ByowCommandSet.MIST_SWITCH) {
            gameState.toggleMistSwitch();
            gameState.increaseRefreshWorldFlag();
        }
        else if (cn.getByowCommand() == ByowCommandSet.LOAD) { // in menu page
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
        if (cn.getByowCommand() == ByowCommandSet.LOAD2) { // in game page
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
                gameState.increaseRefreshWorldFlag();
                //updateWorldAndRender(engine, gameState, null);
                return false;
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.QUIT_AND_SAVE_GAME) { // in menu page

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
        else if (cn.getByowCommand() == ByowCommandSet.QUIT_AND_SAVE_GAME2) { // in game page

            //Engine.GameState gState = new Engine.GameState(rg, world, refWorld, hero, bear);
            if (Engine.saveGameState("savefile.txt")) {
                engine.ter.renderText("GAME OVER");
                return true;
            }
            else {
                engine.ter.renderSaveFailPage();
                engine.ter.renderPause(500);
                gameState.increaseRefreshWorldFlag();
                //updateWorldAndRender(engine, gameState, null);
                return false;
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.CREATE_NEW_WORLD) {
            //ter.initialize(40, 40, 0, 0);
            engine.ter.renderText("CREATING NEW WORLD...");
            engine.ter.renderPause(500);
            // generate world data base (rooms and tunnels)
            //engine.SEED = cn.rNum;
            Engine.RANDOM = new Random(cn.getByowRandomNum());
            gameState.randomSeed = cn.getByowRandomNum();

            WorldGenerateUtils wgu = new WorldGenerateUtils();
            wgu.generateRooms(gameState);
            wgu.generateDoors(gameState);
            wgu.generateLamps(gameState);
            wgu.generateHallways(gameState);

            Avatar hero = new Avatar(Room.getRandomPositionInRandomRoom(gameState), gameState);
            Bear bear = new Bear(Room.getRandomPositionInRandomRoom(gameState), gameState);
            Key key1 = new Key(gameState);
            hero.pickUpKey(key1);

            gameState.hero = hero;
            gameState.bear = bear;
            gameState.readyToPlay = true;
            return true;
        }
        else if (cn.getByowCommand() == ByowCommandSet.MOVE_NORTH) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                //updateWorldAndRender(engine, gameState, Directionset.NORTH);
                gameState.hero.MoveOneStep(Directionset.NORTH, gameState.world);
                gameState.bear.huntHero(gameState.hero.getPosition());
                gameState.increaseRefreshWorldFlag();
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.MOVE_WEST) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                //updateWorldAndRender(engine, gameState, Directionset.WEST);
                gameState.hero.MoveOneStep(Directionset.WEST, gameState.world);
                gameState.bear.huntHero(gameState.hero.getPosition());
                gameState.increaseRefreshWorldFlag();
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.MOVE_SOUTH) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                //updateWorldAndRender(engine, gameState, Directionset.SOUTH);
                gameState.hero.MoveOneStep(Directionset.SOUTH, gameState.world);
                gameState.bear.huntHero(gameState.hero.getPosition());
                gameState.increaseRefreshWorldFlag();
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.MOVE_EAST) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                //updateWorldAndRender(engine, gameState, Directionset.EAST);
                gameState.hero.MoveOneStep(Directionset.EAST, gameState.world);
                gameState.bear.huntHero(gameState.hero.getPosition());
                gameState.increaseRefreshWorldFlag();
            }
        }
        return false;
    }

    boolean isMoveCommand() {
        return this.cn.getByowCommand() == ByowCommandSet.MOVE_NORTH
                || this.cn.getByowCommand()== ByowCommandSet.MOVE_WEST
                || this.cn.getByowCommand() == ByowCommandSet.MOVE_SOUTH
                || this.cn.getByowCommand() == ByowCommandSet.MOVE_EAST;
    }

    boolean isIdleCommand() {
        return this.cn.getByowCommand() == ByowCommandSet.IDLE;
    }



    public void monitorSubMenu (TERenderer ter) {

        boolean looperFlag = true;

        while (looperFlag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                System.out.println(c);

                switch (parseState) {
                    case 0: // idle state
                        if (c == '1') {
                            cn = new CommandNode(ByowCommandSet.OPTION_ONE);
                            looperFlag = false;
                        }
                        else if (c == '2') {
                            cn = new CommandNode(ByowCommandSet.OPTION_TWO);
                            looperFlag = false;
                        }
                        else if (c == '3') {
                            cn = new CommandNode(ByowCommandSet.OPTION_THREE);
                            looperFlag = false;
                        }
                        else if (c == '4') {
                            cn = new CommandNode(ByowCommandSet.OPTION_FOUR);
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

    public void executeKeyCommands(Door door, TETile[][] world, TERenderer ter) {
        // return true means not a command to break out
        // return false means a command to break outer while loop, and no need to monitor anymore.

        if (cn == null) {
            return;
        }

        if (cn.getByowCommand() == ByowCommandSet.OPTION_ONE) {
            if (door != null) {
                door.setType(Tileset.LOCKED_DOOR);
                gameState.increaseRefreshWorldFlag();
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.OPTION_TWO){
            if (door != null) {
                door.setType(Tileset.UNLOCKED_DOOR);
                gameState.increaseRefreshWorldFlag();
            }
        }
    }

    public void executeLampCommands(Lamp lamp, TETile[][] world, TERenderer ter) {
        // return true means not a command to break out
        // return false means a command to break outer while loop, and no need to monitor anymore.

        if (cn == null) {
            return;
        }

        if (cn.getByowCommand() == ByowCommandSet.OPTION_ONE) {
            if (lamp != null) {
                // both the ref word and real world need to be checked
                lamp.switchOff();
                //ter.renderGamePage(world);
            }
        }

        else if (cn.getByowCommand() == ByowCommandSet.OPTION_TWO){
            if (lamp != null) {
                lamp.switchOn();
                //ter.renderGamePage(world);
            }
        }
    }


}