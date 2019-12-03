package src.polynomial;

import src.polynomial.polynomial_processor.PolynomialProcessor;

import java.util.Arrays;

public class FibonacciLfsr extends PolynomialProcessor {

    private int[] outputRegisters;

    public FibonacciLfsr(int[] exOrIndexes,
                         int registersCount){
        this(exOrIndexes,registersCount,new int[]{registersCount});
    }

    public FibonacciLfsr(int[] exOrIndexes,
                         int registersCount,
                         int[] outputRegisters) {
        super(exOrIndexes,registersCount);
        this.outputRegisters = outputRegisters;
    }

    @Override
    protected int processNext() {
        final int output = exOrFibonacci(outputRegisters);
        shift(exOrFibonacci());
        return output;
    }

    @Override
    protected int processPrev() {
        return 0;
    }
}
