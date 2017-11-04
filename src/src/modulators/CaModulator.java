package src.modulators;

import src.generators.CaGenerator;
import src.generators.DataGenerator;
import src.generators.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 01.10.2017.
 */
public class CaModulator implements Modulator<List<Byte>> {
    private CaGenerator caGenerator;
    private DataGenerator dataGenerator;

    public CaModulator(CaGenerator caGenerator, DataGenerator dataGenerator) {
        this.caGenerator = caGenerator;
        this.dataGenerator = dataGenerator;
    }

    @Override
    public List<Byte> modulate() {
        List<Byte> binaryList = dataGenerator.generate();

        List<Byte> modulatedData = new ArrayList<Byte>();

        for (Byte aBinaryList : binaryList) {
            byte bit = aBinaryList;

            for (int j = 0; j < DataGenerator.DATA_STEP; j++) {
                byte ca = caGenerator.generate();
                modulatedData.add(DataUtils.and(bit, ca));
            }
        }

        return modulatedData;
    }
}
