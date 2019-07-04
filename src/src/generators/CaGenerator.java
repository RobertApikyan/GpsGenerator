package src.generators;

import src.polynomial.PolynomialOne;
import src.polynomial.PolynomialTwo;

/**
 * Created by Robert on 30.09.2017.
 */
public class CaGenerator implements Generator<Integer> {
    private PolynomialOne polynomialOne;
    private PolynomialTwo polynomialTwo;

    public CaGenerator(PolynomialOne polynomialOne) {
        this(polynomialOne,null);
    }

    public CaGenerator(PolynomialOne polynomialOne, PolynomialTwo polynomialTwo) {
        this.polynomialOne = polynomialOne;
        this.polynomialTwo = polynomialTwo;
    }

    @Override
    public Integer generate() {
        return DataUtils.exOr(polynomialOne.process(), polynomialTwo.process());
    }

    public void setPolynomialTwo(PolynomialTwo polynomialTwo) {
        this.polynomialTwo = polynomialTwo;
    }
}
