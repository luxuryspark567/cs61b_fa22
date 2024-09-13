package byow.Attribute;

public class ByowCommandSet {

    // Start Menu command
    public static final ByowCommand IDLE = new ByowCommand("Idle opt");
    public static final ByowCommand CREATE_NEW_WORLD = new ByowCommand("create new world");
    public static final ByowCommand QUIT_AND_SAVE_GAME = new ByowCommand("quit & save game on the start menu");
    public static final ByowCommand QUIT_AND_SAVE_GAME2 = new ByowCommand("quit & save game while playing");
    public static final ByowCommand LOAD = new ByowCommand("load game on the start menu");
    public static final ByowCommand LOAD2 = new ByowCommand("load game while playing");

    // move command
    public static final ByowCommand MOVE_NORTH = new ByowCommand("move north");
    public static final ByowCommand MOVE_WEST = new ByowCommand("move west");
    public static final ByowCommand MOVE_SOUTH = new ByowCommand("move south");
    public static final ByowCommand MOVE_EAST = new ByowCommand("move east");

    // door command
    public static final ByowCommand LOCK = new ByowCommand("lock the door");
    public static final ByowCommand UNLOCK = new ByowCommand("unlock the door");
}
