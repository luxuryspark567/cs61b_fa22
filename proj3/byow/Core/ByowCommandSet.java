package byow.Core;

public class ByowCommandSet {

    public static final ByowCommand IDLE = new ByowCommand("Idle opt");
    public static final ByowCommand CREATE_NEW_WORLD = new ByowCommand("create new world");
    public static final ByowCommand QUIT_AND_SAVE_GAME = new ByowCommand("quit & save game");
    public static final ByowCommand MOVE_NORTH = new ByowCommand("move north");
    public static final ByowCommand MOVE_WEST = new ByowCommand("move west");
    public static final ByowCommand MOVE_SOUTH = new ByowCommand("move south");
    public static final ByowCommand MOVE_EAST = new ByowCommand("move east");
    public static final ByowCommand LOAD = new ByowCommand("load game");
}
