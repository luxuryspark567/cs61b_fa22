package byow.Charactors;

import byow.Articles.ChestPlate;
import byow.Articles.Door;
import byow.Attribute.Direction;
import byow.Attribute.Directionset;
import byow.Core.GameState;
import byow.Attribute.Position;
import byow.Attribute.Size;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Utils.DiggerRouteSearch;
import byow.Utils.RouteSearch;
import byow.Utils.WorldGenerateUtils;

import java.util.LinkedList;
import java.util.List;

import static byow.Attribute.Direction.getDirFromPosition;
import static byow.Attribute.Direction.getShiftPosition;
import static byow.Core.Engine.LOOP_LIMIT;
import static byow.Core.Main.gameState;

public class Bear extends Creature{
    private static final int DAMAGE_DFT = 30;
    private static final int HEALTH_DFT = 100;
    private static final int AGE_DFT = 8;
    private static final int WEIGHT_DFT = 2;
    private static final Size SIZE = new Size(2, 2);
    private static final int AFFECTION_DFT = -100;

    private List<Position> huntRoute = null;

    private Direction startHundDir;
    // arms at hand
    public Bear(Position pos, GameState gameState) {
        super(DAMAGE_DFT, HEALTH_DFT, AGE_DFT, WEIGHT_DFT, SIZE, AFFECTION_DFT, pos, gameState);
        this.startHundDir = null;
    }
    public Bear(int damage, int health, int age, int weight, Size size, int affection, Position pos, GameState gameState) {
        super(damage, health, age, weight, size, affection, pos, gameState);
        this.inventory.add(new ChestPlate());
        this.startHundDir = null;
    }

    public List<Position> getHuntRoute() {
        return huntRoute;
    }
    //
    public void huntHero(Position posAvatar) {

        List<TETile> forbidTile = new LinkedList<>();
        forbidTile.add(Tileset.WALL);
        forbidTile.add(Tileset.LOCKED_DOOR);
        forbidTile.add(Tileset.LAMP);
        RouteSearch rs = new DiggerRouteSearch(this.startHundDir, forbidTile, 1);
        List<Position> route = rs.getRoute(this.getPosition(), posAvatar, gameState.world);

        if (route != null) {
            this.huntRoute = route;
            if (route.size() > 1) {
                Position newPos = route.get(1);
                // a successive hunt, update the hunt direction
                this.startHundDir = getDirFromPosition(this.getPosition(), newPos);
                MoveTo(newPos, gameState.world);

                // because bear has taken a leap, should remove the first step.
                route.removeFirst();
            }

        }
        else {
            this.startHundDir = null; // if there is a miss hunt, set to null
            this.huntRoute = null;
        }

    }

}