package src.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Robert on 01.10.2017.
 */
public class DataUtils {

    public static String binaryToString(List<Byte> binaryData) {
        if (binaryData == null || binaryData.size() == 0) return "";

        StringBuilder stringData = new StringBuilder();

        for (int i = 0; i < binaryData.size(); i += 8) {
            StringBuilder stringByte = new StringBuilder();
            for (int j = i; j < i + 8; j++) {
                stringByte.append(binaryData.get(j));
            }

            stringData.append((char) Integer.parseInt(stringByte.toString(), 2));
        }

        return stringData.toString();
    }

    public static List<Byte> stringToBinary(String text) {
        if (text == null) return new ArrayList<Byte>();

        List<Byte> binaryData = new ArrayList<Byte>();

        byte[] bytes = text.getBytes();

        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binaryData.add((byte) ((val & 128) == 0 ? 0 : 1));
                val <<= 1;
            }
        }
        return binaryData;
    }

    public static byte exOr(byte first, byte second) {
        return (byte) ((first + second) % 2 == 0 ? 0 : 1);
    }

    public static byte and(byte first, byte second) {
        return first == 1 ? second : reverseBite(second);
    }

    public static byte reverseBite(byte bit) {
        return (byte) (bit == 1 ? 0 : 1);
    }

    public static boolean isReverse(byte firstBite, List<Byte> caCode) {
        return caCode.get(0) != firstBite;
    }

    public static int stringCount(String string, String match) {
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(string);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }

    public static int[] listMaxAndIndexValues(List<Integer> integers) {
        if (integers == null || integers.size() == 0) return new int[]{-1, -1};
        int max = integers.get(0);
        int index = 0;
        for (int i = 1; i < integers.size(); i++) {
            if (max < integers.get(i)) {
                max = integers.get(i);
                index = i;
            }
        }
        return new int[]{max, index};
    }
}
