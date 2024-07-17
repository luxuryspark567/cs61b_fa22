package deque;

public interface Deque<T> {

    public void addFirst(T item);
    public void addLast(T item);
    //public boolean isEmpty();
    public int size();
    public void printDeque();
    public T removeFirst();
    public T removeLast();
    public T get(int index);

    default public boolean isEmpty() {
        return size() == 0;
    }


    default public String toStringDeque() {
        StringBuilder outPut = new StringBuilder("{");
        for (int i = 0; i < size() - 1; i++) {
            outPut.append(get(i));
            outPut.append(", ");
        }

        if (size() > 0) {
            outPut.append(get(size() - 1));
        }

        outPut.append("}");
        return outPut.toString();
    }
}
