package ngordnet.main;

public class DataIndexedCharMap<V> {
    private V[] items;
    public DataIndexedCharMap(int R) {
        items = (V[]) new Object[R];
    }

    public void set(char c, V v) {
        items[c] = v;
    }

    public V get(char c) {
        return items[c];
    }
}

