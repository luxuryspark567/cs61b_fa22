package PlusWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.awt.*;
import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 40;
    private static final int r = 3; // defined as the longest distance, as be put in a square (3*r)
    TETile[][] mTiles = new TETile[WIDTH][HEIGHT];
    private static final long SEED = 9873;
    private static final Random RANDOM = new Random(SEED);

    private static final long SEED_PATTERN = 2873123;
    private static final Random RANDOM_PATTERN = new Random(SEED_PATTERN);

    private final int r2;
    private final int r3;
    private final int r5;

    private final boolean[][] patternMask;

    private TERenderer ter;

    public PlusWorld() {
        this.r2 = r*2;
        this.r3 = r*3;
        this.r5 = r*5;
        // initialize
        this.ter = new TERenderer();
        this.ter.initialize(WIDTH, HEIGHT);
        // must be called, generate pattern.
        this.patternMask = generatePattern();
        paintCanvas();
    }

    // coordinate of a certain pixel, x is horizontal, and y is vertical
    private class coord {
        int x;
        int y;

        public coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // top-left pixel is (0, 0);
        // x is horizontal, and y is vertical;
        public coord(int serial) {
            this.x = serial % r3;
            this.y = Math.floorMod(serial, r3);
        }
    }

    // set the top-left corner pixel of the canvas as (0, 0)

    //set a random pixel in a sudoku to be (0 ,0),
    private coord getRandomStartPosition() {
        // random number 0 ~ r^2 - 1
        int serial = RANDOM.nextInt(r3 * r3);

        // calc the relative coordinate of this serial number;
        return new coord(serial);
    }

    //what is the coordinate of the top-left corner pixel?
    private coord getOriginCoordinateInSudoku(coord c) {
        // get and return the coordinate of top-left pixel in this sudoku
        return new coord(-c.x, -c.y);
    }

    // To make the canvas fully painted, what is coordinate first top row sudoku
    private coord getInitialSudokuCoordinate(coord c) {
        // there are three conditions

        // if the (0, 0) is in the first portion of a sudoku in vertical direction
        // (c.y - 1) level
        if (c.y > -r) {
            return new coord(c.x + r, c.y - r2);
        }
        // if the (0, 0) is in the second portion of a sudoku in vertical direction
        // (c.y - 2) level
        else if(c.y > -r2) {
            return new coord(c.x + r3, c.y - r);
        }
        // if the (0, 0) is in the third portion of a sudoku in vertical direction
        else {
            return c;
        }
    }

    // get the next sudoku position in horizontal
    // for the horizontal direction, the next is always 5*r away;
    private coord getNextPositionHorizontal(coord c) {
        return new coord(c.x + r5, c.y);
    }

    // get the next sudoku position in vertical
    // for the vertical direction, the next is always besides the left in horizontal, and plus one in vertical;
    // and should roll back if it is out of the canvas.
    private coord getNextPositionVertical(coord c) {
        coord nextLevelSudoku = new coord(c.x - r3, c.y + r);

        // if c.x goes too far, move and return the sudoku on the right
        // I DO NOT SURE IF IT WILL WORK!!!!!!
        if (nextLevelSudoku.x < - r3) {
            nextLevelSudoku.x = nextLevelSudoku.x + r5;
        }
        return nextLevelSudoku;
    }

    // generate a mask to mask which pixels should be paint
    private boolean[][] generatePattern() {
        boolean[][] patternMask = new boolean[r3][r3];
        // typical pattern, a plus sign
        for (int i = 0; i < r3; i++) {// i is horizontal
            if (i < r || i >= r2) {
                for (int j = 0; j < r3; j++) {
                    if (j < r) {
                        patternMask[i][j] = false;
                    }
                    else if (j < r2) {
                        patternMask[i][j] = true;
                    }
                    else {
                        patternMask[i][j] = false;
                    }
                }
            }
            else {
                for (int j = 0; j < r3; j++) {
                    patternMask[i][j] = true;
                }
            }
        }
        return patternMask;
    }

    // generate a random tile pattern
    private static TETile randomTile() {
        int tileNum = RANDOM_PATTERN.nextInt(11);
        return switch (tileNum) {
            case 0 -> Tileset.AVATAR;
            case 1 -> Tileset.WALL;
            case 2 -> Tileset.FLOOR;
            case 3 -> Tileset.GRASS;
            case 4 -> Tileset.WATER;
            case 5 -> Tileset.FLOWER;
            case 6 -> Tileset.LOCKED_DOOR;
            case 7 -> Tileset.UNLOCKED_DOOR;
            case 8 -> Tileset.SAND;
            case 9 -> Tileset.MOUNTAIN;
            case 10 -> Tileset.TREE;
            default -> Tileset.NOTHING;
        };
    }
    // paint the pattern
    private void paintPattern(coord c) {
        TETile tilePattern = randomTile();
        for (int i = 0; i < r3; i++) {
            for (int j = 0; j < r3; j++) {
                if (patternMask[i][j]) {
                    int x1 = c.x + i;
                    int y1 = c.y + j;
                    // should skip if pixel to be paint is out of canvas
                    if (x1 >= 0 && x1 < WIDTH && y1 >= 0 && y1 < HEIGHT) {
                        mTiles[x1][y1] = tilePattern;
                    }
                }
            }
        }
    }

    private void paintCanvas() {
        coord startCoord = getRandomStartPosition();
        coord orgSudoku = getOriginCoordinateInSudoku(startCoord);
        coord curSudoku = getInitialSudokuCoordinate(orgSudoku);

        while (curSudoku.y < HEIGHT) {
            coord bakFirst = curSudoku;
            while(curSudoku.x < WIDTH) {
                paintPattern(curSudoku);
                curSudoku = getNextPositionHorizontal(curSudoku);
            }
            curSudoku = getNextPositionVertical(bakFirst);
        }
    }

    public static void main (String[] s) {
        PlusWorld p = new PlusWorld();
        p.ter.renderFrame(p.mTiles);
    }
}
