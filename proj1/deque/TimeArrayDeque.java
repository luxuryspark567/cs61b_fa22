package deque;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeArrayDeque {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> N = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for (int i = 10000; i <= 1280000; i *= 2) {
            Stopwatch sw = new Stopwatch();
            ArrayDeque <Integer> ad1 = new ArrayDeque<>();
            for (int j = 0; j < i; j += 1) {
                ad1.addLast(100);
            }
            double timeInSeconds = sw.elapsedTime();

            N.addLast(i);
            times.addLast(timeInSeconds);
            opCounts.addLast(i);
        }
        System.out.println("Timing table for addLast");
        printTimingTable(N, times, opCounts);

    }
}
