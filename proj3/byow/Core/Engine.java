package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
//import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.*;
import java.util.List;

import static byow.Core.CommandNode.getCommandList;
import static byow.Core.TileUtils.paintTile;

public class Engine {
    TERenderer ter = new TERenderer();
    RoomGraph rg = new RoomGraph();

    /* Feel free to change the width and height. */
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    public static final int WIDTH_CANVAS = 80;
    public static final int HEIGHT_CANVAS = 40;
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
/*
    void listenForUserInput(TETile[][] world, Avatar hero) {

        // 0, idle
        // 1, "N###S": create new world
        // 2, ":Q": save and quite
        int parseState = 0;
        //char[] charArray = input.toCharArray();
        StringBuilder randomKey = new StringBuilder();
        //List<CommandNode> cmdList = new LinkedList<>();

        while (true) {
            if (ter.hasNextKeyTyped()) {
                // get the char
                char c = ter.nextKeyTyped();

                switch (parseState) {
                    case 0: // idle state
                        if (c == 'l' || c == 'L') {
                            //cmdList.add(new CommandNode(ByowCommandSet.LOAD));
                        }
                        else if (c == 'w' || c == 'W') {
                            System.out.println("Move North");
                        }
                        else if (c == 'a' || c == 'A') {
                            System.out.println("Move West");
                        }
                        else if (c == 's' || c == 'S') {
                            System.out.println("Move South");
                        }
                        else if (c == 'd' || c == 'D') {
                            System.out.println("Move East");
                        }
                        else if (c == 'n' || c == 'N') {
                            randomKey.delete(0, randomKey.length());
                            ter.displayClear();
                            parseState = 1;
                        }
                        else if (c == ':') {
                            parseState = 2;
                        }
                        break;
                    case 1: //create new world command analysis
                        if (c >= '0' && c <= '9') {
                            randomKey.append(c);
                            ter.displayUserInput(randomKey.toString());
                            System.out.println(randomKey);

                        } else {
                            if (c == 's' || c == 'S') {
                                // got a valid random key
                                if (randomKey.isEmpty()) {
                                    //cmdList.add(new CommandNode(ByowCommandSet.CREATE_NEW_WORLD));
                                } else {
                                    // create the world
                                    SEED = Integer.parseInt(randomKey.toString());
                                    RANDOM = new Random(SEED);
                                    rg.initiate(world);

                                    // should add avatars after the world is generated
                                    //hero = new Avatar(world, Position.getRandomPositionInRandomRoom(rg));
                                    //cmdList.add(new CommandNode(ByowCommandSet.CREATE_NEW_WORLD, Integer.parseInt(randomKey.toString())));
                                    // print avatar
                                    //if (hero != null) {
                                    //    hero.paintAvatar();
                                   // }

                                    //debug, fill empty
                                    for (int i = 0; i < WIDTH; i++) {
                                        for (int j = 0; j < HEIGHT; j++) {
                                            if (world[i][j] == null) {
                                                world[i][j] = Tileset.NOTHING;
                                            }
                                        }
                                    }

                                    // render the world
                                    ter.initialize(WIDTH, HEIGHT, 0, 0);
                                    ter.renderFrame(world);
                                }
                            } else {
                                randomKey.delete(0, randomKey.length());
                                System.out.println("invalid create new world command!");
                            }
                            parseState = 0;
                        }
                        break;
                    case 2:
                        if (c == 'q' || c == 'Q') {
                            //cmdList.add(new CommandNode(ByowCommandSet.QUIT_AND_SAVE_GAME));
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
        }
    }

 */
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

        // add avatar
        Avatar hero = null;

        // generate world
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        ter.initialize(WIDTH, HEIGHT, 0, 0);
        // show a menu, for user to select what to do.
        //ter.displayStarterMenu();

        // listen for user input
        //listenForUserInput(finalWorldFrame, hero);

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
                hero = new Avatar(finalWorldFrame, Position.getRandomPositionInRandomRoom(rg));
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
            hero.paintAvatar();
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
