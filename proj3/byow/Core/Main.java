package byow.Core;

import byow.Attribute.Position;
import byow.TileEngine.TETile;
import byow.Utils.TileUtils;

import java.util.ArrayList;
import java.util.TreeMap;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static GameState gameState;
    public static Engine engine;

    public static void main(String[] args) throws Exception {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            engine = new Engine();
            TETile[][] frame = engine.interactWithInputString(args[1]);

            // initial default size render and print the world on that canvas (renderer)
            engine.ter.renderInitialize();
            engine.ter.renderFrame(frame);
            System.out.println(engine.toString());
        } else {
            engine = new Engine();

            gameState = new GameState(new TETile[TileUtils.getTileWorldWidth()][TileUtils.getTileWorldHeight()],
                    new ArrayList<>(Engine.ROOM_NUM),
                    new TreeMap<Position, Object>(new Position.PositionComparator()));

            engine.interactWithKeyboard();
        }
    }
}
