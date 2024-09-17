package byow.TileEngine;

import byow.Attribute.Coordinate;
import byow.Attribute.Direction;
import byow.Attribute.Directionset;
import byow.Attribute.Position;
import byow.Charactors.Avatar;
import byow.Charactors.Creature;
import byow.Core.Engine;
import byow.Core.GameState;
import byow.Utils.TileUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static byow.Attribute.Coordinate.getCanvasCoordFromTilePos;
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

    /**
     * to paint the trace of the bear hunting the hero.
     * */
    public void renderChaseTrace(TETile[][] toRenderWorld, GameState gameState) {

        if (!gameState.enableChaseTrace) {
            return;
        }

        //Font font;
        if (gameState.bear != null)
        {
            List<Position> tmp = gameState.bear.getHuntRoute();
            if (tmp != null) {
                for (Position pos: tmp) {
                    if (isTileType(pos, Tileset.FLOOR, toRenderWorld)) {
                        Font font = new Font("Monaco", Font.BOLD, 30);
                        StdDraw.setFont(font);
                        paintTile(pos, new TETile(Tileset.FLOOR, Color.RED), toRenderWorld);
                    }
                }
            }
        }
    }

    /**
     * to paint the trace of the bear hunting the hero.
     * */
    public void renderMouseTrace(TETile[][] toRenderWorld, GameState gameState) {

        if (!gameState.enableMouseTrace) {
            return;
        }

        if (gameState.hero != null)
        {
            List<Position> tmp = gameState.hero.getHuntRoute();
            if (tmp != null) {
                for (Position pos: tmp) {
                    if (isTileType(pos, Tileset.FLOOR, toRenderWorld))
                    {
                        Font font = new Font("Monaco", Font.BOLD, 30);
                        StdDraw.setFont(font);
                        paintTile(pos, new TETile(Tileset.FLOOR, new Color(128, 192, 128), Color.GRAY), toRenderWorld);
                    }
                }
            }
        }
    }

    public void drawLine(Coordinate coord0, Coordinate coord1) {
        StdDraw.line(coord0.getX(), coord0.getY(), coord1.getX(), coord1.getY());
    }

    public Coordinate getCenter(Coordinate coord) {
        return new Coordinate((double)coord.getX() + 0.5, (double)coord.getY() + 0.5);
    }

    public Coordinate getTail(Coordinate coord, Direction dir) {

        if (coord == null || dir == null) {
            return null;
        }

        if (Directionset.NORTH.equals(dir)) {
            return new Coordinate(coord.getX(), coord.getY() - 0.5);
        }
        else if (Directionset.WEST.equals(dir)) {
            return new Coordinate(coord.getX() + 0.5, coord.getY());
        }
        else if (Directionset.SOUTH.equals(dir)) {
            return new Coordinate(coord.getX(), coord.getY() + 0.5);
        }
        else if (Directionset.EAST.equals(dir)) {
            return new Coordinate(coord.getX() - 0.5, coord.getY());
        }

        return null;
    }

    public Coordinate getHead(Coordinate coord, Direction dir) {

        if (coord == null || dir == null) {
            return null;
        }

        if (Directionset.NORTH.equals(dir)) {
            return new Coordinate(coord.getX(), coord.getY() + 0.5);
        }
        else if (Directionset.WEST.equals(dir)) {
            return new Coordinate(coord.getX() - 0.5, coord.getY());
        }
        else if (Directionset.SOUTH.equals(dir)) {
            return new Coordinate(coord.getX(), coord.getY() - 0.5);
        }
        else if (Directionset.EAST.equals(dir)) {
            return new Coordinate(coord.getX() + 0.5, coord.getY());
        }

        return null;
    }

    public Coordinate getLeftSide(Coordinate coord, Direction dir) {

        if (coord == null || dir == null) {
            return null;
        }

        if (Directionset.NORTH.equals(dir)) {
            return new Coordinate(coord.getX() - 0.25, coord.getY() + 0.25);
        }
        else if (Directionset.WEST.equals(dir)) {
            return new Coordinate(coord.getX() - 0.25, coord.getY() - 0.25);
        }
        else if (Directionset.SOUTH.equals(dir)) {
            return new Coordinate(coord.getX() + 0.25, coord.getY() - 0.25);
        }
        else if (Directionset.EAST.equals(dir)) {
            return new Coordinate(coord.getX() + 0.25, coord.getY() + 0.25);
        }

        return null;
    }

    public Coordinate getRightSide(Coordinate coord, Direction dir) {

        if (coord == null || dir == null) {
            return null;
        }

        if (Directionset.NORTH.equals(dir)) {
            return new Coordinate(coord.getX() + 0.25, coord.getY() + 0.25);
        }
        else if (Directionset.WEST.equals(dir)) {
            return new Coordinate(coord.getX() - 0.25, coord.getY() + 0.25);
        }
        else if (Directionset.SOUTH.equals(dir)) {
            return new Coordinate(coord.getX() - 0.25, coord.getY() - 0.25);
        }
        else if (Directionset.EAST.equals(dir)) {
            return new Coordinate(coord.getX() + 0.25, coord.getY() - 0.25);
        }

        return null;
    }

    public class ViewEnd {
        Position pos; // position in a tile
        Direction dir; // which side is sawed by the viewer
        Coordinate coord; // the coordinate of the viewer's eyesight terminates
        double distance;

        public ViewEnd (Position pos, Direction dir, Coordinate coord) {
            this.pos = pos;
            this.dir = dir;
            this.coord = coord;
            this.distance = 0;
        }
        public void setDistance (double dis) {
            this.distance = dis;
        }

    }

    double[] getThetaSeral(double initialViewAngle) {

        int numOnEachSide = (int)(VIEW_ANGLE_SCOPE / 2 / VIEW_ANGLE_RESOLUTION);

        double[] retDouble = new double[numOnEachSide * 2 + 1]; // initial view + each_side * 2

        retDouble[numOnEachSide] = initialViewAngle;

        for (int index = 1; index <= numOnEachSide; index++) {
            //left
            retDouble[numOnEachSide - index] = Avatar.toViewAngle(initialViewAngle - VIEW_ANGLE_RESOLUTION * index);
            //right
            retDouble[numOnEachSide + index] = Avatar.toViewAngle(initialViewAngle + VIEW_ANGLE_RESOLUTION * index);
        }

        return retDouble;
    }

    double[] getKSeral(Coordinate c1, List<Coordinate> c2List) {
        double[] retDouble = new double[c2List.size()];
        int index = 0;
        for (Coordinate c2: c2List) {
            retDouble[index] = (c1.getY() - c2.getY()) / (c1.getX() - c2.getX());
            index++;
        }
        return retDouble;
    }

    double[] getBSeral(Coordinate c1, List<Coordinate> c2List) {
        double[] retDouble = new double[c2List.size()];
        int index = 0;
        for (Coordinate c2: c2List) {
            retDouble[index] = (c1.getX()*c2.getY() - c2.getX()*c1.getY()) / (c1.getX() - c2.getX());
            index++;
        }
        return retDouble;
    }

    double[] getBSeral(Coordinate c, double[] thetaSeral) {
        double[] retDouble = new double[thetaSeral.length];
        int index = 0;
        for (double theta: thetaSeral) {
            if (theta == -Math.PI/2 || theta == Math.PI/2) {
                retDouble[index] = c.getX();
            }
            else {
                retDouble[index] = c.getY() - c.getX() * Math.tan(theta);
            }
            index++;
        }
        return retDouble;
    }

    double getXByKB(double k, double b, double y) {
        return (y - b) / k;
    }
    double getYByKB(double k, double b, double x) {
        return k * x + b;
    }

    double getXByThetaB(double theta, double b, double y, Coordinate coord) {
        if (theta == -Math.PI/2 || theta == Math.PI/2) {
            return coord.getX() + 0.5;
        }
        return (y - b) / Math.tan(theta);
    }
    double getYByThetaB(double theta, double b, double x) {
        if (theta == -Math.PI/2 || theta == Math.PI/2) {
            return Engine.DOUBLE_MAX; // should never cross, because it is parallel to Y axis
        }
        return Math.tan(theta) * x + b;
    }
    public List<Coordinate> getWindowPixelCoords(Avatar hero, int resolution) {
        if (hero == null) {
            return null;
        }
        Direction viewDir = hero.getViewDir();

        if (viewDir == null) {
            return null;
        }

        Coordinate coord = getCanvasCoordFromTilePos(hero.getPosition());

        if (coord == null) {
            return null;
        }
        List<Coordinate> retList = new ArrayList<>();

        for (int i = 0; i < resolution; i++) {
            if (Directionset.NORTH.equals(viewDir)) {
                retList.add(new Coordinate(coord.getX() + ((float) i +  0.5) / resolution, coord.getY() + 1));
            }
            else if (Directionset.WEST.equals(viewDir)) {
                retList.add(new Coordinate(coord.getX(), coord.getY() + ((float) i +  0.5) / resolution));
            }
            else if (Directionset.SOUTH.equals(viewDir)) {
                retList.add(new Coordinate(coord.getX() + 1 - ((float) i +  0.5) / resolution, coord.getY()));
            }
            else if (Directionset.EAST.equals(viewDir)) {
                retList.add(new Coordinate(coord.getX() + 1, coord.getY() + 1 - ((float) i +  0.5) / resolution));
            }
        }

        return retList;
    }

    public Direction getDirByAngle(double viewAngle) {

        double angle = Avatar.toViewAngle(viewAngle);

        if (angle > Math.PI / 4 && angle <= Math.PI * 3 / 4) {
            return Directionset.NORTH;
        }
        else if (angle > Math.PI * 3 / 4 || angle <= -Math.PI * 3 / 4) {
            return Directionset.WEST;
        }
        else if (angle > -Math.PI * 3 / 4 && angle <= -Math.PI / 4) {
            return Directionset.SOUTH;
        }
        else if (angle > -Math.PI / 4 && angle <= Math.PI / 4) {
            return Directionset.EAST;
        }
        return null;
    }

    public boolean[] getDirBoolByAngle(double viewAngle) {

        double angle = Avatar.toViewAngle(viewAngle);

        boolean[] dirBool = new boolean[]{false, false, false, false}; // north, west, south, east

        if (angle > 0 && angle < Math.PI/2) { //north & east
            dirBool[0] = true;
            dirBool[3] = true;
        }
        else if(angle == Math.PI/2) { // north
            dirBool[0] = true;
        }
        else if(angle > Math.PI/2 && angle < Math.PI) { // north & west
            dirBool[0] = true;
            dirBool[1] = true;
        }
        else if (angle == Math.PI) { // west
            dirBool[1] = true;
        }
        else if (angle > -Math.PI && angle < -Math.PI/2) { // west * south
            dirBool[1] = true;
            dirBool[2] = true;
        }
        else if(angle == -Math.PI/2) { // south
            dirBool[2] = true;
        }
        else if(angle > -Math.PI/2 && angle < 0) { // south && east
            dirBool[2] = true;
            dirBool[3] = true;
        }
        else if (angle == 0) { // east
            dirBool[3] = true;
        }
/*
        if (angle > Math.PI / 4 && angle < Math.PI * 3 / 4) { //north
            dirBool[0] = true;
            dirBool[1] = true;

            dirBool[3] = true;
            //return Directionset.NORTH;
        }
        else if (angle == Math.PI * 3 / 4) { // north & west
            dirBool[0] = true;
            dirBool[1] = true;
        }
        else if (angle > Math.PI * 3 / 4 || angle < -Math.PI * 3 / 4) { // west
            dirBool[0] = true;
            dirBool[1] = true;
            dirBool[2] = true;

            //return Directionset.WEST;
        }
        else if(angle == -Math.PI * 3 / 4) { // west, south
            dirBool[1] = true;
            dirBool[2] = true;
        }
        else if (angle > -Math.PI * 3 / 4 && angle < -Math.PI / 4) { // south

            dirBool[1] = true;
            dirBool[2] = true;
            dirBool[3] = true;
            //return Directionset.SOUTH;
        }
        else if (angle == -Math.PI / 4) { //south & east
            dirBool[2] = true;
            dirBool[3] = true;
        }
        else if (angle > -Math.PI / 4 && angle < Math.PI / 4) { //east
            dirBool[0] = true;

            dirBool[2] = true;
            dirBool[3] = true;
            //return Directionset.EAST;
        }
        else {//if (angle == Math.PI / 4) { //north & east
            dirBool[0] = true;
            dirBool[3] = true;
        }


 */
        return dirBool;

    }

    // get view, and paint first person vision in 2D
    public void renderVisionView (GameState gameState, TETile[][] visionWorld, int resolution){

        Direction viewDir = gameState.hero.getViewDir();
        Coordinate coord = getCanvasCoordFromTilePos(gameState.hero.getPosition());

        if (viewDir == null || coord == null) {
            return;
        }

        // render an arrow to represent the view direction of an avatar
        // TODO: First part: Draw an arrow
        // 2.1 get center
        Coordinate centerCoord = getCenter(coord);
        // 2.1 get tail
        Coordinate tailCoord = getTail(centerCoord, viewDir);
        // 2.2 get head
        Coordinate headCoord = getHead(centerCoord, viewDir);
        // 2.3 get side1 && side2
        Coordinate leftSideCoord = getLeftSide(centerCoord, viewDir);
        Coordinate rightSideCoord = getRightSide(centerCoord, viewDir);
/*
        // Draw arrow
        //StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.RED);
        drawLine(tailCoord, headCoord);
        drawLine(leftSideCoord, headCoord);
        drawLine(rightSideCoord, headCoord);
        StdDraw.show();
*/

        // TODO: Second part: draw vision lines in 2d;

        // get the window and cut it in resolution pieces of slices
        //int resolution = 80;

        //List<Coordinate> windowPixelCoords = getWindowPixelCoords(gameState.hero, resolution);
        double[] thetaSeral = getThetaSeral(gameState.hero.getViewAngle());
        double[] bSeral = getBSeral(centerCoord, thetaSeral);
        List<ViewEnd> windowPixelEnds = new ArrayList<>();


        for (int i = 0; i < thetaSeral.length; i++) {

            System.out.println("------------------------------------------");
            System.out.println(i);
            Position nextPos = gameState.hero.getPosition();
            Direction curDir = getDirByAngle(thetaSeral[i]);
            boolean[] dirBool = getDirBoolByAngle(thetaSeral[i]);
/*
            Direction curDir = Direction.getFirstTrueDirFromPosition(dirBool);
            if (Directionset.NORTH.equals(curDir)) {
                nextPos = Direction.getShiftPosition(gameState.hero.getPosition(), Directionset.NORTH);
            }
            else if (Directionset.WEST.equals(curDir)) {
                nextPos = Direction.getShiftPosition(gameState.hero.getPosition(), Directionset.WEST);
            }
            else if (Directionset.SOUTH.equals(curDir)) {
                nextPos = Direction.getShiftPosition(gameState.hero.getPosition(), Directionset.SOUTH);
            }
            else if (Directionset.EAST.equals(curDir)) {
                nextPos = Direction.getShiftPosition(gameState.hero.getPosition(), Directionset.EAST);
            }
            else {
                continue;
            }
*/
            Coordinate coordTmp = null;

            while (isInTileWorld(nextPos)
                && (isTileType(nextPos, Tileset.FLOOR, gameState.world)
                    || isTileType(nextPos, Tileset.UNLOCKED_DOOR, gameState.world))) { // nextPos is not out of canvas or nextPos is a FLOOR

                System.out.println(nextPos);
                System.out.println(Direction.getFirstTrueDirFromPosition(dirBool));
                System.out.println(gameState.world[nextPos.getX()][nextPos.getY()].description());

                Coordinate coordToCalc = getCanvasCoordFromTilePos(nextPos);
                // 1, find the other side which intersects view line;
                double x1, x2;
                double y1, y2;

                x1 = getXByThetaB(thetaSeral[i], bSeral[i], coordToCalc.getY() + 1, coordToCalc); // north
                y1 = getYByThetaB(thetaSeral[i], bSeral[i], coordToCalc.getX()); // west
                x2 = getXByThetaB(thetaSeral[i], bSeral[i], coordToCalc.getY(), coordToCalc); // south
                y2 = getYByThetaB(thetaSeral[i], bSeral[i], coordToCalc.getX() + 1); //east

                if (x1 >= coordToCalc.getX() && x1 <= coordToCalc.getX() + 1
                    && dirBool[Direction.getIndexFromDir(Directionset.NORTH)]) { // pre south out put, means north input, then the first line is surely met
                    curDir = Directionset.NORTH;
                    coordTmp = new Coordinate(x1, coordToCalc.getY() + 1);
                }

                else if (y1 >= coordToCalc.getY() && y1 <= coordToCalc.getY() + 1
                        && dirBool[Direction.getIndexFromDir(Directionset.WEST)]) {
                    curDir = Directionset.WEST;
                    coordTmp = new Coordinate(coordToCalc.getX(), y1);
                }

                else if (x2 >= coordToCalc.getX() && x2 <= coordToCalc.getX() + 1
                        && dirBool[Direction.getIndexFromDir(Directionset.SOUTH)]) {
                    curDir = Directionset.SOUTH;
                    coordTmp = new Coordinate(x2, coordToCalc.getY());
                }

                else if (y2 >= coordToCalc.getY() && y2 <= coordToCalc.getY() + 1
                        && dirBool[Direction.getIndexFromDir(Directionset.EAST)]) {
                    curDir = Directionset.EAST;
                    coordTmp = new Coordinate(coordToCalc.getX() + 1, y2);
                }

                // 2, get the next position
                if (Directionset.NORTH.equals(curDir)) {
                    nextPos = Direction.getShiftPosition(nextPos, Directionset.NORTH);
                }
                else if (Directionset.WEST.equals(curDir)) {
                    nextPos = Direction.getShiftPosition(nextPos, Directionset.WEST);
                }
                else if (Directionset.SOUTH.equals(curDir)) {
                    nextPos = Direction.getShiftPosition(nextPos, Directionset.SOUTH);
                }
                else if (Directionset.EAST.equals(curDir)) {
                    nextPos = Direction.getShiftPosition(nextPos, Directionset.EAST);
                }
            }
            windowPixelEnds.add(new ViewEnd(nextPos, curDir, coordTmp));
        }

        for (ViewEnd ve: windowPixelEnds) {
            if (ve.coord != null) {
                StdDraw.setPenColor(Color.RED);
                drawLine(centerCoord, ve.coord);

                ve.setDistance(Coordinate.getDistance(centerCoord, ve.coord));

            }
        }
        StdDraw.show();

        // TODO: ThirdPart: get a full vision view of the world
        // resolution
/*
        Coordinate baseCoord = new Coordinate(0.5, 0.5);// text
        // 1, looper over each line
        for (int looper = 0; looper < windowPixelEnds.size(); looper++) {

            ViewEnd ve = windowPixelEnds.get(looper);
            List<ViewEnd> windowPixelEndsHorizontal = new ArrayList<>();

            if (ve != null && ve.coord != null) {

                System.out.println("new row: ");
                System.out.println(ve.distance);
                // 1.1 expand the line end abased on the line distance
                // get window pixels


                //List<Coordinate> windowPixelCoordsHorizontal = getWindowPixelCoordsHorizontal(new Coordinate(0, 0), resolution);

                // get target
                //List<Coordinate> windowPixelCoordsTarget = new ArrayList<>();
                //for (int i = 0; i < resolution; i++) {
                //    double distance = Math.sqrt(Math.pow(ve.coord.getX() - centerCoord.getX(), 2) + Math.pow(ve.coord.getY() - centerCoord.getY(), 2))
                //    Coordinate coordTmp = new Coordinate(windowPixelCoordsBase.get(i).getX(), centerCoord.getY() + distance);
                //    windowPixelCoordsTarget.add(coordTmp);
                //}
                // get K B
                Coordinate centerCoordHorizontal = new Coordinate(0.5, 0.5);

                double[] thetaSeral1 = getThetaSeral(Math.PI / 2);
                double[] bSeral1 = getBSeral(centerCoord, thetaSeral);
                //List<ViewEnd> windowPixelEnds = new ArrayList<>();

                //kSeral = getKSeral(centerCoordHorizontal, windowPixelCoordsHorizontal);
                //bSeral = getBSeral(centerCoordHorizontal, windowPixelCoordsHorizontal);

                //windowPixelEndsHorizontal = new ArrayList<>();


                double distance = Math.sqrt(Math.pow(ve.coord.getX() - centerCoord.getX(), 2) + Math.pow(ve.coord.getY() - centerCoord.getY(), 2));

                // get line view;
                for (int i = 0; i < thetaSeral1.length; i++) {
                    Coordinate coordTmp = null;

                    //if (i == resolution / 2) {
                    //    System.out.println("get to Middle: ");
                    //}
                    // 1, find the other side which intersects view line;
                    double x; // target
                    double y1, y2; // ceil and floor
                    x = getXByThetaB(thetaSeral[i], bSeral[i], centerCoordHorizontal.getY() + distance, centerCoordHorizontal); // north, calc target
                    y1 = getYByThetaB(thetaSeral[i], bSeral[i], 0); // west, calc ceil
                    y2 = getYByThetaB(thetaSeral[i], bSeral[i], 1); //east, calc floor

                    if (x >= 0 && x <= 1) { // line is surely met at the target
                        coordTmp = new Coordinate(x, 0.5 + distance);
                    }
                    else if (y1 >= 1 && y1 <= centerCoord.getY() + distance) {
                        coordTmp = new Coordinate(0, y1);
                    }
                    else if (y2 >= 1 && y2 <= centerCoord.getY() + distance) {
                        coordTmp = new Coordinate(1, y2);
                    }
                    else {
                        System.out.println("can not find the coord");
                    }

                    windowPixelEndsHorizontal.add(new ViewEnd(null, null, coordTmp));
                }

                // test
                Coordinate shiftInc = new Coordinate(1, 0);
                int looperTest = 0;
                for (ViewEnd ve1: windowPixelEndsHorizontal) {
                    if (ve1.coord != null) {

                        double targetX = ve1.coord.getX() + baseCoord.getX() - 0.5;
                        double targetY = ve1.coord.getY();

                        //StdDraw.setPenColor(Color.RED);
                        //drawLine(baseCoord, new Coordinate(targetX, targetY));
                        ve1.setDistance(Coordinate.getDistance(new Coordinate(0.5, 0.5), ve1.coord));
                        //System.out.println(baseCoord);
                        //System.out.println(new Coordinate(targetX, targetY));

                        looperTest++;
                        if (looperTest == resolution / 2) {
                            System.out.println("Middle: ");
                            System.out.println(ve1.distance);
                        }

                    }

                }
                //baseCoord.setX(baseCoord.getCoordinateFromShiftedBase(shiftInc).getX());
                //baseCoord.setY(baseCoord.getCoordinateFromShiftedBase(shiftInc).getY());
                //StdDraw.show();
            }


            // TODO: fourth part, render the 2D vision
            if (windowPixelEndsHorizontal.size() > 0) {
                for (int i = 0; i < windowPixelEndsHorizontal.size(); i++) {
                    int decrease = getDecreaseByDistance(windowPixelEndsHorizontal.get(i).distance);
                    visionWorld[looper][i] = new TETile(Tileset.NOTHING, TileUtils.getNewColor(Color.WHITE, decrease),
                            TileUtils.getNewColor(Color.WHITE, decrease));
                }
            }


        }
 */
    }

    public int getDecreaseByDistance(double distance) {
        return (int)distance * 10;
    }
    public List<Coordinate> getWindowPixelCoordsHorizontal(Coordinate baseCoord, int resolution) {
        List<Coordinate> retList = new ArrayList<>();
        for (int i = 0; i < resolution; i++) {
                retList.add(new Coordinate(baseCoord.getX() + ((float) i +  0.5) / resolution, baseCoord.getY() + 1));
        }
        return retList;
    }


    public void updateWorldAndRender(Engine engine, GameState gameState) {

        if (gameState.refreshWorld > 0) {
            gameState.refreshWorld--;

            TETile[][] toRenderWorld = TETile.copyOf(gameState.world);

            // 1. paint environment
            paintAllLamps(toRenderWorld, gameState);
            paintAllDoors(toRenderWorld, gameState);

            // 2. paint creatures
            paintCreature(gameState.hero, Tileset.AVATAR, toRenderWorld);
            paintCreature(gameState.bear, Tileset.GANON, toRenderWorld);

            // paint the chase trace
            renderChaseTrace(toRenderWorld, gameState);
            renderMouseTrace(toRenderWorld, gameState);

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

            //test

            int resolution = 128;
            TETile[][] testWorld = new TETile[resolution][resolution];
            renderVisionView(gameState, testWorld, resolution);
            /*
            engine.ter.renderPause(1000);

            //test
            //initialize(resolution, resolution, 0, 0);
            for (int i = 0; i < resolution; i++) {
                for (int j = 0; j < resolution; j++) {
                    if (testWorld[i][j] == null) {
                        testWorld[i][j] = Tileset.NOTHING;
                        //testWorld[i][j] = new TETile(Tileset.NOTHING, Color.WHITE, Color.WHITE);
                    }
                }
            }
            engine.ter.renderFrame(testWorld);
 */
        }
    }
}
