package deque;

import java.util.Comparator;

public class TestStringComparator<T> implements Comparator<T> {

    @Override
    public int compare(Object o1, Object o2) {
        String tmp1 = (String) o1;
        String tmp2 = (String) o2;
        return tmp1.compareTo(tmp2);
    }
}
