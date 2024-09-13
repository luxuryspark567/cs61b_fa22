package byow.Attribute;

import java.io.Serializable;

// size of an object
public class Size implements Serializable {
    int w;
    int h;

    public Size () {
        this.w = 0;
        this.h = 0;
    }
    public Size (int w, int h) {
        this.w = w;
        this.h = h;
    }

    public int getW(){
        return this.w;
    }

    public int getH(){
        return this.h;
    }

    public void setW(int w){
        this.w = w;
    }

    public void setH(int h){
        this.h = h;
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Size(");
        s.append(this.w);
        s.append(", ");
        s.append(this.h);
        s.append(")");
        return s.toString();
    }

    public static Size copyOf(Size size) {
        if (size == null) {
            return null;
        }

        return new Size(size.w, size.h);
    }
}