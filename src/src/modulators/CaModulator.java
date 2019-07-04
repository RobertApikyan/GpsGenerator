package src.modulators;

import src.generators.CaGenerator;
import src.generators.DataGenerator;
import src.generators.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 01.10.2017.
 */
public class CaModulator implements Modulator<List<Integer>> {
    private CaGenerator caGenerator;
    private DataGenerator dataGenerator;

    public CaModulator(CaGenerator caGenerator, DataGenerator dataGenerator) {
        this.caGenerator = caGenerator;
        this.dataGenerator = dataGenerator;
    }

    @Override
    public List<Integer> modulate() {
        List<Integer> binaryList = dataGenerator.generate();

        List<Integer> modulatedData = new ArrayList<Integer>();

        for (Integer binary : binaryList) {
            for (int j = 0; j < DataGenerator.DATA_STEP; j++) {
                int ca = caGenerator.generate();
                modulatedData.add(DataUtils.and(binary, ca));
            }
        }

        return modulatedData;
    }
}
