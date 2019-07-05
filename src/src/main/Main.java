package src.main;

import src.generators.CaGenerator;
import src.generators.DataUtils;
import src.polynomial.GenericPolynomial;
import src.polynomial.PolynomialOne;
import src.polynomial.PolynomialState;
import src.polynomial.PolynomialTwo;
import src.polynomial.phaseSelector.PhaseSelector;
import src.polynomial.phaseSelector.SatellitePhaseSelector;
import src.polynomial.polynomial_processor.PolynomialProcessor;
import src.satellites.SatellitesCaFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Robert on 30.09.2017.
 */
public class Main {

    public static final int COMPLETE_CA = 1024;

    public static void main(String[] args) {
        shiftCollector();
        System.out.println();
        simpleGenerator();
    }

    public static void simpleGenerator(){
        CaGenerator caGenerator = new CaGenerator(new PolynomialOne(),new PolynomialTwo(SatellitesCaFactory.SAT_1));
        for (int i = 0; i < COMPLETE_CA; i++) {
            System.out.print(caGenerator.generate() + " ");
        }
    }

    private static void shiftCollector(){

        PolynomialProcessor p1 = new GenericPolynomial(new int[]{3,10},10);
        PolynomialProcessor p2 = new GenericPolynomial(new int[]{2,3,6,8,9,10},10);

        PhaseSelector sate1 = new SatellitePhaseSelector(new int[]{2,6});

        for (int i = 0; i < COMPLETE_CA; i++) {
            PolynomialState ps2 = p2.captureState();

            int value = DataUtils.exOr(sate1.apply(ps2),p1.process());

            System.out.print(value+" ");

            p2.process();
        }
    }

    private static void comparePolynomials() {

        PolynomialProcessor polynomial = new GenericPolynomial(new int[]{2,3,6,8,9,10},10);

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
                PolynomialState s1 = states.get(i);
                PolynomialState s2 = states.get(i + 2);

                PolynomialState s3 = s1.exOr(s2);

                int similarStep = states.indexOf(s3);

                String output =
                        "step= " + i + " | " +
                        s1.toString() + " " +
                        "binary"+ Integer.parseInt(s1.binary(""),2)  + " " +
                        "similar= " + similarStep + " | " + s3.toString() + "\n"
                        ;

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
