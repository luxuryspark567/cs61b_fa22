package byow.Core;

import byow.Articles.Door;
import byow.Articles.Key;
import byow.Articles.Lamp;
import byow.Articles.Room;
import byow.Attribute.*;
import byow.Charactors.Avatar;
import byow.Charactors.Bear;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Utils.WorldGenerateUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static byow.Core.Main.engine;
import static byow.Core.Main.gameState;

//import static byow.TileEngine.TERenderer.TILE_SIZE;


public class CommandMonitor {
    int parseState;
    StringBuilder randomKey;
    CommandNode cn;

    boolean haveSequentialCommands; // could not be cleared in initiate()
    List<ByowCommand> commands;
    //Engine engine;
    //GameState gameState;

    public CommandMonitor() {
        this.parseState = 0;
        this.randomKey = new StringBuilder();
        this.cn = new CommandNode(ByowCommandSet.IDLE);
        this.haveSequentialCommands = false;
        this.commands = null;
        //this.gameState = gameState;
        //this.engine = engine;
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

    public List<ByowCommand> getCommandsFromRoute(List<Position> route) {
        if (route == null) {
            return null;
        }
        List<ByowCommand> cmds = new LinkedList<>();
        // get start point
        Position posSrc = route.removeFirst();

        for (Position posDst:route) {
            Direction dir = Direction.getDirFromPosition(posSrc, posDst);
            cmds.add(ByowCommand.getCommandFromDir(dir));
            // update posSrc
            posSrc = posDst;
        }

        return cmds;
    }

    // monitor game page on time, return a valid command
    public void monitorGamePage() {

        boolean looperFlag = true;

        while (looperFlag) {

            // 1, mouse clicked process
            // if there is a valid route and user clicked on the tile
            if (gameState.getWid().isMouseReleased()) {
                // 1.1 get mouse clicked coordinate
                Position posMouse = gameState.getWid().getMouseHoveredTilePosition();
                gameState.setHoverMousePosition(posMouse);

                // 1.2 decide mouse clicked respond
                // a) if clicked on a tile and the tile is in the route of hero
                List<Position> route = gameState.hero.getHuntRoute();
                if (route != null && route.getLast().equals(posMouse)) {
                    // clicked on the end of the route
                    // should enter a state to go to move step by step to the end.

                    // transfer route to sequence of command;
                    this.commands = getCommandsFromRoute(route);
                    gameState.hero.setHuntRoute(null);
                    this.haveSequentialCommands = true;
                } else {
                    // b) if clicked on an object, and the object is within the reach if the hero

                    // is the object is within the reach if the hero ?
                    if (Position.isWithinReach(gameState.hero.getPosition(), posMouse)) {

                        // is it an operational object
                        Object o = gameState.tmDB.get(posMouse);
                        if (o != null) {
                            gameState.hero.handle(o);
                        }
                    }
                }
            }

            if (haveSequentialCommands) {
                if (this.commands != null && !this.commands.isEmpty()) {
                    cn = new CommandNode(this.commands.removeFirst());
                    looperFlag = false;

                    // update world
                    engine.ter.renderPause(100);
                    engine.ter.updateWorldAndRender(engine, gameState);
                }
                else {
                    this.haveSequentialCommands = false;
                }
            }
            else {
                // 2, key pressed process
                if (StdDraw.hasNextKeyTyped()) {
                    char c = StdDraw.nextKeyTyped();
                    System.out.println(c);

                    switch (parseState) {
                        case 0: // idle state
                            if (c == 'z' || c == 'Z') {
                                cn = new CommandNode(ByowCommandSet.TURN_LEFT);
                                looperFlag = false;
                            }
                            else if (c == 'c' || c == 'C') {
                                cn = new CommandNode(ByowCommandSet.TURN_RIGHT);
                                looperFlag = false;
                            }
                            else if (c == 'k' || c == 'K') {
                                cn = new CommandNode(ByowCommandSet.MIST_SWITCH);
                                looperFlag = false;
                            }
                            else if (c == 'j' || c == 'J') {
                                cn = new CommandNode(ByowCommandSet.CHASE_TRACE_SWITCH);
                                looperFlag = false;
                            }
                            else if (c == 'm' || c == 'M') {
                                cn = new CommandNode(ByowCommandSet.MOUSE_TRACE_SWITCH);
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

                /*
                gameState.getWid().updateMouseHoverTile();

                if (gameState.getWid().isTileInfoChanged()) {
                    gameState.getWid().backUpMouseHoverTileInfo();
                    gameState.increaseRefreshWorldFlag();
                }

                gameState.hero.hunt(gameState.getMouseHoverHoveredPosition());

                if (gameState.hero.isHuntRouteChanged()) {
                    gameState.hero.setBakedHuntRoute();
                    gameState.increaseRefreshWorldFlag();
                }
*/
                engine.ter.updateWorldAndRender(engine, gameState);
                //engine.ter.renderGamePage(gameState.world);
            }

        }
    }


    /**
     * Uses serialization to create a copy of the given Random, needed for
     * repeatability in some tests.
     */

    // the boolean indicates if the command executed is a killer command: to terminate the program
    public boolean executeCommands(Engine engine) {
        // return true means not a command to break out
        // return false means a command to break outer while loop, and no need to monitor anymore.

        if (cn == null) {
            return false;
        }
        if (cn.getByowCommand() == ByowCommandSet.TURN_LEFT) {
            if (gameState.hero != null) {
                gameState.hero.setViewAngle(gameState.hero.getViewAngle() + Engine.VIEW_ANGLE_RESOLUTION);
                gameState.increaseRefreshWorldFlag();
            }

        }
        else if (cn.getByowCommand() == ByowCommandSet.TURN_RIGHT) {
            if (gameState.hero != null) {
                gameState.hero.setViewAngle(gameState.hero.getViewAngle() - Engine.VIEW_ANGLE_RESOLUTION);
                gameState.increaseRefreshWorldFlag();
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.MIST_SWITCH) {
            gameState.toggleMistSwitch();
            gameState.increaseRefreshWorldFlag();
        }
        else if (cn.getByowCommand() == ByowCommandSet.CHASE_TRACE_SWITCH) {
            gameState.toggleTraceChaseSwitch();
            gameState.increaseRefreshWorldFlag();
        }
        else if (cn.getByowCommand() == ByowCommandSet.MOUSE_TRACE_SWITCH) {
            gameState.toggleMouseTraceSwitch();
            gameState.increaseRefreshWorldFlag();
        }
        else if (cn.getByowCommand() == ByowCommandSet.LOAD) { // in menu page
            if (Engine.loadGameState("savefile.txt")) {
                Engine.RANDOM = new Random(gameState.randomSeed);
                // in menu page, if game is reloaded should jump out of the menu loop
                gameState.readyToPlay = true;
                gameState.increaseRefreshWorldFlag();
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
                gameState.increaseRefreshWorldFlag();
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
                gameState.hero.setViewAngle(Math.PI / 2);
                gameState.bear.hunt(gameState.hero.getPosition());
                gameState.increaseRefreshWorldFlag();
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.MOVE_WEST) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                //updateWorldAndRender(engine, gameState, Directionset.WEST);
                gameState.hero.MoveOneStep(Directionset.WEST, gameState.world);
                gameState.hero.setViewAngle(-Math.PI);
                gameState.bear.hunt(gameState.hero.getPosition());
                gameState.increaseRefreshWorldFlag();
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.MOVE_SOUTH) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                //updateWorldAndRender(engine, gameState, Directionset.SOUTH);
                gameState.hero.MoveOneStep(Directionset.SOUTH, gameState.world);
                gameState.hero.setViewAngle(-Math.PI / 2);
                gameState.bear.hunt(gameState.hero.getPosition());
                gameState.increaseRefreshWorldFlag();
            }
        }
        else if (cn.getByowCommand() == ByowCommandSet.MOVE_EAST) {
            if (gameState.hero != null) {
                //ter.initialize(WIDTH, HEIGHT, 0, 0);
                //updateWorldAndRender(engine, gameState, Directionset.EAST);
                gameState.hero.MoveOneStep(Directionset.EAST, gameState.world);
                gameState.hero.setViewAngle(0);
                gameState.bear.hunt(gameState.hero.getPosition());
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