package byow.Core;

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
}
