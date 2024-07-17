package hw2;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class TestPercolation {
    Percolation p1 = new Percolation(5);
    Percolation p2 = new Percolation(5);

    Percolation p3 = new Percolation(5);

    // Construct
    public TestPercolation() {
        p1.open(0, 0);
        p1.open(0, 1);
        p1.open(1, 1);
        p1.open(2, 1);
        p1.open(2, 2);
        p1.open(2, 3);
        p1.open(3, 3);
        p1.open(4, 3);
        p1.open(4, 4);

        p2.open(0, 0);
        p2.open(0, 1);
        p2.open(1, 1);
        p2.open(1, 2);
        p2.open(2, 1);
        p2.open(2, 2);
        p2.open(2, 3);
        p2.open(2, 4);

        p3.open(0, 0);
        p3.open(1, 0);
        p3.open(1, 3);
        p3.open(1, 4);
        p3.open(2, 0);
        p3.open(2, 3);
        p3.open(2, 4);
    }
    @Test
    public void TestOpen() {
        assertEquals(p1.numberOfOpenSites(), 9);
        assertEquals(p2.numberOfOpenSites(), 8);
    }
    @Test
    public void TestIsOpen() {
        assertTrue(p1.isOpen(0, 0));
        assertTrue(p1.isOpen(0, 1));
        assertFalse(p1.isOpen(0, 2));
        assertFalse(p1.isOpen(0, 3));
        assertFalse(p1.isOpen(0, 4));
        assertFalse(p1.isOpen(1, 0));
        assertTrue(p1.isOpen(1, 1));
        assertFalse(p1.isOpen(1, 2));
        assertFalse(p1.isOpen(1, 3));
        assertFalse(p1.isOpen(1, 4));
        assertFalse(p1.isOpen(2, 0));
        assertTrue(p1.isOpen(2, 1));
        assertTrue(p1.isOpen(2, 2));
        assertTrue(p1.isOpen(2, 3));
        assertFalse(p1.isOpen(2, 4));
        assertFalse(p1.isOpen(3, 0));
        assertFalse(p1.isOpen(3, 1));
        assertFalse(p1.isOpen(3, 2));
        assertTrue(p1.isOpen(3, 3));
        assertFalse(p1.isOpen(3, 4));
        assertFalse(p1.isOpen(4, 0));
        assertFalse(p1.isOpen(4, 1));
        assertFalse(p1.isOpen(4, 2));
        assertTrue(p1.isOpen(4, 3));
        assertTrue(p1.isOpen(4, 4));

        assertTrue(p2.isOpen(0, 0));
        assertTrue(p2.isOpen(0, 1));
        assertFalse(p2.isOpen(0, 2));
        assertFalse(p2.isOpen(0, 3));
        assertFalse(p2.isOpen(0, 4));
        assertFalse(p2.isOpen(1, 0));
        assertTrue(p2.isOpen(1, 1));
        assertTrue(p2.isOpen(1, 2));
        assertFalse(p2.isOpen(1, 3));
        assertFalse(p2.isOpen(1, 4));
        assertFalse(p2.isOpen(2, 0));
        assertTrue(p2.isOpen(2, 1));
        assertTrue(p2.isOpen(2, 2));
        assertTrue(p2.isOpen(2, 3));
        assertTrue(p2.isOpen(2, 4));
        assertFalse(p2.isOpen(3, 0));
        assertFalse(p2.isOpen(3, 1));
        assertFalse(p2.isOpen(3, 2));
        assertFalse(p2.isOpen(3, 3));
        assertFalse(p2.isOpen(3, 4));
        assertFalse(p2.isOpen(4, 0));
        assertFalse(p2.isOpen(4, 1));
        assertFalse(p2.isOpen(4, 2));
        assertFalse(p2.isOpen(4, 3));
        assertFalse(p2.isOpen(4, 4));
    }
    @Test
    public void TestIsFull() {
        assertTrue(p1.isFull(0, 0));
        assertTrue(p1.isFull(0, 1));
        assertFalse(p1.isFull(0, 2));
        assertFalse(p1.isFull(0, 3));
        assertFalse(p1.isFull(0, 4));
        assertFalse(p1.isFull(1, 0));
        assertTrue(p1.isFull(1, 1));
        assertFalse(p1.isFull(1, 2));
        assertFalse(p1.isFull(1, 3));
        assertFalse(p1.isFull(1, 4));
        assertFalse(p1.isFull(2, 0));
        assertTrue(p1.isFull(2, 1));
        assertTrue(p1.isFull(2, 2));
        assertTrue(p1.isFull(2, 3));
        assertFalse(p1.isFull(2, 4));
        assertFalse(p1.isFull(3, 0));
        assertFalse(p1.isFull(3, 1));
        assertFalse(p1.isFull(3, 2));
        assertTrue(p1.isFull(3, 3));
        assertFalse(p1.isFull(3, 4));
        assertFalse(p1.isFull(4, 0));
        assertFalse(p1.isFull(4, 1));
        assertFalse(p1.isFull(4, 2));
        assertTrue(p1.isFull(4, 3));
        assertTrue(p1.isFull(4, 4));

        assertTrue(p2.isFull(0, 0));
        assertTrue(p2.isFull(0, 1));
        assertFalse(p2.isFull(0, 2));
        assertFalse(p2.isFull(0, 3));
        assertFalse(p2.isFull(0, 4));
        assertFalse(p2.isFull(1, 0));
        assertTrue(p2.isFull(1, 1));
        assertTrue(p2.isFull(1, 2));
        assertFalse(p2.isFull(1, 3));
        assertFalse(p2.isFull(1, 4));
        assertFalse(p2.isFull(2, 0));
        assertTrue(p2.isFull(2, 1));
        assertTrue(p2.isFull(2, 2));
        assertTrue(p2.isFull(2, 3));
        assertTrue(p2.isFull(2, 4));
        assertFalse(p2.isFull(3, 0));
        assertFalse(p2.isFull(3, 1));
        assertFalse(p2.isFull(3, 2));
        assertFalse(p2.isFull(3, 3));
        assertFalse(p2.isFull(3, 4));
        assertFalse(p2.isFull(4, 0));
        assertFalse(p2.isFull(4, 1));
        assertFalse(p2.isFull(4, 2));
        assertFalse(p2.isFull(4, 3));
        assertFalse(p2.isFull(4, 4));

        assertTrue(p3.isFull(0, 0));
        assertFalse(p3.isFull(0, 1));
        assertFalse(p3.isFull(0, 2));
        assertFalse(p3.isFull(0, 3));
        assertFalse(p3.isFull(0, 4));
        assertTrue(p3.isFull(1, 0));
        assertFalse(p3.isFull(1, 1));
        assertFalse(p3.isFull(1, 2));
        assertFalse(p3.isFull(1, 3));
        assertFalse(p3.isFull(1, 4));
        assertTrue(p3.isFull(2, 0));
        assertFalse(p3.isFull(2, 1));
        assertFalse(p3.isFull(2, 2));
        assertFalse(p3.isFull(2, 3));
        assertFalse(p3.isFull(2, 4));
        assertFalse(p3.isFull(3, 0));
        assertFalse(p3.isFull(3, 1));
        assertFalse(p3.isFull(3, 2));
        assertFalse(p3.isFull(3, 3));
        assertFalse(p3.isFull(3, 4));
        assertFalse(p3.isFull(4, 0));
        assertFalse(p3.isFull(4, 1));
        assertFalse(p3.isFull(4, 2));
        assertFalse(p3.isFull(4, 3));
        assertFalse(p3.isFull(4, 4));
    }
    @Test
    public void TestNumberOfOpenSites() {
        assertEquals(p1.numberOfOpenSites(), 9);
        assertEquals(p2.numberOfOpenSites(), 8);
    }

    @Test
    public void TestPercolates() {
        assertTrue(p1.percolates());
        assertFalse(p2.percolates());
    }
}
