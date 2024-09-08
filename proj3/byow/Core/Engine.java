package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Engine {
    TERenderer ter = new TERenderer();
    RoomGraph rg = new RoomGraph();

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int ROOM_NUM = 15;
    public static final int ROOM_WIDTH_MAX = 30;
    public static final int ROOM_HEIGHT_MAX = 10;
    public static final int ROOM_WIDTH_MIN = 5;
    public static final int ROOM_HEIGHT_MIN = 5;
    private static final long SEED = 21;

    // limit the length of the tunnel, this is very important to find the best route,
    // because if the current road is too much, should try another way.
    // and because a MPQ is used to find the closest pair of nodes, there is a high chance that
    // try another route would be better.
    // but there surely is a chance that no route could be found to meet the limit, but I accept that
    // if a route is too long, I would rather not to go there.
    public static final int LOOP_LIMIT = WIDTH * HEIGHT * 3 / 10;

    public static final int DIRECTION_NUM = 4; // north, west, south and east
    public static final Random RANDOM = new Random(SEED);

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
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

        // generate world
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];

        // create the world
        rg.initiate(finalWorldFrame);

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
        eng.ter.initialize(WIDTH, HEIGHT);
        eng.ter.renderFrame(tr);
    }
}
