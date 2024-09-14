package byow.Attribute;

public class ByowCommand {

    private final String description;

    public ByowCommand(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return this.description;
    }
    public String description() {
        return description;
    }

    public static ByowCommand getCommandFromDir(Direction dir) {
        if (dir == null) {
            return null;
        }
        if (dir.equals(Directionset.NORTH)) {
            return ByowCommandSet.MOVE_NORTH;
        }
        else if (dir.equals(Directionset.WEST)) {
            return ByowCommandSet.MOVE_WEST;
        }
        else if (dir.equals(Directionset.SOUTH)) {
            return ByowCommandSet.MOVE_SOUTH;
        }
        else if (dir.equals(Directionset.EAST)) {
            return ByowCommandSet.MOVE_EAST;
        }

        return null;
    }
}
