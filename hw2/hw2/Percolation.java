package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Arrays;

public class Percolation {

    private WeightedQuickUnionUF w;
    private int N;
    private int matrixLen;
    private int headIndex;
    //private int tailIndex;
    private int[] openMarker;

    private int xytoIndex(int row, int col) {
        return this.N * row + col;
    }
    private boolean indexCheckInvalid(int row, int col) {
        return row >= this.N || col >= this.N || row < 0 || col < 0;
        //return xytoIndex(row, col) >= this.matrixLen || xytoIndex(row, col) < 0;
    }
    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        this.N = N;
        this.matrixLen = N * N;
        this.headIndex = N * N;
        //this.tailIndex = N * N + 1;
        this.openMarker = new int[N * N];
        w = new WeightedQuickUnionUF(N * N + 2);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (indexCheckInvalid(row, col)) {
            return;
        }
        if (isOpen(row, col)) {
            return;
        }

        // Mark as open
        this.openMarker[xytoIndex(row, col)] = 1;

        if (isOpen(row, col + 1)){
            w.union(xytoIndex(row, col), xytoIndex(row, col + 1));
        }

        if (isOpen(row, col - 1)){
            w.union(xytoIndex(row, col), xytoIndex(row, col - 1));
        }

        if (isOpen(row + 1, col)){
            w.union(xytoIndex(row + 1, col), xytoIndex(row, col ));
        }

        if (isOpen(row - 1, col)){
            w.union(xytoIndex(row - 1, col), xytoIndex(row, col));
        }

        if (row == 0){
            w.union(this.headIndex, xytoIndex(row, col));
        }
/*
        if (row == this.N - 1){
            w.union(this.tailIndex, xytoIndex(row, col));
        }

 */
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (indexCheckInvalid(row, col)) {
            return false;
        }
        return this.openMarker[xytoIndex(row, col)] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        //return w.connected(xytoIndex(row, col), this.headIndex) && ;
        return w.connected(xytoIndex(row, col), this.headIndex);
    }

    // number of open sites
    public int numberOfOpenSites() {
        //return this.N - w.count() + 1;
        return Arrays.stream(this.openMarker).sum();
    }

    // does the system percolate?
    public boolean percolates() {
        for (int looper = 0; looper < N; looper++){
            if (w.connected(this.headIndex, xytoIndex(this.N - 1, looper))) {
                return true;
            }
        }
        return false;
        //return w.connected(this.headIndex, this.tailIndex);
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(3);

        p.open(0, 0);
        p.open(0, 1);
        p.open(1, 1);
        p.open(2, 1);

        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
    }
}
