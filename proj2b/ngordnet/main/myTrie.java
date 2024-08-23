package ngordnet.main;

import java.util.LinkedList;

public class myTrie {
    private static final int R = 128; // ASCII
    private Node root;	// root of trie

    private static class Node {
        private char ch;
        private boolean isKey; // isKey is redundant, because is valueList is null, then it's not a key.
        private LinkedList<Integer> valueList = null;

        private DataIndexedCharMap<Node> next;
        private Node(char c, boolean b, int value, int R) {
            ch = c;
            isKey = b;
            if (b) { //is a valid key, then need to save to value list.
                valueList = new LinkedList<>();
                valueList.add(value);
            }
            else{
                valueList = null;
            }
            next = new DataIndexedCharMap<Node>(R);
        }
    }

    // constructor, build a trie
    public myTrie() {
        root = new Node(' ', false, 0, 128);
    }

    // add a key-value to the Trie
    public boolean add(String key, int value) {
        // transfer to char array
        char[] car = key.toCharArray();
        Node cur = root;

        // looper over the none final chars
        int i;
        for (i = 0; i < car.length - 1; i++) {

            // if position car[looper] is null, the needs to create a new node.
            if (cur.next.get(car[i]) == null) {
                cur.next.set(car[i], new Node(car[i], false, 0, 128));
            }

            // move to the next Node
            cur = cur.next.get(car[i]);
        }

        char finalChar = car[car.length - 1];
        // if position car[looper] is null, the needs to create a new node.
        if (cur.next.get(finalChar) == null) {
            cur.next.set(finalChar, new Node(finalChar, true, value, 128));
            return true;
        } else {
            // get the next node
            cur = cur.next.get(finalChar);

            // to mark the node
            // if it is already a key
            if (cur.isKey) { // is the same as "if (cur.valueList != null) {"
                //check if value is already existed in the list
                if (!cur.valueList.contains(value)) { // if value is not in the list, then add to the value list
                    cur.valueList.add(value);
                    return true;
                } else {
                    return false;
                }
            } else {
                cur.valueList = new LinkedList<>();
                cur.valueList.add(value);
                cur.isKey = true;
                return true;
            }
        }
    }

    // get an iterable for a given key.
    public LinkedList<Integer> get(String key) {

        // get one char each time;
        char[] car = key.toCharArray();
        Node cur = root;

        //search for key;
        int i = 0;
        for (i = 0; i < car.length; i++) {
            if (cur.next.get(car[i]) != null) {
                cur = cur.next.get(car[i]);
            }
            else { // if a null link is discovered along the way, then its is not in the Trie.
                return new LinkedList<>(); // return empty list
            }
        }
        if (cur == null || !cur.isKey) {
            return new LinkedList<>();
        }
        else {
            return cur.valueList; // return empty list
        }
    }

    // delete a given key
    public boolean delete(String key) {return false;}

}
