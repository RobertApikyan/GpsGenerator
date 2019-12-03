package src.polynomial.parallelCaGenerator;

import src.generators.CaGenerator;
import src.generators.DataUtils;
import src.polynomial.PolynomialOne;
import src.polynomial.PolynomialTwo;
import src.polynomial.parallel_fibonacci_lfsr.FibonacciParallelLFSR;
import src.utils.ElapsedTimeCounter;

import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.function.Consumer;

import static src.satellites.SatellitesCaFactory.*;
import static src.utils.Utils.timeWatch;

public class ParallelCa {

    public static void main(String[] args) {

        int[] satellite = SAT_14;

        CaGenerator caGenerator = new CaGenerator(new PolynomialOne(), new PolynomialTwo(satellite));
        int[] sequentialValues = new int[1024];

        long sequentialTime = timeWatch(() -> {
            for (int i = 0; i < 1024; i++) {
                sequentialValues[i] = caGenerator.generate();
            }
        });

        System.out.println("sequential time =\t"+sequentialTime);

        ElapsedTimeCounter parallelTimeCounter = new ElapsedTimeCounter();
        parallelTimeCounter.start();
        generate(satellite, output -> {
                long parallelTime = parallelTimeCounter.stop();
                System.out.println("parallel time =\t"+parallelTime);
        }
        );
    }

    public static void generate(int[] goldNumber,
                                Consumer<int[]> onComplete) {

        final int[] output = new int[1024];
        final FibonacciParallelLFSR firstParallelLfsr = new FibonacciParallelLFSR();
        final FibonacciParallelLFSR secondParallelLfsr = new FibonacciParallelLFSR();

        firstParallelLfsr.generate(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new int[]{3, 10}, new int[]{10}, 0, ignored -> {
                    if (secondParallelLfsr.isFinished()) {
                        onComplete.accept(output);
                    }
                },
                (pos, value) -> output[pos] = DataUtils.exOr(output[pos], value));

        secondParallelLfsr.generate(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new int[]{2, 3, 6, 8, 9, 10}, goldNumber, 0, ignored -> {
                    if (firstParallelLfsr.isFinished()) {
                        onComplete.accept(output);
                    }
                },
                (pos, value) -> output[pos] = DataUtils.exOr(output[pos], value));
    }
}
