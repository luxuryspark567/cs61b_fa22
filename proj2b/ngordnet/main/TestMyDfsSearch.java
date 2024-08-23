package ngordnet.main;

import edu.princeton.cs.algs4.In;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

public class TestMyDfsSearch {
    myGraph mg;
    public TestMyDfsSearch() {
        String hyponymFile = "./data/wordnet/hyponyms16.txt";
        mg = new myGraph(new In(hyponymFile));
    }
    @Test

    public void TestDfsSearch() {
        myDfsSearch mds;
        LinkedList<Integer> l;

        mds = new myDfsSearch(mg,2);
        l= new LinkedList<>();

        for (int i : mds.vertexes()) {
            l.add(i);
        }

        assertTrue(l.contains(2));
        assertTrue(l.contains(3));
        assertTrue(l.contains(4));
        assertTrue(l.contains(5));

        mds = new myDfsSearch(mg,6);
        l= new LinkedList<>();

        for (int i : mds.vertexes()) {
            l.add(i);
        }

        assertTrue(l.contains(6));
        assertTrue(l.contains(7));
        assertTrue(l.contains(8));
        assertTrue(l.contains(9));
        assertTrue(l.contains(10));

        mds = new myDfsSearch(mg,11);
        l= new LinkedList<>();

        for (int i : mds.vertexes()) {
            l.add(i);
        }

        assertTrue(l.contains(11));
        assertTrue(l.contains(12));
        assertTrue(l.contains(13));
    }
}
