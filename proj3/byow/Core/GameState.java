package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.EdgeWeightedGraph;

import java.io.*;
import java.util.List;
import java.util.TreeMap;

import static byow.Core.Main.gameState;

public class GameState implements Serializable {

    // 1, Environment Status
    public TETile[][] world; // pixel world
    public TETile[][] refWorld;
    public List<Room> roomLut;// 1.1 all rooms
    public EdgeWeightedGraph ewg;// 1.2 graph with edges to save rooms and tunnels (back up only)
    public TreeMap<Position, Object> tmDB;// 1.3 TreeMap which saves the position of every interactive Objects (doors, avatars, enemys)
    public Avatar hero;
    public Bear bear;

    public GameState(TETile[][] world, List<Room> roomLut, EdgeWeightedGraph ewg, TreeMap<Position, Object> tmDB) {
        this.world = world;
        this.refWorld = null;
        this.roomLut = roomLut;
        this.ewg = ewg;
        this.tmDB = tmDB;
    }

    public static void saveGameState(String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static GameState loadGameState(String filePath) {
        GameState gameState = null;
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            gameState = (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return gameState;
    }
}
