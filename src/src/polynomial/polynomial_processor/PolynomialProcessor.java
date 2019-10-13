package src.polynomial.polynomial_processor;

import src.generators.DataUtils;
import src.polynomial.PolynomialState;

/**
 * Created by Robert on 30.09.2017.
 */
public abstract class PolynomialProcessor {
    private int[] exOrIndexes;
    private int[] register;
    private int processCount = 0;
    private int registerSize;

    public PolynomialProcessor(int[] exOrIndexes, int registerSize) {
        this.exOrIndexes = exOrIndexes;
        this.registerSize = registerSize;
        register = createBytes();
    }

    protected byte exOr() {
        return exOr(exOrIndexes);
    }

    protected byte exOr(int[] exOrIndexes) {
       return DataUtils.exOr(register,exOrIndexes);
    }

    /**
     * @param startValue, the value from exOr
     * @return output value
     */
    protected int shift(int startValue) {
        int lastItemIndex = register.length - 1;
        int outPut = register[lastItemIndex];
        System.arraycopy(register, 0, register, 1, lastItemIndex);
        register[0] = startValue;
        return outPut;
    }

    public void resetRegister() {
        processCount = 0;
        register = createBytes();
    }

    protected int[] createBytes() {
        int[] registers = new int[registerSize];
        for (int i = 0; i < registers.length; i++) {
            registers[i] = 1;
        }
        return registers;
    }

    public int[] getCurrentBytes() {
        return register;
    }

    public int process() {
        processCount++;
        return processNext();
    }

    public PolynomialState captureState() {
        return new PolynomialState(register);
    }

    /**
     * Here you might implement your
     * logic related with shift register
     * Example simple 1+x3+x10
     * byte process(){
     * return shift(exOr);
     * }
     *
     * @return outPut value
     */
    protected abstract int processNext();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("step= ").append(processCount).append(" | ");
        for (int b : register) {
            builder.append(b).append(" ");
        }
        return builder.toString();
    }

    public int getRegisterSize() {
        return registerSize;
    }

    public int[] getExOrIndexes(){
        return exOrIndexes;
    }

    protected void setRegisterValues(int[] registerValues){
        if (this.register.length != registerValues.length){
            throw new IllegalArgumentException();
        }
        this.register=registerValues;
    }
}
