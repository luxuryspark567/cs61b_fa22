package deque;

public class ArrayDeque <T>{
    private T[] arr;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        nextFirst = 4;
        nextLast = 5;
        arr = (T[]) new Object[8];
    }

    private void resize(int capacity) {
        // apply space for the new buffer pBuf;
        T[] pBuf = (T[]) new Object[capacity];

        // copy from the nextFirst to nextLast to the middle of the new buff,
        // why the middle? maybe the data won't touch both ends if start from the middle
        // because mod function need to be applied if index is over the buffer length.
        int curr = (nextFirst + 1) % arr.length;
        for (int i = 0; i < size; i++) {
            pBuf[i] = arr[curr];
            curr = (curr + 1) % arr.length;
        }

        //change buffer pointer.
        arr = pBuf;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    public void addFirst(T item) {
        // add item to array;
        if (size == arr.length) {
            resize(size * 2);
        }

        // move your ass for the new item
        arr[nextFirst] = item;
        if (nextFirst == 0) {
            nextFirst = arr.length - 1;
        }
        else {
            nextFirst -= 1;
        }

        size += 1;
    }

    public void addLast(T item) {
        // resize if buffer is full;
        if (size == arr.length) {
            resize(size * 2);
        }

        //move ass for the new item
        arr[nextLast] = item;
        if (nextLast == arr.length - 1) {
            nextLast = 0;
        }
        else {
            nextLast += 1;
        }
        size += 1;
    }

    public boolean isEmpty () {
        return size == 0;
    }


    public int size() {
        return size;
    }

    public void printDeque() {
        int looper = 0;
        while (looper < size) {
            System.out.print(this.get(looper) + " ");
            looper += 1;
        }
        System.out.println();
    }

    public T removeFirst() {
        //check if space is empty
        if (0 == size) {
            return null;
        }

        //store removed item and clear up
        nextFirst = (nextFirst + 1) % arr.length;
        T result = arr[nextFirst];
        arr[nextFirst] = null;
        size -= 1;

        if ((float)size / arr.length < 0.25 && arr.length > 8) {
            resize(arr.length / 2);
        }
        return result;
    }

    public T removeLast() {
        //check if space is empty
        if (0 == size) {
            return null;
        }

        // remove item;
        nextLast = nextLast - 1;
        if (nextLast < 0) {
            nextLast = arr.length - 1;
        }

        //store removed item and clear up
        T result = arr[nextLast];
        arr[nextLast] = null;
        size -= 1;

        if ((float)size / arr.length < 0.25 && arr.length > 8) {
            resize(arr.length / 2);
        }
        return result;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return arr[(nextFirst + index + 1) % arr.length];
    }

    public boolean equals(Object o) {
        ArrayDeque<T> Target = (ArrayDeque<T>) o;
        if (size != Target.size) {
            return false;
        }

        // The stop condition is i == nextLast;
        int i = 0;
        while (i < size) {
            if (!Target.get(i).equals(this.get(i))) {
                return false;
            }
            i += 1;
        }

        return true;
    }
}
