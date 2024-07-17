package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T>{

    private static class IntNode<T> {
        private T item;
        private IntNode prev;
        private IntNode next;

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
    @Override
    public void addFirst(T item) {
        IntNode<T> newNode = new IntNode<>(item);
        IntNode pNext = sentinel.next;
        pNext.prev = newNode;
        sentinel.next = newNode;
        newNode.prev = sentinel;
        newNode.next = pNext;

        size += 1;
    }
    @Override
    public void addLast(T item) {
        IntNode<T> newNode = new IntNode<>(item);
        IntNode pPrev = sentinel.prev;
        pPrev.next = newNode;
        newNode.prev = pPrev;
        newNode.next = sentinel;
        sentinel.prev = newNode;

        size += 1;
    }
    /**
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    */

    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        IntNode<T> pNode = sentinel.next;
        while (pNode != sentinel) {
            System.out.print(pNode.item + " ");
            pNode = pNode.next;
        }
        System.out.println();
    }
    @Override
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
    @Override
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
    @Override
    public T get(int index) {
        /** for circular queue, it should be ok to have these weired indexes*/
        if (index >= size || index < 0) {
            return null;
        }

        IntNode<T> pNode = sentinel.next;

        while (index > 0) {
            pNode = pNode.next;
            index -= 1;
        }
        return pNode.item;
    }

    @Override
    public boolean equals(Object o) {
        LinkedListDeque<T> targetList = (LinkedListDeque) o;
        //length should meet
        if (size != targetList.size()) {
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

    @Override
    public String toString() {
        return toStringDeque();
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator<>();
    }

    private class LinkedListDequeIterator<Glorp> implements Iterator<Glorp> {

        private int index;
        public LinkedListDequeIterator() {
            index = 0;
        }
        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Glorp next() {
            Glorp result = (Glorp)get(index);
            index += 1;
            return result;
        }
    }
}
