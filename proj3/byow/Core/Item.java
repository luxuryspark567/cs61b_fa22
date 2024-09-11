package byow.Core;

import java.io.Serializable;

public class Item implements Serializable {

    private static final String DESCRIPTION_DFT = "an undefined item";
    private static final int WEIGHT_DFT = 0;
    private static final int VALUE_DFT = 0;
    private static final int DURATION_DFT = 100;
    //private static final int SIGNATURE_DFT = 1;

    private String description;
    private int weight;
    private int value;

    private int durability;
    private Position pos;

    public Item () {
        this.description = DESCRIPTION_DFT;
        this.weight = WEIGHT_DFT;
        this.value = VALUE_DFT;
        this.durability = DURATION_DFT;
        this.pos = null;
    }

    public Item (int weight, int value, int dur) {
        this.description = DESCRIPTION_DFT;
        this.weight = weight;
        this.value = value;
        this.durability = dur;
        this.pos = null;
    }

    public Item (String descr, Position pos) {
        this.description = descr;
        this.weight = WEIGHT_DFT;
        this.value = VALUE_DFT;
        this.durability = DURATION_DFT;
        this.pos = Position.copyOf(pos);
    }

    public Item (String descr, int weight, int value, int dur, Position pos) {
        this.weight = weight;
        this.value = value;
        this.durability = dur;
        this.description = descr;
        this.pos = Position.copyOf(pos);
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public Position getPosition(){
        return Position.copyOf(this.pos);
    }

    public void setDescription(String desr) {
        this.description = desr;
    }

    public void setWeight (int weight) {
        this.weight = weight;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setPosition(Position pos) {
        this.pos = Position.copyOf(pos);
    }

    /**
     * how this item handle other objects, such as a key to a door.
     * */
    boolean handle(Object o) {
        return true;
    }
}
