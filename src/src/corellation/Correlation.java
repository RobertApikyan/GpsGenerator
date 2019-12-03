package src.corellation;

import java.util.Arrays;

public class Correlation {

    public static double coefficient(double[] first, double[] second) {
        double firstSum = 0.0;
        double secondSum = 0.0;

        for (int i = 0; i < Math.max(first.length, second.length); i++) {
            if (i < first.length) {
                firstSum += Math.pow(first[i], 2.0);
            }
            if (i < second.length) {
                secondSum += Math.pow(second[i], 2.0);
            }
        }

        if (firstSum == 0) {
            firstSum = 1;
        }

        if (secondSum == 0) {
            secondSum = 1;
        }

        return Math.sqrt(firstSum * secondSum);
    }

    public static double[] crossValues(
            double[] sample,
            double[] source,
            double emptyValue,
            double corrCoefficient
    ) {
        double[] equalizedSample;

        if (sample.length > source.length) {
            throw new IllegalArgumentException("sample.length > source.length , sample length might be " +
                    "smaller or equal to source length");
        } else if (sample.length < source.length) {
            equalizedSample = new double[source.length];
            // equalize arrays

            for (int i = 0; i < equalizedSample.length; i++) {
                equalizedSample[i] = emptyValue;
                if (sample.length > i) {
                    equalizedSample[i] = sample[i];
                }
            }
        } else { // if sample.length == source.length
            equalizedSample = sample;
        }

        double[] corrValues = new double[sample.length + 2 * source.length];

        int corrIndex = 0;
        for (int i = 1 - sample.length; i < sample.length + source.length + 1; i++) {
            double[] shiftedSample = arrayShiftLinear(equalizedSample, i, true);

            corrValues[corrIndex] =
                    value(0, Math.max(shiftedSample.length, source.length) - 1, shiftedSample, source) / corrCoefficient;

            corrIndex++;
        }

        return corrValues;
    }

    public static double value(int start, int end, double[] first, double[] second) {
        int i = start;
        int j = Math.max(first.length, second.length) - (end - start) - 1;

        double sum = 0.0;

        for (int d = 0; d <= end - start; d++) {
            double firstValue = 0.0;

            if (first.length > i) {
                firstValue = first[i];
            }

            double secondValue = 0.0;

            if (second.length > j) {
                secondValue = second[j];
            }

            sum += firstValue * secondValue;

            i++;
            j++;
        }

        return sum;
    }

    public static double[] correlation(
            int start,
            int end,
            double[] first,
            double[] second,
            double[] result,
            double coefficient
    ) {
        int step = start + end + 1;
        int length = Math.max(first.length, second.length);

        int de = end;
        int ds = start;

        if ((length - 1 - step) >= 0) {
            de++;
        } else {
            if ((length - step) <= 0) {
                ds++;
            }
        }

        if (step == 2 * length) {
            return result;
        } else {
            double sum = value(start, end, first, second);
            result[step - 1] = sum / coefficient;

            return correlation(ds, de, first, second, result, coefficient);
        }
    }

    private static double[] arrayShift(
            double[] array,
            int shift,
            boolean circular,
            boolean copyArray,
            double emptyValue
    ) {
        if (array.length <= 1) {
            return array;
        }

        boolean toRight = shift > 0;

        double[] shiftedArray = array;

        if (copyArray) {
            shiftedArray = Arrays.copyOf(array, array.length);
        }

        for (int step = 0; step < Math.abs(shift); step++) {

            double tail = emptyValue;

            if (circular) {
                tail = toRight ? shiftedArray[array.length - 1] : shiftedArray[0];
            }

            if (toRight) {
                for (int i = array.length - 1; i >= 0; i--) {
                    if (i - 1 >= 0) {
                        shiftedArray[i] = shiftedArray[i - 1];
                    } else {
                        shiftedArray[i] = tail;
                    }
                }
            } else {
                for (int i = 0; i < array.length; i++) {
                    if (i + 1 < array.length) {
                        shiftedArray[i] = shiftedArray[i + 1];
                    } else {
                        shiftedArray[i] = tail;
                    }
                }
            }
        }

        return shiftedArray;
    }

    public static double[] arrayShiftCircular(double[] array, int shift, boolean copyArray) {
        return arrayShift(array, shift, true, copyArray, 0.0);
    }

    public static double[] arrayShiftLinear(double[] array, int shift, boolean copyArray) {
        return arrayShift(array, shift, false, copyArray, 0.0);
    }
}
