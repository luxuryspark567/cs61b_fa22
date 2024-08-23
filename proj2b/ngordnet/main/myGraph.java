package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.LinkedList;

public class myGraph{
    // Adjacency List
    private LinkedList<Integer>[] lList;
    private int v = 0;
    private int e = 0;
    private int graphSize = 83000;
    //private int graphSize = 1000;
    //Create empty graph with v vertices
    public myGraph(int V) {
        graphSize = V;
        lList = new LinkedList[graphSize];
        for (int i = 0; i < graphSize; i++) {
            lList[i] = new LinkedList<>();
            v++;
        }
    }
    public myGraph(In in) {
        lList = new LinkedList[graphSize];
        for (int i = 0; i < this.graphSize; i++) {
            lList[i] = new LinkedList<>();
            v++;
        }

        int hyper;
        while(in.hasNextLine()) {
            //get from file
            String line = in.readLine();
            String[] split = line.split(",");
            int[] ids = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();

            hyper = ids[0];
            // Save edge to graph
            for (int i = 1; i < ids.length; i++) {
                if (hyper < graphSize && ids[i] < graphSize) {
                    addEdge(hyper, ids[i]);
                }
            }
        }
    }

    //add an edge v-w
    public void addEdge(int v, int w) {
        lList[v].add(w);
        e++;
    }
    //vertices adjacent to v
    public Iterable<Integer> adj(int v) {
        return lList[v];
    }

    //number of vertices
    int V() {
        return v;
    }
    //number of edges
    int E(){
        return e;
    }
}
