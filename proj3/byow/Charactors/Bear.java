package byow.Charactors;

import byow.Articles.ChestPlate;
import byow.Core.GameState;
import byow.Attribute.Position;
import byow.Attribute.Size;

import java.util.List;

import static byow.Attribute.Direction.getDirFromPosition;
import static byow.Core.Main.gameState;

public class Bear extends Creature{
    private static final int DAMAGE_DFT = 30;
    private static final int HEALTH_DFT = 100;
    private static final int AGE_DFT = 8;
    private static final int WEIGHT_DFT = 2;
    private static final Size SIZE = new Size(2, 2);
    private static final int AFFECTION_DFT = -100;

    // arms at hand
    public Bear(Position pos, GameState gameState) {
        super(DAMAGE_DFT, HEALTH_DFT, AGE_DFT, WEIGHT_DFT, SIZE, AFFECTION_DFT, pos, gameState);
    }
    public Bear(int damage, int health, int age, int weight, Size size, int affection, Position pos, GameState gameState) {
        super(damage, health, age, weight, size, affection, pos, gameState);
        this.inventory.add(new ChestPlate());
    }

    @Override
    public void hunt(Position pos) {
        // get the route to hunt the hero down in given world
        List<Position> route = getPathTo(this.getSrcDir(), pos);

        if (route != null) {
            this.setHuntRoute(route);
            if (route.size() > 1) {
                Position newPos = route.get(1);
                // a successive hunt, update the hunt direction
                this.setSrcDir(getDirFromPosition(this.getPosition(), newPos));
                MoveTo(newPos, gameState.world);

                // because bear has taken a leap, should remove the first step.
                route.removeFirst();
            }
        } else {
            this.setSrcDir(null);// if there is a miss hunt, set to null
            this.setHuntRoute(null);
        }
    }
}