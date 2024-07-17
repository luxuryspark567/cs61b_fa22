package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class BakPercolation {

    private WeightedQuickUnionUF w;
    private int openHead;
    private int N;
    private int totalSizeNum;

    private int[] fullMarker;
    // create N-by-N grid, with all sites initially blocked
    public BakPercolation(int N) {
        this.N = N;
        this.openHead = N * N;
        this.totalSizeNum = N * N + 1;
        this.fullMarker = new int[N * N];
        w = new WeightedQuickUnionUF(this.totalSizeNum);
    }

    private boolean checkIndex(int row, int col){
        return row >= this.N || col >= this.N || row < 0 || col < 0;
    }
    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (checkIndex(row, col)) {
            return;
        }
        w.union(this.openHead, row * this.N + col);
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (checkIndex(row, col)) {
            return false;
        }
        return w.connected(this.openHead, row * this.N + col);
    }

    private boolean isFullHelper(int row, int col) {
        if (checkIndex(row, col)) {
            return false;
        }
        // mark as already searched
        if (1 == this.fullMarker[row * this.N + col]) {
            return false;
        }
        this.fullMarker[row * this.N + col] = 1;

        if (!isOpen(row, col)) {
            return false;
        }
        else if (0 == row) {
            return true;
        }
        else {
            return (isFullHelper(row, col - 1) || isFullHelper(row, col + 1) || isFullHelper(row - 1, col) || isFullHelper(row + 1, col));
        }
    }
    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        for (int i = 0; i < this.N * this.N; i++) {
            this.fullMarker[i] = 0;
        }
        return isFullHelper(row, col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return this.totalSizeNum - w.count();
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = 0; i < this.N; i++) {
            if (isFull(this.N - 1, i)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        BakPercolation p = new BakPercolation(3);

        p.open(0, 0);
        p.open(0, 1);
        p.open(1, 1);
        p.open(2, 1);

        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
    }
}
