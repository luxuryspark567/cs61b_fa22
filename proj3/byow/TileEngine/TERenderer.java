package byow.TileEngine;

import byow.Attribute.Position;
import byow.Charactors.Avatar;
import byow.Charactors.Creature;
import byow.Core.Engine;
import byow.Core.GameState;
import byow.Utils.TileUtils;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.List;

import static byow.Core.Engine.*;
import static byow.Utils.TileUtils.*;
import static byow.Utils.TileUtils.paintTile;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer implements Serializable {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
        StdDraw.show();
    }

    public void renderInitialize() {
        initialize(getCanvasWidth(), getCanvasHeight(), X_OFF, Y_OFF);
        //initialize(WIDTH, HEIGHT, X_OFF, Y_OFF * 2);
    }
    public void renderText(String str) {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text((double)getCanvasWidth() / 2, (double)getCanvasHeight() / 2, str);
        StdDraw.show();
    }
/*
    public void renderTextWithoutClear(String str, int size, TileUtils.CanvasCoordinate  cc) {
        //StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, size);
        StdDraw.setFont(fontBig);
        StdDraw.text(cc.getX(), cc.getY(), str);
        StdDraw.show();
    }

    public void renderTextWithoutClearRightAligned(String str, int size, TileUtils.CanvasCoordinate cc) {
        //StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, size);
        StdDraw.setFont(fontBig);
        StdDraw.textRight(cc.getX(), cc.getY(), str);
        StdDraw.show();
    }
*/
    public void renderTextWithoutClearLeftAligned(String str, int size, TileUtils.CanvasCoordinate cc) {
        //StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, size);
        StdDraw.setFont(fontBig);
        StdDraw.textLeft(cc.getX(), cc.getY(), str);
        StdDraw.show();
    }

    public void renderTextRightAligned(String str, int size, Position pos) {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, size);
        StdDraw.setFont(fontBig);
        StdDraw.textRight(pos.getX(), pos.getY(), str);
        StdDraw.show();
    }

    public void renderClear() {
        StdDraw.clear(new Color(0, 0, 0));
    }

    public void renderAddTextToCanvas(String str, int size, Position pos) {
        //StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(pos.getX(), pos.getY(), str);
        StdDraw.show();
    }

    public void renderAddTextToCanvas(String str, int size, double x, double y) {
        //StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, size);
        StdDraw.setFont(fontBig);
        StdDraw.text(x, y, str);
        StdDraw.show();
    }
    public void renderShow() {
        StdDraw.show();;
    }
    public void paintCreature(Creature c, TETile type, TETile[][] world) {
        if (c == null) {
            return;
        }
        //this.world = TETile.copyOf(this.refWorld);
        //if (c.getBackedPosition() != null) {
        //    paintTile(c.getBackedPosition(), c.getBackedTile(), world);
        //}
        paintTile(c.getPosition(), type, world);
    }
