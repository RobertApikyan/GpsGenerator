package src.polynomial;

import src.polynomial.polynomial_processor.PolynomialProcessor;

public class GenericPolynomial extends PolynomialProcessor {

    public GenericPolynomial(int[] exOrIndexes,int registersCount) {
        super(exOrIndexes,registersCount);
    }

    @Override
    protected int processNext() {
        return shift(exOr());
    }
}
