package src.detectors;

import src.generators.CaGenerator;
import src.generators.DataUtils;
import src.polynomial.PolynomialTwo;
import src.polynomial.polynomial_processor.PolynomialProcessor;
import src.satellites.SatellitesCaFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Robert on 02.10.2017.
 */
public class GoldNumberDetector implements Detector<byte[], List<Byte>> {
    private CaGenerator caGenerator;

    public GoldNumberDetector(CaGenerator caGenerator) {
        this.caGenerator = caGenerator;
    }

    @Override
    public byte[] detect(List<Byte> bytes) {

        List<byte[]> goldNumbers = SatellitesCaFactory.getGoldNumbersByteArrayList();
        List<Byte> ca = new ArrayList<Byte>();
        List<Byte> caReverse = new ArrayList<Byte>();
        List<Integer> matches = new ArrayList<Integer>();

        for (byte[] goldNumber : goldNumbers) {
            PolynomialTwo polynomialTwo = SatellitesCaFactory.createFor(goldNumber);

            caGenerator.setPolynomialTwo(polynomialTwo);

            for (int i = 0; i < PolynomialProcessor.COMPLETE_CA; i++) {
                Byte caCode = caGenerator.generate();
                ca.add(caCode);
                caReverse.add(DataUtils.reverseBite(caCode));
            }

            String bytesString = bytes.toString().replace("[", "").replace("]", "").replace(",","");
            String caString = ca.toString().replace("[", "").replace("]", "").replace(",","");
            String caReverseString = caReverse.toString().replace("[", "").replace("]", "").replace(",","");

            if (bytesString.contains(caString) || bytesString.contains(caReverseString)) {
                int caCount = DataUtils.stringCount(bytesString, caString);
                int caRevCount = DataUtils.stringCount(bytesString, caString);
                matches.add(caCount + caRevCount);
            } else {
                matches.add(0);
            }

            ca.clear();
        }

        int[] maxAndIndexValues = DataUtils.listMaxAndIndexValues(matches);
        System.out.println(matches.toString());
        System.out.println("Gold Number Detector: goldNumber= " + Arrays.toString(goldNumbers.get(maxAndIndexValues[1])) + ", " + "matches= " + maxAndIndexValues[0]);
        return goldNumbers.get(maxAndIndexValues[1]);
    }
}
