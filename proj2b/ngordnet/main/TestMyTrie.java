package ngordnet.main;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class TestMyTrie {
    myTrie mr;

    public TestMyTrie(){
        mr = new myTrie();
    }

    @Test
    public void testConstruct() {
        mr.add("change", 1000);
        mr.add("change", 800);
        mr.add("change",800);
        mr.add("chance",800);
        mr.add("order",800);

        mr.add("orange",100);

        LinkedList<Integer> values;

        values = mr.get("change");
        assertTrue(values.contains(1000));
        assertTrue(values.contains(800));

        values = mr.get("chance");
        assertTrue(values.contains(800));

        values = mr.get("order");
        assertTrue(values.contains(800));

        values = mr.get("orange");
        assertTrue(values.contains(100));

        //values = mr.get("false");
        //assertTrue(values.contains(100));
    }
}
