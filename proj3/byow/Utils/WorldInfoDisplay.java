package byow.Utils;

import byow.Attribute.Position;
import byow.TileEngine.TETile;

import java.io.Serializable;

import static byow.Core.Main.gameState;
import static byow.Utils.TileUtils.getMouseCoord;

public class WorldInfoDisplay implements Serializable {

    TETile tileMouse; //get the current hovering tile
    TETile tileMouseBack;

    //int avatarHealthBak;
    public WorldInfoDisplay(){
        this.tileMouse = null;
        this.tileMouseBack = null;
        //this.avatarHealthBak = -1;
    }

    public TETile getTileMouse() {
        return this.tileMouse;
    }

    public TETile getTileMouseBack() {
        return this.tileMouseBack;
    }
    public void updateMouseHoverTile() {
        TileUtils.CanvasCoordinate cc = getMouseCoord();
        Position posMouse = TileUtils.getTilePosFromCanvasCoord(cc);

        if (posMouse == null) {
            this.tileMouse = null;
        }
        else {
            this.tileMouse = gameState.world[posMouse.getX()][posMouse.getY()];
            //if (tileMouse != null) {
            //    System.out.println(tileMouse.description());
            //}
        }
    }

    public boolean isTileInfoChanged() {
        if (tileMouse == null) {
            return tileMouseBack != null;
        }
        else {
            if (tileMouseBack == null) {
                return true;
            }
            return !tileMouse.description().equals(tileMouseBack.description());
        }
    }
    public void backUpMouseHoverTileInfo() {
        this.tileMouseBack = this.tileMouse;
    }
/*
    public void getMouseHoverTileInfo() {
        TileUtils.CanvasCoordinate cc = getMouseCoord();
        Position posMouse = TileUtils.getTilePosFromCanvasCoord(cc);

        if (posMouse == null) {
            this.tileMouse = null;
        }
        else {
            this.tileMouse = gameState.world[posMouse.getX()][posMouse.getY()];
            if (tileMouse != null)
                System.out.println(tileMouse.description());
        }
    }

 */
/*
    public void displayMouseTileInfo(TERenderer tr) {
        this.getTileInfo();
        this.backTileInfo();
        if (isTileInfoChanged()) {
            tr.renderTileInfo(this.tileDescr);
        }
    }

 */

/*
    private boolean isAvatarHealthChanged() {
        return this.tileDescrBack.equals(this.tileDescr);
    }
    private void backTileInfo() {
        this.tileDescrBack = this.tileDescr;
    }
    public void displayMouseAvatarHealth(TERenderer tr) {

    }
 */
}
