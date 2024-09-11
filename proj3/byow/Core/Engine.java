package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
//import edu.princeton.cs.introcs.StdDraw;

import java.util.*;
import java.util.List;

import static byow.Core.CommandNode.getCommandList;


public class Engine {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    //public static final int WIDTH_CANVAS = 80;
    //public static final int HEIGHT_CANVAS = 40;
    public static final int ROOM_NUM = 15;
    public static final int ROOM_WIDTH_MAX = 30;
    public static final int ROOM_HEIGHT_MAX = 10;
    public static final int ROOM_WIDTH_MIN = 5;
    public static final int ROOM_HEIGHT_MIN = 5;
    private static long SEED = 19900219;
    // limit the length of the tunnel, this is very important to find the best route,
    // because if the current road is too much, should try another way.
    // and because a MPQ is used to find the closest pair of nodes, there is a high chance that
    // try another route would be better.
    // but there surely is a chance that no route could be found to meet the limit, but I accept that
    // if a route is too long, I would rather not to go there.
    public static final int LOOP_LIMIT = WIDTH * HEIGHT * 3 / 10;

    public static final int DIRECTION_NUM = 4; // north, west, south and east
    public static Random RANDOM = new Random(SEED);
    TERenderer ter = new TERenderer();
    RoomGraph rg = new RoomGraph(WIDTH, HEIGHT);

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // generate world
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        // add monitor, which will listen commands typed by user;
        CommandMonitor cMonitor = new CommandMonitor();

        // render menu page
        ter.initialize(WIDTH, HEIGHT, 0, 0);
        ter.renderMenuPage();

        // listening menu page
        cMonitor.initiate();
        cMonitor.monitorMenuPage(ter);
        // the start menu should never execute any command that uses a "refWorld"
        cMonitor.executeCommands(null, null, world, null, ter);

        // render game page

        // generate world data base (rooms and tunnels)
        SEED = cMonitor.cn.rNum;
        RANDOM = new Random(SEED);
        TETile[][] refWorld = rg.initiate(world);

        Avatar hero = new Avatar(Position.getRandomPositionInRandomRoom(rg), rg, world, refWorld);
        Bear bear = new Bear(Position.getRandomPositionInRandomRoom(rg), rg, world, refWorld);
        Key key1 = new Key(ter, world, refWorld);
        hero.pickUpKey(key1);

        ter.initialize(WIDTH, HEIGHT, 0, 0);
        ter.renderCreature(hero, Tileset.AVATAR, world);
        ter.renderCreature(bear, Tileset.GANON, world);
        ter.renderGamePage(world);

        // listening game page;
        cMonitor.initiate();

        while (cMonitor.isIdleCommand() || cMonitor.isMoveCommand()) {
            cMonitor.initiate();
            cMonitor.monitorGamePage();
            cMonitor.executeCommands(hero, bear, world, refWorld, ter);
            System.out.println(rg.environmentDatabase);

        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        // parse the input
        List<CommandNode> commands = getCommandList(input);

        // add avatar
        Avatar hero = null;

        // generate world
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];

        for (CommandNode cn: commands) {

            if (cn.bc == ByowCommandSet.CREATE_NEW_WORLD) {
                // create the world
                SEED = cn.rNum;
                RANDOM = new Random(SEED);
                rg.initiate(finalWorldFrame);

                // should add avatars after the world is generated
                hero = new Avatar(Position.getRandomPositionInRandomRoom(rg), rg, finalWorldFrame, TETile.copyOf(finalWorldFrame));
            }
            else if (cn.bc == ByowCommandSet.QUIT_AND_SAVE_GAME) {

            }
            else if (cn.bc == ByowCommandSet.MOVE_NORTH) {
                if (hero != null) {
                    hero.MoveOneStep(Directionset.NORTH);
                }
            }
            else if (cn.bc == ByowCommandSet.MOVE_WEST) {
                if (hero != null) {
                    hero.MoveOneStep(Directionset.WEST);
                }
            }
            else if (cn.bc == ByowCommandSet.MOVE_SOUTH) {
                if (hero != null) {
                    hero.MoveOneStep(Directionset.SOUTH);
                }
            }
            else if (cn.bc == ByowCommandSet.MOVE_EAST) {
                if (hero != null) {
                    hero.MoveOneStep(Directionset.EAST);
                }
            }
            else if (cn.bc == ByowCommandSet.LOAD) {

            }
        }

        // print avatar
        if (hero != null) {
            ter.paintCreature(hero, Tileset.AVATAR, finalWorldFrame);
        }

        //debug, fill empty
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (finalWorldFrame[i][j] == null) {
                    finalWorldFrame[i][j] = Tileset.NOTHING;
                }
            }
        }

        return finalWorldFrame;
    }
    public static void main(String[] s) {
        // get engine, it would generate a world save in 2D array
        Engine eng = new Engine();
        TETile[][] tr = eng.interactWithInputString("n1234s");

        // initial default size render and print the world on that canvas (renderer)
        eng.ter.initialize(WIDTH, HEIGHT, 0, 0);
        eng.ter.renderFrame(tr);
    }
}
