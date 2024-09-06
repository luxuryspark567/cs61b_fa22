package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import static byow.Core.Engine.*;

// used to connect two rooms
public class Hallway {
    private Door src;
    private Door dst;

    private int w;

/*
    public Hallway(Door d1, Door d2, TETile[][] world) {
        // get most adjacent doors for two rooms, suppose the answer is r1.sd and r2.ed
        this.src = d1;
        this.dst = d2;
        // generate a hallway between d1 and d2
    }
*/
    public Hallway(Door d1, Door d2) {
        // get most adjacent doors for two rooms, suppose the answer is r1.sd and r2.ed
        this.src = d1;
        this.dst = d2;
        //this.tps = new TurnPoint[tps.length];
        //System.arraycopy(tps, 0, this.tps, 0, tps.length);
        // generate a hallway between d1 and d2
    }
    public Door getSrc() {
        return this.src;
    }

    public Door getDst() {
        return this.dst;
    }

    public int getWeight() {
        return this.w;
    }

    public void setSrc(Door d) {
        this.src = d;
    }

    public void setDst(Door d) {
        this.dst = d;
    }

    public void setWeight(int w) {
        this.w = w;
    }
}
