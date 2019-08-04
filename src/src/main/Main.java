package src.main;

import src.polynomial.GenericPolynomial;
import src.polynomial.PolynomialOne;
import src.polynomial.PolynomialState;
import src.polynomial.polynomial_processor.PolynomialProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by Robert on 30.09.2017.
 */
public class Main {

    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(4,
            10,
            1000L,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(10,false) {
            });

    private static volatile boolean isFinished = false;

    public static final int COMPLETE_CA = 1024;

    public static void main(String[] args) {
//        comparePolynomials();

        int[] output = new int[9000];
        PolynomialState initialState = new PolynomialState(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        generateRange(output, initialState, 5, 6, 0);

        while (!isFinished){

        }

        System.out.println(Arrays.toString(output));


        int[] output2 = new int[9000];
        GenericPolynomial polynomial = new GenericPolynomial(new int[]{5, 6}, 10);
        for (int i = 0; i < 9000; i++) {
            output2[i] = polynomial.process();
        }

        System.out.println(Arrays.toString(output2));

    }

    private static void comparePolynomials() {

        PolynomialProcessor polynomial = new GenericPolynomial(new int[]{3, 9}, 10);

        List<PolynomialState> states = new ArrayList<PolynomialState>();
        states.add(polynomial.captureState());
        for (int i = 0; i < COMPLETE_CA; i++) {
            polynomial.process();

            states.add(polynomial.captureState());
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new PrintWriter(new File("/Users/robert/Desktop/Project_Docs/tez/polynomialCompared.txt")));

            for (int i = 0; i < COMPLETE_CA - 1; i++) {

                int nextIndex = i + 6;
                PolynomialState s1 = states.get(i);
                PolynomialState s2 = states.get(nextIndex);

                PolynomialState s3 = s1.exOr(s2);

                int similarStep = states.indexOf(s3);

                String output =
                        "step= " + i + " | " +
                                s1.toString() + " " +
                                "s" + i + " + s" + nextIndex + " =" + " " +
                                "s" + similarStep + " ( " + s3.toString() + " ) " + "\n";

                bw.write(output);
                System.out.println(output);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writePolynomial() {
        PolynomialOne polynomialOne = new PolynomialOne();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new PrintWriter(new File("/Users/robert/Desktop/Project_docs/tez/polinomialOne.txt")));
            for (int i = 0; i < COMPLETE_CA; i++) {
                String output = polynomialOne.toString() + "\n";
                bw.write(output);
                polynomialOne.process();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void generateRange(final int[] output, PolynomialState initialState,
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
                isFinished = true;
                break;
            }

            int out = polynomial.process();
            output[index] = out;
        }
    }

    private static int[] defineValidIndexes(int f1, int f2, int n, int step) {
        int i = step - 1;
        int j;
        int k;

        boolean firstRole;
        boolean secondRole;
        do {
            i++;
            j = i + f2 - f1;
            k = i + f2;

            firstRole = i - n + f2 >= 0;
            secondRole = i - n + 2 * f2 - f1 >= 0;

        } while (
                !firstRole
                        ||
                        !secondRole);

        return new int[]{i, j, k};
    }
}
