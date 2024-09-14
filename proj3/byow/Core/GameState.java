package byow.Core;

import byow.Articles.Room;
import byow.Attribute.Position;
import byow.Charactors.Avatar;
import byow.Charactors.Bear;
import byow.TileEngine.TETile;
import byow.Utils.WorldInfoDisplay;
import edu.princeton.cs.algs4.EdgeWeightedGraph;

import java.io.*;
import java.util.List;
import java.util.TreeMap;

//import static byow.Core.Main.gameState;

public class GameState implements Serializable {

    // 1, Environment Status
    public TETile[][] world; // pixel world
    //public TETile[][] refWorld;
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
    public boolean enableMist;
    public boolean enableChaseTrace;
    public boolean enableMouseTrace;
    public int refreshWorld;

    Position posHoverMouse;

    WorldInfoDisplay wid;

    public void setHoverMousePosition(Position pos) {
        this.posHoverMouse = Position.copyOf(pos);
    }
    public Position getMouseHoverHoveredPosition() {
        return this.posHoverMouse;
    }
    public GameState(TETile[][] world, List<Room> roomLut, TreeMap<Position, Object> tmDB) {
        this.world = world;
        this.roomLut = roomLut;
        //this.doorLut = doorLut;
        this.tmDB = tmDB;
        this.readyToPlay = true;
        this.enableMist = false;
        this.enableChaseTrace = true;
        this.enableMouseTrace = true;
        this.refreshWorld = 1;
        this.wid = new WorldInfoDisplay();
    }

    public WorldInfoDisplay getWid() {
        return this.wid;
    }
    public void increaseRefreshWorldFlag(){
        this.refreshWorld++;
    }

    public void toggleMistSwitch() {
        this.enableMist = !this.enableMist;
    }
    public void toggleTraceChaseSwitch() {
        this.enableChaseTrace = !this.enableChaseTrace;
    }
    public void toggleMouseTraceSwitch() {
        this.enableMouseTrace = !this.enableMouseTrace;
    }
}
