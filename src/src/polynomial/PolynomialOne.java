package src.polynomial;

import src.polynomial.polynomial_processor.PolynomialProcessor;

/**
 * Created by Robert on 30.09.2017.
 */
public class PolynomialOne extends PolynomialProcessor {
    // 1+x3+x10 algorithm
    private static final byte[] EX_OR_INDEXES = new byte[]{3,10};

    public PolynomialOne() {
        super(EX_OR_INDEXES);
    }

    @Override
    public byte processNext() {
        return shift(exOr());
    }
}
