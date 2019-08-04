package src.main;

import src.polynomial.GenericPolynomial;
import src.polynomial.PolynomialState;

import java.util.Arrays;
import java.util.concurrent.*;

import static src.utils.Utils.timeWatch;

public class ParallelTwoFeedbackLFSR {

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                5,
                1000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10, false) {
                });

        final int lfsrRunLength = 1024;
        final int lfsrLength = 10;
        final int f1 = 7;
        final int f2 = 10;
        final int[] initialState = new int[lfsrLength];

        for (int i = 0; i < initialState.length; i++) {
            initialState[i] = 1;
        }

        final ParallelTwoFeedbackLFSR psr = new ParallelTwoFeedbackLFSR(executor, initialState, f1, f2);
        Runnable psrBlock = new Runnable() {
            @Override
            public void run() {
                final int[] output = psr.generate(lfsrRunLength);
                System.out.println(Arrays.toString(output));
            }
        };
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

    private void generateRange(final int[] output, PolynomialState initialState,
                               final int f1,
                               final int f2,
                               final int step) {

        int[] indexes = defineValidIndexes(f1, f2, initialState.getValues().length, step);

        // Si + Sj = Sk
        final int i = indexes[0];
        final int j = indexes[1];
        final int k = indexes[2];

        GenericPolynomial polynomial = new GenericPolynomial(new int[]{f1, f2}, initialState.getValues().length);
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

        boolean isFirstRoleSatisfied;
        boolean isSecondRoleSatisfied;

        do {

            i++;
            j = i + f2 - f1;
            k = i + f2;

            isFirstRoleSatisfied = i - n + f2 >= 0;
            isSecondRoleSatisfied = i - n + 2 * f2 - f1 >= 0;
        } while (!isFirstRoleSatisfied || !isSecondRoleSatisfied);

        return new int[]{i, j, k};
    }
}
