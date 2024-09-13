package byow.Core;

import byow.Articles.Room;
import byow.Attribute.Position;
import byow.Charactors.Avatar;
import byow.Charactors.Bear;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.EdgeWeightedGraph;

import java.io.*;
import java.util.List;
import java.util.TreeMap;

//import static byow.Core.Main.gameState;

public class GameState implements Serializable {

    // 1, Environment Status
    public TETile[][] world; // pixel world
    public TETile[][] refWorld;
    public List<Room> roomLut;// 1.1 all rooms

    // 1.1 all door, have to save doors in a persistent structure, because door is structure, if not saved in a persistent
    // structure, it will be lost after saved, because int tmDB only pointers are saved, not the data
    //public List<Door> doorLut;

    public EdgeWeightedGraph ewg;// 1.2 graph with edges to save rooms and tunnels (back up only)
    public TreeMap<Position, Object> tmDB;// 1.3 TreeMap which saves the position of every interactive Objects (doors, avatars, enemys)
    public Avatar hero;
    public Bear bear;
    public long randomSeed;
    public boolean readyToPlay;

    public boolean seeOutOfSightSwitch;
    public GameState(TETile[][] world, List<Room> roomLut, TreeMap<Position, Object> tmDB) {
        this.world = world;
        this.refWorld = null;
        this.roomLut = roomLut;
        //this.doorLut = doorLut;
        this.tmDB = tmDB;
        this.readyToPlay = true;
        this.seeOutOfSightSwitch = true;
    }
    /*
    public GameState(TETile[][] world, List<Room> roomLut, EdgeWeightedGraph ewg, TreeMap<Position, Object> tmDB) {
        this.world = world;
        this.refWorld = null;
        this.roomLut = roomLut;
        this.ewg = ewg;
        this.tmDB = tmDB;
    }
     */
}
