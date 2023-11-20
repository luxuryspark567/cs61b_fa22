package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
  public void BuggyAListTest() {

      AListNoResizing<Integer> testArrayNoResize = new AListNoResizing<>();
      BuggyAList<Integer> testArrayResized = new BuggyAList<>();

      testArrayNoResize.addLast(4);
      testArrayNoResize.addLast(5);
      testArrayNoResize.addLast(6);

      testArrayResized.addLast(4);
      testArrayResized.addLast(5);
      testArrayResized.addLast(6);

      assertEquals(testArrayNoResize.removeLast(),testArrayResized.removeLast());
      assertEquals(testArrayNoResize.removeLast(),testArrayResized.removeLast());
      assertEquals(testArrayNoResize.removeLast(),testArrayResized.removeLast());
  }

  @Test
    public void randomizedTest() {

      AListNoResizing<Integer> L = new AListNoResizing<>();
      BuggyAList<Integer> L1 = new BuggyAList<>();

      int N = 5000;
      for (int i = 0; i < N; i += 1) {
          int operationNumber = StdRandom.uniform(0, 4);
          if (operationNumber == 0) {
              // addLast
              int randVal = StdRandom.uniform(0, 100);
              L.addLast(randVal);
              L1.addLast(randVal);
              System.out.println("addLast(" + randVal + ")");
          } else if (operationNumber == 1) {
              // size
              int size = L.size();
              int size1 = L1.size();
              assertEquals(size, size1);
              System.out.println("size: " + size);
          }
          else if (operationNumber == 2) {
              if (L.size() > 0) {
                  int tmpValue = L.getLast();
                  int tmpValue1 = L1.getLast();
                  assertEquals(tmpValue, tmpValue1);
                  System.out.println("L.getLast():" + tmpValue);
              }
          }
          else if (operationNumber == 3) {
              if (L.size() > 0) {
                  int tmpValue = L.removeLast();
                  int tmpValue1 = L1.removeLast();
                  assertEquals(tmpValue, tmpValue1);
                  System.out.println("L.removeLast(): " + tmpValue);
              }
          }
      }

  }
}
