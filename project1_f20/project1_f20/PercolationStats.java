import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private int times;
    private final double[] percThreshold;


    // Perform T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException(" N and T have to be greater than 0");
        }

        this.times = T;
        percThreshold = new double[T];

        for (int i = 0; i < T; i++) {

            // creates a blocked grid
            Percolation perc = new Percolation(N);

            // randomly opens sites until it percolates
            while (!perc.percolates()) {

                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                perc.open(row, col);
            }
            // collects the threshold data
            percThreshold[i] = (double) perc.numberOfOpenSites() / (N * N);
        }
    }


    // Sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(percThreshold);
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(percThreshold);
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(times));
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(times));
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}


