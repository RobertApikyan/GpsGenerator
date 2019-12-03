package src.main;

import src.polynomial.FibonacciLfsr;
import src.polynomial.parallel_fibonacci_lfsr.FibonacciParallelLFSR;
import src.utils.ElapsedTimeCounter;

import java.util.Arrays;

import static src.utils.Utils.timeWatch;

public class ParallelAndSequentialLfsrOutputGeneration {

    public static void main(String[] args) {
        final FibonacciParallelLFSR lfsr = new FibonacciParallelLFSR();
        // Initial state of FibonacciLfsr's registers, all set to 1
        final int[] initialState = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        // The feedback indexes
        final int[] exOrIndexes = new int[]{2, 3, 6, 8, 9,initialState.length - 1};
        // The maximum period of FibonacciLfsr, period = 2^registersCount - 1
        final int maxRun = (int) Math.pow(2, initialState.length) - 1;

        // Parallel generation
        final ElapsedTimeCounter etc = new ElapsedTimeCounter();
        etc.start();
        lfsr.generate(initialState, exOrIndexes, 0, outputBits -> {
            final long timeElapseInParallel = etc.stop();
            System.out.println("Parallel\t" + timeElapseInParallel+"\t"+ Arrays.toString(outputBits));
        });

        // Linear generation
        final int[] linearOutputBits = new int[maxRun];
        final FibonacciLfsr polynomial = new FibonacciLfsr(exOrIndexes, initialState.length);
        final long timeElapseInSequential = timeWatch(() -> {
            for (int i = 0; i < maxRun; i++) {
                linearOutputBits[i] = polynomial.process();
            }
        });

        System.out.println("Sequential\t"+timeElapseInSequential +"\t"+ Arrays.toString(linearOutputBits));
    }
}