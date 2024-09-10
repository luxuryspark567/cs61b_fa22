package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

public class CommandMonitor {
    int parseState;
    StringBuilder randomKey;
    CommandNode cn;

    public CommandMonitor() {
        this.parseState = 0;
        this.randomKey = new StringBuilder();
        this.cn = new CommandNode(ByowCommandSet.IDLE);
    }
    public void initiate() {
        this.parseState = 0;
        this.randomKey = new StringBuilder();
        this.cn = new CommandNode(ByowCommandSet.IDLE);
    }

    // monitor the menu page one time, return a valid command
    public void monitorMenuPage (TERenderer ter) {

        boolean looperFlag = true;

        while (looperFlag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                System.out.println(c);

                switch (parseState) {
                    case 0: // idle state
                        if (c == 'l' || c == 'L') {
                            cn = new CommandNode(ByowCommandSet.LOAD);
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
                                    cn = new CommandNode(ByowCommandSet.CREATE_NEW_WORLD);
                                    looperFlag = false;
                                } else {
                                    cn = new CommandNode(ByowCommandSet.CREATE_NEW_WORLD, Long.parseLong(randomKey.toString()));
                                    looperFlag = false;
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
                            cn = new CommandNode(ByowCommandSet.QUIT_AND_SAVE_GAME);
                            looperFlag = false;
                        } else {
                            System.out.println("invalid quit & save command!");
                        }
                        parseState = 0;
                        break;
                    default:
                        parseState = 0;
                        break;
                }

                //render
                if (parseState == 1 && cn.bc == ByowCommandSet.IDLE) {
                    //ter.initialize(40, 40, 0, 0);
                    ter.renderText(randomKey.toString());
                }
            }
        }

    }

    public void executeCommands(Avatar hero, TETile[][] world, TERenderer ter) {
        // return true means not a command to break out
        // return false means a command to break outer while loop, and no need to monitor anymore.

        if (cn == null) {
            return;
        }

        if (cn.bc == ByowCommandSet.LOAD) {
            //ter.initialize(40, 40, 0, 0);
            ter.renderText("LOAD GAME");
        }
        else if (cn.bc == ByowCommandSet.CREATE_NEW_WORLD){
            //ter.initialize(40, 40, 0, 0);
            ter.renderText("CREATING NEW WORLD...");
            ter.renderPause(500);
        }
        else if (cn.bc == ByowCommandSet.QUIT_AND_SAVE_GAME) {
            //this.ter.initialize(40, 40, 0, 0);
            ter.renderText("GAME OVER");
        }
        else if (cn.bc == ByowCommandSet.MOVE_NORTH) {
            if (hero != null) {
                hero.MoveOneStep(Directionset.NORTH);
                ter.renderGamePage(hero, world);
            }
        }
        else if (cn.bc == ByowCommandSet.MOVE_WEST) {
            if (hero != null) {
                hero.MoveOneStep(Directionset.WEST);
                ter.renderGamePage(hero, world);
            }
        }
        else if (cn.bc == ByowCommandSet.MOVE_SOUTH) {
            if (hero != null) {
                hero.MoveOneStep(Directionset.SOUTH);
                ter.renderGamePage(hero, world);
            }
        }
        else if (cn.bc == ByowCommandSet.MOVE_EAST) {
            if (hero != null) {
                hero.MoveOneStep(Directionset.EAST);
                ter.renderGamePage(hero, world);
            }
        }
    }

    // monitor game page on time, return a valid command
    public void monitorGamePage() {

        boolean looperFlag = true;

        while (looperFlag) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                System.out.println(c);

                switch (parseState) {
                    case 0: // idle state
                        if (c == 'w' || c == 'W') {
                            cn = new CommandNode(ByowCommandSet.MOVE_NORTH);
                            looperFlag = false;
                        }
                        else if (c == 'a' || c == 'A') {
                            cn = new CommandNode(ByowCommandSet.MOVE_WEST);
                            looperFlag = false;
                        }
                        else if (c == 's' || c == 'S') {
                            cn = new CommandNode(ByowCommandSet.MOVE_SOUTH);
                            looperFlag = false;
                        }
                        else if (c == 'd' || c == 'D') {
                            cn = new CommandNode(ByowCommandSet.MOVE_EAST);
                            looperFlag = false;
                        }
                        else if (c == ':') {
                            parseState = 2;
                        }
                        break;
                    case 2:
                        if (c == 'q' || c == 'Q') {
                            cn = new CommandNode(ByowCommandSet.QUIT_AND_SAVE_GAME);
                            looperFlag = false;
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
        }
    }

    boolean isMoveCommand() {
        return this.cn.bc == ByowCommandSet.MOVE_NORTH
                || this.cn.bc== ByowCommandSet.MOVE_WEST
                || this.cn.bc == ByowCommandSet.MOVE_SOUTH
                || this.cn.bc == ByowCommandSet.MOVE_EAST;
    }

    boolean isIdleCommand() {
        return this.cn.bc == ByowCommandSet.IDLE;
    }
}