package src.polynomial;

import src.polynomial.polynomial_processor.PolynomialProcessor;

/**
 * Created by Robert on 30.09.2017.
 */
public class PolynomialOne extends PolynomialProcessor {
    // 1+x3+x10 algorithm
    private static final int[] EX_OR_INDEXES = new int[]{3,10};

    public PolynomialOne() {
        super(EX_OR_INDEXES,10);
    }

    @Override
    public int processNext() {
        return shift(exOrFibonacci());
    }

    @Override
    protected int processPrev() {
        return 0;
    }
}
