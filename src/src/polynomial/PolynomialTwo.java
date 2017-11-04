package src.polynomial;

import src.polynomial.polynomial_processor.PolynomialProcessor;

/**
 * Created by Robert on 30.09.2017.
 */
public class PolynomialTwo extends PolynomialProcessor {
    // 1+x2+x3+x6+x8+x9+x10 algorithm
    private static final byte[] EX_OR_INDEXES = new byte[]{2, 3, 6, 8, 9, 10};
    private byte[] goldNumbers;

    public PolynomialTwo(byte[] goldNumbers) {
        super(EX_OR_INDEXES);
        this.goldNumbers = goldNumbers;
    }

    @Override
    public byte processNext() {
        byte outPut = exOr(goldNumbers);
        shift(exOr());
        return outPut;
    }

    public void setGoldNumbers(byte[] goldNumbers) {
        resetRegister();
        this.goldNumbers = goldNumbers;
    }
}
