package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void testEmptySize() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        assertEquals(0, L.size());
    }

    @Test
    public void testAddAndSize() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(99);
        L.addLast(99);
        assertEquals(2, L.size());
    }


    @Test
    public void testAddAndGetLast() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(99);
        assertEquals(99, (int)L.removeLast());
        L.addLast(36);
        assertEquals(36, (int)L.removeLast());
    }


    @Test
    public void testGet() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(99);
        assertEquals(99, (int)L.get(0));
        L.addLast(36);
        assertEquals(99, (int)L.get(0));
        assertEquals(36, (int)L.get(1));
    }


    @Test
    public void testRemove() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(99);
        assertEquals(99, (int)L.get(0));
        L.addLast(36);
        assertEquals(99, (int)L.get(0));
        L.removeLast();
        assertEquals(99, (int)L.get(0));
        L.addLast(100);
        assertEquals(100, (int)L.get(1));
        assertEquals(2, L.size());
    }

    /** Tests insertion of a large number of items.*/
    @Test
    public void testMegaInsert() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        int N = 1000000;
        for (int i = 0; i < N; i += 1) {
            L.addLast(i);
        }

        for (int i = 0; i < N; i += 1) {
            L.addLast(L.get(i));
        }
    }

    @Test
    public void addFirstTest() {
        ArrayDeque<Integer> testQue = new ArrayDeque<>();
        testQue.addFirst(100);
        testQue.addFirst(200);
        assertEquals(2, testQue.size());
        assertEquals(200, (int)testQue.get(0));
        assertEquals(100, (int)testQue.get(1));
    }

    @Test
    public void addLastTest() {
        ArrayDeque<Integer> testQue = new ArrayDeque<>();
        testQue.addLast(100);
        testQue.addLast(200);
        assertEquals(2, testQue.size());
        assertEquals(100, (int)testQue.get(0));
        assertEquals(200, (int)testQue.get(1));
    }

    @Test
    public void equalsTest() {
        ArrayDeque<Integer> testQue1 = new ArrayDeque<>();
        ArrayDeque<Integer> testQue2 = new ArrayDeque<>();
        testQue1.addFirst(100);
        testQue1.addFirst(200);
        testQue2.addFirst(100);
        testQue2.addFirst(200);
        assertEquals(true, testQue1.equals(testQue2));

        testQue1.addFirst(300);
        assertEquals(false, testQue1.equals(testQue2));

        testQue2.addFirst(400);
        assertEquals(false, testQue1.equals(testQue2));

        assertEquals(400, (int)testQue2.removeFirst());

        testQue2.addFirst(300);
        assertEquals(true, testQue1.equals(testQue2));
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String>  lld1 = new ArrayDeque<String>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    public void removeLastLargeDataTest() {
        ArrayDeque<Integer> testQue = new ArrayDeque<>();

        // add 0~255 to the array queue
        for (int i = 0; i < 256; i += 1)
        {
            testQue.addLast(i);
        }

        // check if every element is equal to 0~255
        assertEquals(256, testQue.size());

        for (int i = 0; i < 256; i += 1) {
            assertEquals(i, (int)testQue.get(i));
        }

        //remove half the elements in array queue
        for (int i = 0; i < 128; i += 1)
        {
            testQue.removeLast();
        }

        //check if the rest elements are equal to 0 ~ 127
        for (int i = 0; i < 128; i += 1) {
            assertEquals(i, (int)testQue.get(i));
        }

        //remove the rest 128 elements, and check if the buffer is cleared up

        for (int i = 0; i < 128; i += 1)
        {
            testQue.removeLast();
        }
        assertEquals(0, testQue.size());
    }
    @Test
    public void removeFirstLargeDataTest() {
        ArrayDeque<Integer> testQue = new ArrayDeque<>();
        for (int i = 0; i < 256; i += 1)
        {
            testQue.addFirst(i);
        }
        assertEquals(256, testQue.size());

        for (int i = 0; i < 256; i += 1)
        {
            testQue.removeFirst();
        }
        assertEquals(0, testQue.size());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        /*
        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        */
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            ad1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) ad1.removeLast(), 0.0);
        }
    }
}
