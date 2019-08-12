package src.main;

import src.polynomial.GenericPolynomial;
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

    public static final int COMPLETE_CA = 1024;

    public static void main(String[] args) {
        comparePolynomials();
//        printSequence();
    }

    private static void printSequence(){
        GenericPolynomial pol = new GenericPolynomial(new int[]{2,4},4);
        for (int i = 0; i < 32; i++) {
            System.out.print(pol.process()+" ");
        }
    }

    private static void comparePolynomials() {

        PolynomialProcessor polynomial = new GenericPolynomial(new int[]{1, 2}, 4);

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

                int nextIndex = i + 1;
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
}
