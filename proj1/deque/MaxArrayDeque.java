package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    Comparator<T> comp;
    public MaxArrayDeque(Comparator<T> c) {
        comp = c;
    }

    public T max() {
        return max(comp);
    }

    public T max(Comparator<T> c) {
        T result = get(0);
        for (T x: this) {
            if (c.compare(x, result) > 0) { // compare returns positive if x is larger
                result = x;
            }
        }
        return result;
    }
}
