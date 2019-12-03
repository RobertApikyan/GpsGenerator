package src.polynomial;

import src.polynomial.polynomial_processor.PolynomialProcessor;

import java.util.Arrays;

public class GaloisLfsr extends PolynomialProcessor {

    private int[] outputRegisters;

    public GaloisLfsr(int[] exOrIndexes,
                      int registersCount){
        this(exOrIndexes,registersCount,new int[]{registersCount});
    }

    public GaloisLfsr(int[] exOrIndexes,
                      int registersCount,
                      int[] outputRegisters) {
        super(exOrIndexes,registersCount);
        this.outputRegisters = outputRegisters;
    }

    @Override
    protected int processNext() {
        final int output = exOrGalois();
        shift(output);
        return output;
    }

    @Override
    protected int processPrev() {
        return 0;
    }

    public void setState(PolynomialState state){
        int[] values = state.getValues();
        setRegisterValues(Arrays.copyOf(values,values.length));
    }
}
