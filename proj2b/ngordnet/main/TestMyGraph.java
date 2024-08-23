package ngordnet.main;

import edu.princeton.cs.algs4.In;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
public class TestMyGraph {

    @Test
    public void TestAddEdge() {
        myGraph mg = new myGraph(100);
        mg.addEdge(1, 2);
        mg.addEdge(1, 3);
        mg.addEdge(1, 4);
        mg.addEdge(2,5);
        mg.addEdge(2,6);
        mg.addEdge(2,7);

        Iterable<Integer> iter = mg.adj(2);
        LinkedList<Integer> l = new LinkedList<>();
        for( int i: iter) {
            l.add(i);
        }
        assertFalse(l.contains(1));
        assertFalse(l.contains(2));
        assertFalse(l.contains(3));
        assertTrue(l.contains(5));
        assertTrue(l.contains(6));
        assertTrue(l.contains(7));
    }

    @Test
    public void TestStreamConstruct() {
        String hyponymFile = "./data/wordnet/hyponyms16.txt";
        myGraph mg = new myGraph(new In(hyponymFile));

        Iterable<Integer> iter = mg.adj(8);
        LinkedList<Integer> l = new LinkedList<>();
        for( int i: iter) {
            l.add(i);
        }
        assertFalse(l.contains(1));
        assertFalse(l.contains(2));
        assertFalse(l.contains(3));
        assertTrue(l.contains(9));
        assertTrue(l.contains(10));


        iter = mg.adj(8);
        l = new LinkedList<>();
        for( int i: iter) {
            l.add(i);
        }
        assertFalse(l.contains(1));
        assertFalse(l.contains(2));
        assertFalse(l.contains(3));
        assertTrue(l.contains(10));
    }
}
