package src.polynomial;

import src.polynomial.polynomial_processor.PolynomialProcessor;

/**
 * Created by Robert on 30.09.2017.
 */
public class PolynomialTwo extends PolynomialProcessor {
    // 1+x2+x3+x6+x8+x9+x10 algorithm
    private static final int[] EX_OR_INDEXES = new int[]{2, 3, 6, 8, 9, 10};
    private int[] goldNumbers;

    public PolynomialTwo(int[] goldNumbers) {
        super(EX_OR_INDEXES,10);
        this.goldNumbers = goldNumbers;
    }

    @Override
    public int processNext() {
        byte outPut = exOrFibonacci(goldNumbers);
        shift(exOrFibonacci());
        return outPut;
    }

    @Override
    protected int processPrev() {
        return 0;
    }
}