/*
    public void renderCreature(Creature c, TETile tile, TETile[][] world) {
        // print avatar
        if (c != null) {
            paintCreature(c, tile, world);
        }
    }
*/

    public void renderAvatarHearts(Avatar hero, TETile[][] world) {
        //debug, fill empty

        // reset fond
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        int x = 1;
        int y = getCanvasHeight() - 5;
        for (int i = 0; i < hero.getHealth(); i++) {
            Tileset.HEART.draw(x + i, y);
        }

        StdDraw.show();
    }


    public void renderMenuPage() {
        renderClear();
        renderAddTextToCanvas("CS61B: THE GAME", 30, (double)getCanvasWidth() / 2, (double)getCanvasHeight() * 3 / 4);
        renderAddTextToCanvas("New Game (N)", 16, (double)getCanvasWidth() / 2, (double)getCanvasHeight() / 2);
        renderAddTextToCanvas("Load Game (L)", 16, (double)getCanvasWidth() / 2, (double)getCanvasHeight() / 2 - 1);
        renderAddTextToCanvas("Quit Game (Q)", 16, (double)getCanvasWidth() / 2, (double)getCanvasHeight() / 2 - 2);
        renderShow();
    }

    public void renderRandomInputPage(String Num) {
        renderClear();
        renderAddTextToCanvas("Type Seed:", 30, (double)getCanvasWidth() / 2, (double)getCanvasHeight() - 1);
        renderAddTextToCanvas(Num, 30, (double)getCanvasWidth() / 2, (double)getCanvasHeight() / 2);
        renderShow();
    }

    public void renderKeyMenu() {
        String str1 = "Lock the door";
        String str2 = "Unlock the door";
        //this.renderTextWithoutClear("Actions:", 16, new Position(width/2, height/2));
        this.renderTextWithoutClearLeftAligned("1: " + str1, 16,
                new TileUtils.CanvasCoordinate((double)(2), 1));
        this.renderTextWithoutClearLeftAligned("2: " + str2, 16,
                new TileUtils.CanvasCoordinate((double)(str1.length() + 2), 1));
    }

    public void renderLampMenu() {

        String str1 = "Switch Off";
        String str2 = "Switch On";
        //this.renderTextWithoutClear("Actions:", 16, new Position(width/2, height/2));
        this.renderTextWithoutClearLeftAligned("1: " + str1, 16,
                new TileUtils.CanvasCoordinate((double)(2), 1));
        this.renderTextWithoutClearLeftAligned("2: " + str2, 16,
                new TileUtils.CanvasCoordinate((double)(str1.length() + 2), 1));
    }
    // pause count ms
    public void renderPause(int count) {
        StdDraw.pause(count);
    }

    public void renderLoadFailPage() {
        renderClear();
        renderAddTextToCanvas("Load Fail", 30, (double)getCanvasWidth() / 2, (double)getCanvasHeight() / 2);
        renderShow();

        renderPause(500);
    }

    public void renderSaveFailPage() {
        renderClear();
        renderAddTextToCanvas("Save Fail", 30, (double)getCanvasWidth() / 2, (double)getCanvasHeight() / 2);
        renderShow();

        renderPause(500);
    }

    public void renderTileInfo(String str) {
        if (str != null) {
            renderTextWithoutClearLeftAligned(str, 30, new CanvasCoordinate(0, getCanvasHeight() - 2));
        }
    }

    public void resetFond(){
        // reset fond
        // TODO: the size 16 should be universal
        Font font = new Font("Monaco", Font.BOLD, 16 - 2);
        StdDraw.setFont(font);
    }
    public void updateWorldAndRender(Engine engine, GameState gameState) {

        if (gameState.refreshWorld > 0) {
            gameState.refreshWorld--;

            TETile[][] toRenderWorld = TETile.copyOf(gameState.world);

            // 1. paint environment
            paintAllLamps(toRenderWorld, gameState);
            paintAllDoors(toRenderWorld, gameState);

            // 2. paint creatures
            engine.ter.paintCreature(gameState.hero, Tileset.AVATAR, toRenderWorld);
            engine.ter.paintCreature(gameState.bear, Tileset.GANON, toRenderWorld);

            if (gameState.bear != null)
            {
                List<Position> tmp = gameState.bear.getHuntRoute();
                if (tmp != null) {
                    for (Position pos: tmp) {
                        if (isTileType(pos, Tileset.FLOOR, toRenderWorld))
                            paintTile(pos, new TETile(Tileset.FLOOR, Color.RED), toRenderWorld);
                    }
                }
            }

            // 3. paint mist
            if (gameState.hero != null) {
                paintMist(gameState.hero, toRenderWorld);
            }

            // 4. render the world
            resetFond();
            engine.ter.renderFrame(toRenderWorld);

            // 5. render HUD
            // 5.1 update avatar health
            engine.ter.renderAvatarHearts(gameState.hero, toRenderWorld);
            // 5.2 update mouse hover info
            if (gameState.getWid().getTileMouse() != null) {
                engine.ter.renderTileInfo(gameState.getWid().getTileMouse().description());
            }
        }
    }
}
