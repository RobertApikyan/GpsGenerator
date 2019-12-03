package src.main;

import src.polynomial.FibonacciLfsr;
import src.polynomial.GaloisLfsr;
import src.polynomial.parallel_fibonacci_lfsr.FibonacciParallelLFSR;
import src.polynomial.parallel_galois_lfsr.GaloisParallelLFSR;
import src.polynomial.polynomial_processor.PolynomialProcessor;
import src.utils.ElapsedTimeCounter;

import java.util.Arrays;

import static src.utils.Utils.timeWatch;


/**
 * Created by Robert on 30.09.2017.
 */
public class Main {

    public static void main(String[] args) {

        final GaloisParallelLFSR lfsr = new GaloisParallelLFSR();
        // Initial state of Galois registers, all set to 1
        final int[] initialState = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        // The feedback indexes
        final int[] exOrIndexes = new int[]{2, 3, 6, 8, 9, initialState.length - 1};
        // The maximum period of GaloisLFSR, period = 2^registersCount - 1
        final int maxRun = (int) Math.pow(2, initialState.length) - 1;

        // Parallel generation
        final ElapsedTimeCounter etc = new ElapsedTimeCounter();
        etc.start();
        lfsr.generate(initialState, exOrIndexes, 0, outputBits -> {
            final long timeElapseInParallel = etc.stop();
            System.out.println("Parallel\t" + timeElapseInParallel + "\t");
        });

        // Linear generation
        final int[] linearOutputBits = new int[maxRun];
        final GaloisLfsr polynomial = new GaloisLfsr(exOrIndexes, initialState.length);
        final long timeElapseInSequential = timeWatch(() -> {
            for (int i = 0; i < maxRun; i++) {
                linearOutputBits[i] = polynomial.process();
            }
        });

        System.out.println("Sequential\t" + timeElapseInSequential );
    }
}
