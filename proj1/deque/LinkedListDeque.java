package deque;

public class LinkedListDeque<T> {

    private static class IntNode<T>{
        public T item;
        public IntNode prev;
        public IntNode next;

        public IntNode(T x) {
            item = x;
            prev = null;
            next = null;
        }
    }

    private IntNode sentinel;
    private int size;

    /**
     * 63 here limit the elements must be integer, right? how to fix this?
     * will change 63 to null work?
     * */
    public LinkedListDeque() {
        sentinel = new IntNode<T>(null);
        size = 0;
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    public void addFirst(T item) {
        IntNode<T> newNode = new IntNode<>(item);
        IntNode pNext = sentinel.next;
        pNext.prev = newNode;
        sentinel.next = newNode;
        newNode.prev = sentinel;
        newNode.next = pNext;

        size += 1;
    }

    public void addLast(T item) {
        IntNode<T> newNode = new IntNode<>(item);
        IntNode pPrev = sentinel.prev;
        pPrev.next = newNode;
        newNode.prev = pPrev;
        newNode.next = sentinel;
        sentinel.prev = newNode;

        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        IntNode<T> pNode = sentinel.next;
        while (pNode != sentinel) {
            System.out.print(pNode.item + " ");
            pNode = pNode.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        IntNode<T> pNext = sentinel.next.next;
        T result = (T) sentinel.next.item;
        sentinel.next = pNext;
        pNext.prev = sentinel;
        size -= 1;

        return result;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        IntNode<T> pPrev = sentinel.prev.prev;
        T result = (T) sentinel.prev.item;
        sentinel.prev = pPrev;
        pPrev.next = sentinel;
        size -= 1;
        return result;
    }

    public T get(int index) {
        /** for circular queue, it should be ok to have these weired indexes*/
        if (index >= size || index < 0) {
            return null;
        }

        IntNode<T> pNode = sentinel.next;

        while(index > 0) {
            pNode = pNode.next;
            index -= 1;
        }
        return pNode.item;
    }

    @Override
    public boolean equals(Object o) {
        LinkedListDeque<T> targetList = (LinkedListDeque)o;
        //length should meet
        if (size != targetList.size()){
            return false;
        }
        //each elements should meet
        IntNode<T> pNode = sentinel.next;
        IntNode<T> pTarget = targetList.sentinel.next;
        while (pNode != sentinel) {
            /**
             * WHY the first line is not working?
             * */
            //if (pNode.item != pTarget.item) {
            if (!pNode.item.equals(pTarget.item)) {
                return false;
            }
            pNode = pNode.next;
            pTarget = pTarget.next;
        }
        return true;
    }
 /**
    public T getRecursive(int index) {}
 */
}
