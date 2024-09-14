package byow.Utils;

import byow.Attribute.Position;
import byow.TileEngine.TETile;

import java.util.List;

public interface RouteSearch {
    // return a route from posSrc to posDst in mWorld
    public List<Position> getRoute(Position posSrc, Position posDst, TETile[][] mWorld);
}
