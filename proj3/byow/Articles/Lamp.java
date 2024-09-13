package byow.Articles;

import byow.Core.GameState;
import byow.Attribute.Position;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.Serializable;

import static byow.Core.Main.engine;
 public class Lamp extends Item implements Serializable {

        private static final int WEIGHT_DFT = 100;
        private static final int VALUE_DFT = 0;
        private static final int DURATION_DFT = 100;
        private static final int LUMEN_DFT = 100;
        private int lumen;
        private boolean isOn;

        TERenderer ter;
        TETile[][] world;
        TETile[][] refWorld;
        GameState gameState;

        Room room; // a lamp should always in a room;

        public Lamp(GameState gameState) {
            super(WEIGHT_DFT, VALUE_DFT, DURATION_DFT);
            this.lumen = LUMEN_DFT;
            this.ter = engine.ter;
            this.world = gameState.world;
            this.refWorld = gameState.refWorld;
            this.gameState = gameState;
            this.isOn = true;
            this.room = null;
            //this.engine = engine;
        }

     public Lamp(Position pos, Room room, GameState gameState) {
         super(WEIGHT_DFT, VALUE_DFT, DURATION_DFT, pos);
         this.lumen = LUMEN_DFT;
         this.ter = engine.ter;
         this.world = gameState.world;
         this.refWorld = gameState.refWorld;
         this.gameState = gameState;
         this.isOn = true;
         this.room = room;
         //this.engine = engine;
     }

    public Lamp(int lumen, GameState gameState) {
        super(WEIGHT_DFT, VALUE_DFT, DURATION_DFT);
        this.lumen = lumen;
        this.ter = engine.ter;
        this.world = gameState.world;
        this.refWorld = gameState.refWorld;
        this.gameState = gameState;
        this.isOn = true;
        this.room = null;
        //this.engine = engine;
    }
    public Room getRoom() {
            return this.room;
    }
    public Lamp(int weight, int value, int dur, int lumen, GameState gameState) {
        super(weight, value, dur);
        this.lumen = lumen;
        this.ter = engine.ter;
        this.world = gameState.world;
        this.refWorld = gameState.refWorld;
        this.gameState = gameState;
        this.isOn = true;
        this.room = null;
        //this.engine = engine;
    }

    public int getLumen() {
        return this.lumen;
    }

    public void setLumen(int lum) {
         this.lumen = lum;
     }

    public boolean getSwitch() {
        return this.isOn;
     }

    public void switchOn() {
        this.isOn = true;
     }

    public void switchOff() {
         this.isOn = false;
     }

    }


