package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestMaxArrayDeque {

    @Test
    public void maxDefaultCompareFunction() {
        TestIntegerComparator<Integer> testComparator = new TestIntegerComparator<Integer>();
        MaxArrayDeque<Integer> testDeque = new MaxArrayDeque(testComparator);
        testDeque.addLast(100);
        testDeque.addLast(200);
        testDeque.addLast(300);
        testDeque.addLast(400);

        assertEquals(400, (int)testDeque.max());

    }
    @Test
    public void maxThirdPartyCompareFunction() {
        TestIntegerComparator<Integer> testIntCp = new TestIntegerComparator<Integer>();
        TestStringComparator<String> testStrCp = new TestStringComparator<String>();
        MaxArrayDeque<String> testDeque = new MaxArrayDeque(testIntCp);
        testDeque.addLast("Alpha");
        testDeque.addLast("Hero");
        testDeque.addLast("Link");
        testDeque.addLast("Zelda");

        assertEquals("Zelda", (String)testDeque.max(testStrCp));

    }
}
