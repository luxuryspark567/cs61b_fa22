package byow.Core;

import java.util.LinkedList;
import java.util.List;
public class CommandNode {
    ByowCommand bc;
    long rNum;

    public CommandNode (ByowCommand bc, long rNum) {
        this.bc = bc;
        this.rNum = rNum;
    }

    public CommandNode (ByowCommand bc) {
        this.bc = bc;
        this.rNum = 0;
    }

    public static List<CommandNode> getCommandList(String input) {
        char[] charArray = input.toCharArray();
        StringBuilder randomKey = new StringBuilder();
        List<CommandNode> cmdList = new LinkedList<>();

        // 0, idle
        // 1, "N###S": create new world
        // 2, ":Q": save and quite
        int parseState = 0;
        for (char c:charArray) {
            switch (parseState) {
                case 0: // idle state
                    if (c == 'l' || c == 'L') {
                        cmdList.add(new CommandNode(ByowCommandSet.LOAD));
                    }
                    else if (c == 'w' || c == 'W') {
                        cmdList.add(new CommandNode(ByowCommandSet.MOVE_NORTH));
                    }
                    else if (c == 'a' || c == 'A') {
                        cmdList.add(new CommandNode(ByowCommandSet.MOVE_WEST));
                    }
                    else if (c == 's' || c == 'S') {
                        cmdList.add(new CommandNode(ByowCommandSet.MOVE_SOUTH));
                    }
                    else if (c == 'd' || c == 'D') {
                        cmdList.add(new CommandNode(ByowCommandSet.MOVE_EAST));
                    }
                    else if (c == 'n' || c == 'N') {
                        randomKey.delete(0, randomKey.length());
                        parseState = 1;
                    }
                    else if (c == ':') {
                        parseState = 2;
                    }
                    break;
                case 1: //create new world command analysis
                    if (c >= '0' && c <= '9') {
                        randomKey.append(c);
                    } else {
                        if (c == 's' || c == 'S') {
                            // got a valid random key
                            if (randomKey.isEmpty()) {
                                cmdList.add(new CommandNode(ByowCommandSet.CREATE_NEW_WORLD));
                            } else {
                                cmdList.add(new CommandNode(ByowCommandSet.CREATE_NEW_WORLD, Integer.parseInt(randomKey.toString())));
                            }
                        } else {
                            randomKey.delete(0, randomKey.length());
                            System.out.println("invalid create new world command!");
                        }
                        parseState = 0;
                    }
                    break;
                case 2:
                    if (c == 'q' || c == 'Q') {
                        cmdList.add(new CommandNode(ByowCommandSet.QUIT_AND_SAVE_GAME));
                    } else {
                        System.out.println("invalid quit & save command!");
                    }
                    parseState = 0;
                    break;
                default:
                    parseState = 0;
                    break;
            }
        }

        return cmdList;
    }
}