package bstmap;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V extends Comparable> implements Iterable<K>, Map61B<K, V> {

    private BSTNode root;

    private class BSTNode {
        //private BSTNode root;
        private int size;
        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;

        // construct
        public BSTNode(K key, V value) {
            this.size = 1;
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }
    public BSTMap () {
        root = null;
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    private boolean containsKey(BSTNode root, K key) {
        // return condition
        if (null == root) { // not found the given key
            return false;
        }

        int cmp = key.compareTo(root.key);

        if (cmp == 0) { // result is tree.key - key?? not sure need to test
            return true;
        }
        else if (cmp < 0) { // Search Left
            return containsKey(root.left, key);
        }
        else { // Search Right
            return containsKey(root.right, key);
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode root, K key) {
        // return condition
        if (null == root) { // not found the given key
            return null;
        }

        int cmp = key.compareTo(root.key);

        if (cmp == 0) { // result is tree.key - key?? not sure need to test
            return root.value;
        }
        else if (cmp < 0) { // Search Left
            return get(root.left, key);
        }
        else { // Search Right
            return get(root.right, key);
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    private int size(BSTNode root) {
        if (null == root) {
            return 0;
        }
        return root.size;
    }
    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }
    private BSTNode put(BSTNode root, K key, V value) {
        // create a new node
        if (null == root) {
            root = new BSTNode(key, value);
            return root;
        }
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            root.value = value; // update value to the new one
        }
        else if (cmp < 0) { // Search Left
            root.left = put(root.left, key, value);
        }
        else { // Search Right
            root.right = put(root.right, key, value);
        }
        root.size = 1 + size(root.left) + size(root.right);
        return root;
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<K>();
        // loop over all node, and add all key to a Set
        // how to looper over all nodes?
        return keySet(root, set);
        //throw new UnsupportedOperationException();
    }

    private Set<K> keySet(BSTNode root, Set<K> set) {

        //return condition
        if (root == null) { // if current
            return set;
        }
        // catch current key
        set.add(root.key);
        // catch all keys in left branch
        set = keySet(root.left, set);
        // catch all keys in right branch
        set = keySet(root.right, set);

        return set;
    }

    // get the min node in the tree
    private BSTNode getMin(BSTNode root) {
        if (null != root.left) {
            return getMin(root.left);
        }

        //if left branch is null, then this is the min value;
        return root;
    }
    // Remove and return the min
    private BSTNode removeMin(BSTNode root) {

        if (null == root.left) { // left is null, and right is not null, just replace with right branch
            return root.right;
        }
        //go to left branch
        root.left = removeMin(root.left);
        root.size = 1 + size(root.left) + size(root.right);
        return root;
    }

    public void removeMin() {
        root = removeMin(root);
    }

    private BSTNode getMax(BSTNode root) {
        if (null != root.right) {
            return getMax(root.right);
        }

        //if right branch is null, then this is the max value;
        return root;
    }
    // Remove and return the min
    private BSTNode removeMax(BSTNode root) {

        if (null == root.right) {
            return root.left;
        }
        root.right = removeMax(root.right);
        root.size = 1 + size(root.left) + size(root.right);
        return root;
    }

    public void removeMax() {
        root = removeMax(root);
    }
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {

        V result = get(key);
        //if (result != null)// if this line uncommented, there is a problem that key with a null value won't be able to be deleted
        root = remove(root, key);
        return result;
        //throw new UnsupportedOperationException();
    }
    private BSTNode remove(BSTNode root, K key) {
        if (null == root) {
            return null;
        }
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            // only three condition to consider:
            // 1, left is null, then replace current node with right branch
            if (null == root.left) {
                return root.right;
            }
            // 2, right is null, then replace current node with left branch
            if (null == root.right) {
                return root.left;
            }

            // 3, left and right are both valid, delete the min value in the right branch, and replace current node
            // with the removed min node in right
            BSTNode min = getMin(root.right);
            root.key = min.key;
            root.value = min.value;
            root.right = removeMin(root.right);
        }
        else if (cmp < 0) {
            root.left = remove(root.left, key);
        }
        else {
            root.right = remove(root.right, key);
        }
        root.size = 1 + size(root.left) + size(root.right);
        return root;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        if (get(key) == value) {
            return remove(key);
        }
        return null;
    }

    //private Set<K> set;
    @Override
    public Iterator<K> iterator() {
        Set<K> set = keySet();
        return set.iterator();
    }
/*
    private class BSTMapIterator implements Iterator<K> {

        public BSTMapIterator(){
            set = keySet();
        }
        @Override
        public boolean hasNext() {
            return !this.set.isEmpty();
        }

        @Override
        public K next() {
            return this.set.;
        }

        private BSTNode next(BSTNode travel) {

            return null;

        }
    }
 */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long start_ns = System.nanoTime();
        Stopwatch sw = new Stopwatch();
        BSTMap m = new BSTMap();

        m.put(500, "a");
        m.put(1000, "b");
        m.put(100, "c");
        m.put(900, "d");
        m.put(400, "e");
        m.put(200, "d");
/*
        m.removeMin();
        m.removeMin();

        m.removeMax();
        m.removeMax();

        System.out.println(m.remove(50));
        System.out.println(m.remove(400));

        System.out.println(m.remove(50));
        System.out.println(m.remove(400, "a"));
        System.out.println(m.remove(500, "d"));
        System.out.println(m.remove(400, "e"));
        System.out.println(m.remove(500, "a"));
        System.out.println(m.size());
*/
        for (Object travel: m) {
            System.out.println(travel);
        }
        return sw.elapsedTime();
        long end = System.currentTimeMillis();
        long end_ns = System.nanoTime();
        System.out.println("runtime is: " + (end - start) + "ms");
        System.out.println("runtime is: " + (end_ns - start_ns) + "ns");
    }
}
