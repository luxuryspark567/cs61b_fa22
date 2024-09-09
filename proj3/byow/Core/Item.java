package byow.Core;

public class Item {
    private String description;
    private int weight;
    private int value;

    public Item () {
        this.weight = 0;
        this.value = 0;
    }

    public Item (int weight, int value) {
        this.weight = weight;
        this.value = value;
    }
    public Item (int weight, int value, String description) {
        this.weight = weight;
        this.value = value;
        this.description = description;
    }
}
