package byow.Utils;

import byow.Attribute.Position;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;

import static byow.Core.Main.gameState;
import static byow.Utils.TileUtils.getMouseCoord;

public class WorldInfoDisplay implements Serializable {

    TETile tileMouse; //get the current hovering tile
    TETile tileMouseBack;

    int mouseClickState;

    //int avatarHealthBak;
    public WorldInfoDisplay(){
        this.tileMouse = null;
        this.tileMouseBack = null;
        this.mouseClickState = 0;
        //this.avatarHealthBak = -1;
    }

    public TETile getTileMouse() {
        return this.tileMouse;
    }


    public TETile getTileMouseBack() {
        return this.tileMouseBack;
    }
    public Position getMouseHoveredTilePosition() {
        TileUtils.CanvasCoordinate cc = getMouseCoord();
        return TileUtils.getTilePosFromCanvasCoord(cc);
    }
    public void updateMouseHoverTile() {
        Position posMouse = getMouseHoveredTilePosition();
        gameState.setHoverMousePosition(posMouse);
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

    public boolean isMouseReleased() {
        boolean retBool = false;
        switch (mouseClickState) {
            case 0: // mouse not pressed state

                if (StdDraw.isMousePressed()) {
                    // mouse clicked
                    mouseClickState = 1;
                }
                break;

            case 1:

                if (!StdDraw.isMousePressed()) {
                    mouseClickState = 0;
                    // mouse released
                    retBool = true;
                }
                break;
            default:
                mouseClickState = 0;
                break;
        }

        return retBool;
    }

}
