package src.generators;

import java.util.List;

/**
 * Created by Robert on 30.09.2017.
 */
public class DataGenerator implements Generator<List<Integer>> {
    public static final int DATA_STEP = 20460;

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public List<Integer> generate() {
        return DataUtils.stringToBinary(text);
    }
}
