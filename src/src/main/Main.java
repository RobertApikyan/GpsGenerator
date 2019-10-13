package src.main;

import src.polynomial.LFSR;
import src.polynomial.PolynomialOne;
import src.polynomial.PolynomialState;
import src.polynomial.polynomial_processor.PolynomialProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Robert on 30.09.2017.
 */
public class Main {

    public static int COMPLETE_CA;

    public static void main(String[] args) {
        comparePolynomials();
    }

    private static void printSequence() {
        LFSR pol = new LFSR(new int[]{2, 4}, 4);
        for (int i = 0; i < 32; i++) {
            System.out.print(pol.process() + " ");
        }
    }

    private static void comparePolynomials() {

        PolynomialProcessor polynomial = new LFSR(new int[]{ 2, 3, 4, 6, 7,10 }, 10);

        List<PolynomialState> states = new ArrayList<PolynomialState>();
        states.add(polynomial.captureState());

        COMPLETE_CA = (int) Math.pow(2,polynomial.getRegisterSize())-1;

        for (int i = 0; i < COMPLETE_CA; i++) {
            polynomial.process();
            states.add(polynomial.captureState());
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new PrintWriter(new File("/Users/robert/Desktop/Project_Docs/tez/polynomialCompared.txt")));

            for (int i = 0; i < COMPLETE_CA - 1; i++) {
                System.out.print("step= " + i + "\t |"+ states.get(i).toString()+" ");

                PolynomialState sum = states.get(i);
                int fl = polynomial.getExOrIndexes()[polynomial.getExOrIndexes().length  -1];

                System.out.print("s" + i);

                for (int j = polynomial.getExOrIndexes().length-2; j >= 0 ; j--) {
                    int exOrIndex = polynomial.getExOrIndexes()[j];
                    int f = i + fl - exOrIndex;
                    if (f >= COMPLETE_CA){
                        break;
                    }
                    sum = sum.exOr(states.get(f));
                    System.out.print(" + s"+f);
                }

                int similarStep = states.indexOf(sum);

                System.out.print(" = s"+similarStep + " ( " + sum.toString() + " ) " + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  finally {
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
}
