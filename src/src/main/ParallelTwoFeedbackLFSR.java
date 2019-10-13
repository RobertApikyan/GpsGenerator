package src.main;

import src.polynomial.LFSR;
import src.polynomial.PolynomialState;

import java.util.Arrays;
import java.util.concurrent.*;

public class ParallelTwoFeedbackLFSR {

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                5,
                1000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10, false) {
                });

        // required code sequence length
        final int lfsrRunLength = 1024;
        // LFSR length
        final int lfsrLength = 33;
        // first feedback position
        final int f1 = 32;
        // second feedback position
        final int f2 = 33;
        // initial state of registers
        final int[] initialState = new int[lfsrLength];
        // setting all ones for initial state
        for (int i = 0; i < initialState.length; i++) {
            initialState[i] = 1;
        }
        // initializing ParallelTwoFeedbackLFSR instance with parameters initial sate and f1 and f2
        final ParallelTwoFeedbackLFSR psr = new ParallelTwoFeedbackLFSR(executor, initialState, f1, f2);
        // start generation process
        final int[] output = psr.generate(lfsrRunLength);
        // print the output code sequences
        System.out.println(Arrays.toString(output));
    }

    private volatile boolean isFinished = false;
    private final Executor executor;
    private final int f1;
    private final int f2;
    private final int[] initialState;
    private int requiredLength;

    public ParallelTwoFeedbackLFSR(Executor executor, int[] initialState, int f1, int f2) {
        this.executor = executor;
        this.initialState = initialState;
        this.f1 = f1;
        this.f2 = f2;
    }

    private int[] generate(int runLength) {
        this.requiredLength = runLength;
        isFinished = false;
        int[] output = new int[runLength];
        generateRange(output, new PolynomialState(initialState), f1, f2, 0);
        while (!isFinished) {}
        return output;
    }

    private void generateRange(final int[] output,
                               PolynomialState initialState,
                               final int f1,
                               final int f2,
                               final int step) {

        int[] indexes = defineValidIndexes(f1, f2, initialState.getValues().length, step);

        // Si + Sj = Sk
        final int i = indexes[0];
        final int j = indexes[1];
        final int k = indexes[2];

        LFSR polynomial = new LFSR(new int[]{f1, f2}, initialState.getValues().length);
        polynomial.setState(initialState);

        for (int index = step; index < k; index++) {

            if (index == i) {
                initialState = polynomial.captureState();
            }

            if (index == j) {
                PolynomialState currentPolynomialState = polynomial.captureState();
                final PolynomialState nextState = initialState.exOr(currentPolynomialState);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        generateRange(output, nextState, f1, f2, k);
                    }
                });
            }

            if (index >= output.length) {
                break;
            }

            int out = polynomial.process();
            output[index] = out;

            if (--requiredLength == 0) {
                isFinished = true;
            }
        }
    }

    private int[] defineValidIndexes(int f1, int f2, int n, int step) {
        int i = step - 1;
        int j;
        int k;

        boolean isRoleSatisfied;


        do {

            i++;
            j = i + f2 - f1;
            k = i + f2;

            isRoleSatisfied = i - n + f2 >= 0;
        }
        while (!isRoleSatisfied);

        return new int[]{i, j, k};
    }
}
