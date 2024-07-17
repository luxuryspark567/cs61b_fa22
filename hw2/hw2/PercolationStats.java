package hw2;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private float[] per;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        per = new float[T];
        // Test for T times
        for (int looper = 0; looper < T; looper++) {
            Percolation p = pf.make(N);
            // Looper until p percolates
            while(!p.percolates()){
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                p.open(row, col);
            }
            per[looper] = (float)p.numberOfOpenSites() / N / N;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double sum = 0;
        for (float v : per) {
            sum += v;
        }
        return sum / per.length;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double localMean = mean();
        double squareSum = 0;
        for (float v : per) {
            squareSum += (v - localMean) * (v - localMean);
        }
        return Math.sqrt(squareSum / (per.length - 1));
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(per.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(per.length);
    }

    public static void main(String[] args) {
        PercolationFactory pf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(100, 1000, pf);
        System.out.println(ps.mean());
        System.out.println(ps.stddev());
        System.out.println(ps.confidenceLow());
        System.out.println(ps.confidenceHigh());
    }
}
