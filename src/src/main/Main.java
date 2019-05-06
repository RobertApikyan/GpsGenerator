package src.main;

import src.generators.CaGenerator;
import src.polynomial.PolynomialOne;
import src.polynomial.PolynomialTwo;
import src.satellites.SatellitesCaFactory;

import static test.PolynomialTest.GOLD_NUMBERS;

/**
 * Created by Robert on 30.09.2017.
 */
public class Main {
    public static void main(String[] args) {
        PolynomialOne polynomialOne = new PolynomialOne();
        PolynomialTwo polynomialTwo = new PolynomialTwo(SatellitesCaFactory.
                SAT_3);
        CaGenerator caGenerator = new CaGenerator(polynomialOne, polynomialTwo);

        StringBuilder ca = new StringBuilder();

        for (int i = 0; i < 1024; i++) {
            byte b = caGenerator.generate();
            if (b == 0) {
                ca.append("-1.0");
            } else {
                ca.append("1.0");
            }

            if (i != 1023) {
                ca.append(", ");
            }
        }

        System.out.println(ca.toString());
    }
}
