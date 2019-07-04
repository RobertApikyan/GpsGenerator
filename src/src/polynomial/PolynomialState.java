package src.polynomial;

import src.generators.DataUtils;

import java.util.Arrays;

public class PolynomialState {

    private int[] values;

    public PolynomialState(int length){
        this.values = new int[length];
    }

    public PolynomialState(int[] bytes){
        this.values = Arrays.copyOf(bytes,bytes.length);
    }

    public void setByte(int index,int value){
        values[index] = value;
    }

    public int getByte(int index) {
        return values[index];
    }

    public PolynomialState exOr(PolynomialState polynomialState){

        PolynomialState result = new PolynomialState(values.length);

        for (int i = 0; i < values.length; i++) {
            int resultByte = DataUtils.exOr(getByte(i),polynomialState.getByte(i));
            result.setByte(i,resultByte);
        }

        return result;
    }

    public int[] getValues(){
        return values;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolynomialState that = (PolynomialState) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public String toString() {
        return binary(" ");
    }

    public String binary(String separator){
        StringBuilder builder = new StringBuilder();
        for (int b : values) {
            builder.append(b).append(separator);
        }
        return builder.toString();
    }
}
