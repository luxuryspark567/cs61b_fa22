package byow.Attribute;

public class ByowCommandSet {

    // Start Menu command
    public static final ByowCommand IDLE = new ByowCommand("Idle opt");
    public static final ByowCommand CREATE_NEW_WORLD = new ByowCommand("create new world");
    public static final ByowCommand QUIT_AND_SAVE_GAME = new ByowCommand("quit & save game on the start menu");
    public static final ByowCommand QUIT_AND_SAVE_GAME2 = new ByowCommand("quit & save game while playing");
    public static final ByowCommand LOAD = new ByowCommand("load game on the start menu");
    public static final ByowCommand LOAD2 = new ByowCommand("load game while playing");

    public static final ByowCommand MIST_SWITCH = new ByowCommand("switch mist on or off");
    // move command
    public static final ByowCommand MOVE_NORTH = new ByowCommand("move north");
    public static final ByowCommand MOVE_WEST = new ByowCommand("move west");
    public static final ByowCommand MOVE_SOUTH = new ByowCommand("move south");
    public static final ByowCommand MOVE_EAST = new ByowCommand("move east");



    // Object operation command
    public static final ByowCommand OPTION_ONE = new ByowCommand("option 1");
    public static final ByowCommand OPTION_TWO = new ByowCommand("option 2");
    public static final ByowCommand OPTION_THREE = new ByowCommand("option 3");
    public static final ByowCommand OPTION_FOUR = new ByowCommand("option 4");
}
