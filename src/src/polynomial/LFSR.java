package src.polynomial;

import src.polynomial.polynomial_processor.PolynomialProcessor;

import java.util.Arrays;

public class LFSR extends PolynomialProcessor {

    public LFSR(int[] exOrIndexes, int registersCount) {
        super(exOrIndexes,registersCount);
    }

    @Override
    protected int processNext() {
        return shift(exOr());
    }

    public void setState(PolynomialState state){
        int[] values = state.getValues();
        setRegisterValues(Arrays.copyOf(values,values.length));
    }
}
