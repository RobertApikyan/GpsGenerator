package test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import src.demodulators.CaDemodulator;
import src.detectors.GoldNumberDetector;
import src.generators.CaGenerator;
import src.generators.DataGenerator;
import src.generators.DataUtils;
import src.modulators.CaModulator;
import src.polynomial.PolynomialOne;
import src.polynomial.PolynomialTwo;
import src.satellites.SatellitesCaFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Robert on 30.09.2017.
 */

public class PolynomialTest {

    public static final byte[] EXPECTED_BYTE_RESULT_FOR_POLYNOMIAL_ONE = new byte[]{1, 0, 0, 0, 1, 1, 1, 0, 0, 0};
    public static final byte[] EXPECTED_BYTE_RESULT_FOR_POLYNOMIAL_ONE_OUTPUT = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public static final byte[] EXPECTED_BYTE_RESULT_FOR_POLYNOMIAL_TWO = new byte[]{0, 0, 1, 0, 1, 1, 0, 1, 0, 0};
    public static final byte[] EXPECTED_BYTE_RESULT_FOR_POLYNOMIAL_TWO_OUTPUT = new byte[]{0, 0, 1, 1, 0, 1, 1, 1, 1, 1}; // for #GOLD_NUMBERS = new byte[]{2, 6}

    public static final List<Integer> EXPECTED_HELLO_WORLD_TO_BYTE_LIST = Arrays.asList(0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1,
            0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0
            , 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1,
            1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0
            , 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0,
            0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0);

    public static final byte[] EXCEPTED_CA_OUT = new byte[]{1, 1, 0, 0, 1, 0, 0, 0, 0, 0};

    public static final int[] GOLD_NUMBERS = SatellitesCaFactory.SAT_19;
    public static final String TEXT = "Robert Hello World And Good Night";

    PolynomialOne polynomialOne;
    PolynomialTwo polynomialTwo;
    CaGenerator caGenerator;
    DataGenerator dataGenerator;
    CaModulator caModulator;
    CaDemodulator caDemodulator;
    GoldNumberDetector goldNumberDetector;

    @Rule
    public PrintTestNameRule nameRule = new PrintTestNameRule();

    @Before
    public void beforeTest() {
        polynomialOne = new PolynomialOne();
        polynomialTwo = new PolynomialTwo(GOLD_NUMBERS);
        caGenerator = new CaGenerator(polynomialOne, polynomialTwo);
        dataGenerator = new DataGenerator();
        dataGenerator.setText(TEXT);
        caModulator = new CaModulator(caGenerator, dataGenerator);
        caDemodulator = new CaDemodulator(caGenerator);
        goldNumberDetector = new GoldNumberDetector(new CaGenerator(polynomialOne));
    }
//
//    @Test
//    public void test_ca_generator_out() {
//        for (int i = 0; i < DataGenerator.DATA_STEP; i++) {
//            System.out.println(caGenerator.generate());
//        }
//    }

    //    @Test
//    public void test_String_to_Binary() throws UnsupportedEncodingException {
//        List<Integer> binary = DataUtils.stringToBinary("Hello World");
//        System.out.println(DataUtils.binaryToString(binary));
//    }
//

    @Test
    public void test_CaModulator() {
        System.out.println("text= " + TEXT);
        List<Integer> modulatedData = caModulator.modulate();

        int[] goldNumber = goldNumberDetector.detect(modulatedData);

        System.out.println("goldNumber= " + Arrays.toString(goldNumber));

        CaGenerator demodulatorCaGenerator = new CaGenerator(polynomialOne, new PolynomialTwo(goldNumber));

        caDemodulator = new CaDemodulator(demodulatorCaGenerator);

        List<Integer> demodulatedData = caDemodulator.demodulate(modulatedData);

        System.out.println("result= " + DataUtils.binaryToString(demodulatedData));
    }
}
