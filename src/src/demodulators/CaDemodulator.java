package src.demodulators;

import src.generators.CaGenerator;
import src.generators.DataGenerator;
import src.generators.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 01.10.2017.
 */
public class CaDemodulator implements Demodulator<List<Byte>, List<Byte>> {
    private CaGenerator caGenerator;
    private List<Byte> ca;// Ca code

    public CaDemodulator(CaGenerator caGenerator) {
        this.caGenerator = caGenerator;
    }

    @Override
    public List<Byte> demodulate(List<Byte> data) {
        initCaCodes();
        List<Byte> outData = new ArrayList<Byte>();

        for (int i = 0; i < data.size(); i += DataGenerator.DATA_STEP) {
            boolean isReverse = DataUtils.isReverse(data.get(i), ca);
            byte dataBit = (byte) (isReverse ? 0 : 1);
            outData.add(dataBit);
        }

        return outData;
    }

    private void initCaCodes() {
        ca = new ArrayList<Byte>();
        for (int i = 0; i < DataGenerator.DATA_STEP; i++) {
            byte caBit = caGenerator.generate();
            ca.add(caBit);
        }
    }
}
