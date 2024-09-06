package byow.Core;

public class TurnPoint {
    private Position pos;
    private int totalWidth;
    private int totalHeight;
    private boolean[] expandable;

    public TurnPoint(int w, int h, Position pos, boolean[] dir) {
        this.totalWidth = w;
        this.totalHeight = h;
        this.pos = pos;
        this.expandable = new boolean[4];
        this.expandable[0] = dir[0]; //north;
        this.expandable[1] = dir[1]; //west;
        this.expandable[2] = dir[2]; //south;
        this.expandable[3] = dir[3]; //east;
    }

    public Position getPosition() {
        return this.pos;
    }

    public int getTotalWidth() {
        return totalWidth;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    public boolean[] getDirection() {
        return this.expandable;
    }

    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public void setTotalWidth(int w) {
        this.totalWidth = w;
    }
    public void setTotalHeight(int h) {
        this.totalHeight = h;
    }

    public void setDirection(boolean[] dir) {
        for (int i = 0; i < 4; i++) {
            this.expandable[i] = dir[i];
        }
    }
}
