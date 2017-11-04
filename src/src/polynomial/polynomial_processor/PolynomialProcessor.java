package src.polynomial.polynomial_processor;

/**
 * Created by Robert on 30.09.2017.
 */
public abstract class PolynomialProcessor {
    public static final int COMPLETE_CA = 1023;
    private byte[] exOrIndexes;
    private byte[] bytes = createBytes();
    private int processCount = 0;

    public PolynomialProcessor(byte... exOrIndexes) {
        this.exOrIndexes = exOrIndexes;
    }

    protected byte exOr() {
        return exOr(exOrIndexes);
    }

    public byte exOr(byte[] exOrIndexes) {
        byte sum = 0;

        for (byte exOrIndex : exOrIndexes) {
            sum += bytes[exOrIndex - 1];
        }

        return (byte) (sum % 2 == 0 ? 0 : 1);
    }

    /**
     * @param startValue, the value from exOr
     * @return output value
     */
    protected byte shift(byte startValue) {
        int lastItemIndex = bytes.length - 1;
        byte outPut = bytes[lastItemIndex];
        System.arraycopy(bytes, 0, bytes, 1, lastItemIndex);
        bytes[0] = startValue;
        return outPut;
    }

    public void resetRegister() {
        processCount = 0;
        bytes = createBytes();
    }

    private byte[] createBytes() {
        return new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    }

    public byte[] getCurrentBytes() {
        return bytes;
    }

    public byte process() {
        if (processCount == COMPLETE_CA) {
            resetRegister();
        }
        processCount++;
        return processNext();
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
    public abstract byte processNext();
}
