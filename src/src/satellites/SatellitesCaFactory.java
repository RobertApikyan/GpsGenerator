package src.satellites;

import src.polynomial.PolynomialTwo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Robert on 01.10.2017.
 */
public class SatellitesCaFactory {

    public static final byte[] SAT_1 = new byte[]{2, 6};
    public static final byte[] SAT_2 = new byte[]{3, 7};
    public static final byte[] SAT_3 = new byte[]{4, 8};
    public static final byte[] SAT_4 = new byte[]{5, 9};
    public static final byte[] SAT_5 = new byte[]{1, 9};
    public static final byte[] SAT_6 = new byte[]{2, 10};
    public static final byte[] SAT_7 = new byte[]{1, 8};
    public static final byte[] SAT_8 = new byte[]{2, 9};
    public static final byte[] SAT_9 = new byte[]{3, 10};
    public static final byte[] SAT_10 = new byte[]{2, 3};
    public static final byte[] SAT_11 = new byte[]{3, 4};
    public static final byte[] SAT_12 = new byte[]{5, 6};
    public static final byte[] SAT_13 = new byte[]{6, 7};
    public static final byte[] SAT_14 = new byte[]{7, 8};
    public static final byte[] SAT_15 = new byte[]{8, 9};
    public static final byte[] SAT_16 = new byte[]{9, 10};
    public static final byte[] SAT_17 = new byte[]{1, 4};
    public static final byte[] SAT_18 = new byte[]{2, 5};
    public static final byte[] SAT_19 = new byte[]{3, 6};
    public static final byte[] SAT_20 = new byte[]{4, 7};
    public static final byte[] SAT_21 = new byte[]{5, 8};
    public static final byte[] SAT_22 = new byte[]{6, 9};
    public static final byte[] SAT_23 = new byte[]{1, 3};
    public static final byte[] SAT_24 = new byte[]{4, 6};
    public static final byte[] SAT_25 = new byte[]{5, 7};
    public static final byte[] SAT_26 = new byte[]{6, 8};
    public static final byte[] SAT_27 = new byte[]{7, 9};
    public static final byte[] SAT_28 = new byte[]{8, 10};
    public static final byte[] SAT_29 = new byte[]{1, 6};
    public static final byte[] SAT_30 = new byte[]{2, 7};
    public static final byte[] SAT_31 = new byte[]{3, 8};
    public static final byte[] SAT_32 = new byte[]{4, 9};

    private static final List<byte[]> goldNumbersByteArrayList = Arrays.asList(SAT_1, SAT_2, SAT_3, SAT_4, SAT_5, SAT_6, SAT_7, SAT_8, SAT_9, SAT_10, SAT_11, SAT_12, SAT_13, SAT_14, SAT_15, SAT_16, SAT_17, SAT_18, SAT_19, SAT_20,
            SAT_21, SAT_22, SAT_23, SAT_24, SAT_25, SAT_26, SAT_27, SAT_28, SAT_29, SAT_30, SAT_31, SAT_32);

    public static PolynomialTwo createFor(byte[] goldNumbers) {
        return new PolynomialTwo(goldNumbers);
    }

    public static List<byte[]> getGoldNumbersByteArrayList(){
        return goldNumbersByteArrayList;
    }
}
