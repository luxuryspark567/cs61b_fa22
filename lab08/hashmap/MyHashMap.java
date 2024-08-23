package hashmap;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private int size;
    private int initialSize;
    private final double loadFactor;

    private Collection<Node>[] hm;
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;
        //  Initialize hash table
        hm = createTable(initialSize);

        // Initialize each chain
        for (int index = 0; index < this.initialSize; index++) {
            hm[index] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
        //throw new UnsupportedOperationException();
    }
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        this.size = 0;
        this.hm = createTable(initialSize);
        // Initialize each chain
        for (int index = 0; index < this.initialSize; index++) {
            hm[index] = createBucket();
        }
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {

        // Get bucket
        int index = getIndex(key);

        // check if key is in the bucket
        for (Node ele:hm[index]) {
            if (ele.key.equals(key)) {
                return true;
            }
        }

        return false;
        //throw new UnsupportedOperationException();
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        // Get bucket
        int index = getIndex(key);

        // check if key is in the bucket
        for (Node ele:hm[index]) {
            if (ele.key.equals(key)) {
                return ele.value;
            }
        }
        return null;
        //throw new UnsupportedOperationException();
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
        //throw new UnsupportedOperationException();
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value){
        // Get bucket
        int index = getIndex(key);
        boolean flag = false;
        // check if key is in the bucket
        for (Node ele:hm[index]) {
            if (ele.key.equals(key)) {
                //replace value is found the same key
                ele.value = value;
                flag = true;
            }
        }

        // No identical Key, then add a new node;
        if (!flag) {
            hm[index].add(createNode(key,value));
            this.size++;
        }

        // check if it needs to resize the hash map
        if ((double)this.size / this.initialSize > this.loadFactor) {
            resize();
        }
        //throw new UnsupportedOperationException();
    }

    // Multiply the hash map
    private void resize() {
        Collection<Node>[] hmOld = this.hm;
        int loopLimit = this.initialSize;
        this.initialSize = this.initialSize * 2;
        this.clear();


        // move node from old hm to new hm
        for (int index = 0; index < loopLimit; index++) { // loop each chain
            for (Node ele:hmOld[index]) { // loop each node
                this.put(ele.key, ele.value);
            }
        }

    }

    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();
        for (Collection<Node> c: hm) {
            for (Node ele: c) {
                result.add(ele.key);
            }
        }
        return result;
        //throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key){
        V result;
        result = get(key);
        if (result != null) {
            // remove op
            int index = getIndex(key);
            Node mark = null;
            for (Node e: hm[index]) {
                if (e.key.equals(key)) {
                    mark = e;
                    //this.hm[index].remove(e);
                }
            }
            this.hm[index].remove(mark);
        }
        return result;
        //throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value){
        V result;
        result = get(key);
        if (result != null && result.equals(value)) {
            // remove op
            this.remove(key);
        }
        return result;
        //throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        Set<K> s = keySet();
        return s.iterator();
    }
/*
    private class myHashMapIterator implements Iterator<Node> {

        public myHashMapIterator() {

        }
        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }
        public Node next() {
            throw new UnsupportedOperationException();
        }

    }

 */

    private int getIndex(K key){
        return key.hashCode() & 0x7FFFFFF % this.initialSize;
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> mhm = new MyHashMap<>();
        mhm.put("what", 100);
        mhm.put("the", 101);
        mhm.put("fuck", 102);

        for (String k: mhm) {
            System.out.println(k);
        }

        mhm.remove("what");
        mhm.remove("the", 101);

        for (String k: mhm) {
            System.out.println(k);
        }
        List<String> hl = new ArrayList<>();
        List<String> ll = new LinkedList<>();

        Set<String> hs = new HashSet<>();
        Set<String> ts = new TreeSet<>();
        Set<String> lhs = new LinkedHashSet<>();

        Map<String, Integer> hm = new HashMap<>();
        Map<String, Integer> tm = new TreeMap<>();

        Queue<String> q = new ArrayDeque<>();
        Deque<String> dq = new ArrayDeque<>();
        Queue<String> pq = new PriorityQueue<>();

        WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(100);

        System.out.print(-12%11);
    }
}
