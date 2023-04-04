import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Models an N-by-N percolation system.

public class Percolation {

    private boolean[][] grid;
    private int openSites;
    private int top;
    private int bottom;
    private final int gridSize;
    private WeightedQuickUnionUF graph;
    private WeightedQuickUnionUF graph2;

    // Create an N-by-N grid, with all sites blocked.

    public Percolation(int N) {

        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("N must be greater than zero");
        }

        grid = new boolean[N][N];
        openSites = 0;
        gridSize = N;
        graph = new WeightedQuickUnionUF((N * N) + 2);
        graph2 = new WeightedQuickUnionUF((N * N) + 1); //extra graph to fix backwash

        // extra site at the top and bottom to make percolation checking easier
        top = 0;
        bottom = (N * N) + 1;

        // connects top with every site in row zero and bottom with every site in row N-1
        for (int i = 0; i < N; i++) {
            if (i != N * (N - 1) + i) { // if statement to avoid bug with a 1x1 grid
                graph.union(top, encode(0, i));
                graph.union(bottom, encode(N - 1, i));
                graph2.union(top, encode(0, i));
            }
        }
    }

    // Open site (row, col) if it is not open already.
    public void open(int row, int col) {

        if (row < 0 || row > gridSize - 1 || col < 0 || col > gridSize - 1) {
            throw new IndexOutOfBoundsException("Row value is out of grid bounds");
        }
        if (!grid[row][col]) {
            grid[row][col] = true;
            openSites++;

            // connects site to the left if its open and withing the grid
            if (col - 1 >= 0 && isOpen(row, col - 1)) {
                graph.union(encode(row, col), encode(row, col - 1));
                graph2.union(encode(row, col), encode(row, col - 1));
            }
            // connects site to the right if its open and withing the grid
            if (col + 1 <= gridSize - 1 && isOpen(row, col + 1)) {
                graph.union(encode(row, col), encode(row, col + 1));
                graph2.union(encode(row, col), encode(row, col + 1));
            }
            // connects site to the bottom one if its open snd withing the grid
            if (row - 1 >= 0 && isOpen(row - 1, col)) {
                graph.union(encode(row, col), encode(row - 1, col));
                graph2.union(encode(row, col), encode(row - 1, col));
            }
            // connects site to the upward one if its open snd withing the grid
            if (row + 1 <= gridSize - 1 && isOpen(row + 1, col)) {
                graph.union(encode(row, col), encode(row + 1, col));
                graph2.union(encode(row, col), encode(row + 1, col));
            }
        }
    }


    // Is site (row, col) open?
    public boolean isOpen(int row, int col) {

        if (row < 0 || row > gridSize - 1 || col < 0 || col > gridSize - 1) {
            throw new IndexOutOfBoundsException("Row value is out of grid bounds or col");
        }
        return grid[row][col];
    }

    // Checks if site (row, col) is full by checking if the site is open and connected to any site in row 0
    public boolean isFull(int row, int col) {

        if (row < 0 || row > gridSize - 1 || col < 0 || col > gridSize - 1) {
            throw new IndexOutOfBoundsException("Row value is out of grid bounds or col");
        }
        return isOpen(row, col) && graph2.connected(top, encode(row, col));
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // checks if system percolate by checking if top and bottom are connected
    public boolean percolates() {

        return graph.connected(top, bottom);
    }

    // An integer ID (1...N) for site (row, col).
    private int encode(int row, int col) {

        return gridSize * row + col + 1;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        } else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
