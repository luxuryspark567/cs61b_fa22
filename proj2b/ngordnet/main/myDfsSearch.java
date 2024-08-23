package ngordnet.main;

import java.util.LinkedList;

public class myDfsSearch {
    private int size;
    private myGraph mg;
    private LinkedList<Integer> ll;
    public myDfsSearch(myGraph mg, int v) {
        ll = new LinkedList<>();
        this.mg = mg;
        myPreorderDfs(v);
    }

    public void myPreorderDfs (int v) {
        // pre-order: visit before going deep
        ll.add(v);
        for (int n: mg.adj(v)) {
            myPreorderDfs(n);
        }
    }
    public Iterable<Integer> vertexes() {
        return ll;
    }
    int size() {
        return size;
    }
}
