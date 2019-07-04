package src.generators;

import src.polynomial.PolynomialState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Robert on 01.10.2017.
 */
public class DataUtils {

    public static String binaryToString(List<Integer> binaryData) {
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

    public static List<Integer> stringToBinary(String text) {
        if (text == null) return new ArrayList<Integer>();

        List<Integer> binaryData = new ArrayList<Integer>();

        byte[] bytes = text.getBytes();

        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binaryData.add((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binaryData;
    }

    public static int exOr(int first, int second) {
        return  ((first + second) % 2 == 0 ? 0 : 1);
    }

    public static int exOrValue(int value){
        return value % 2 == 0 ? 0 : 1;
    }

    public static byte exOr(int[] stateValues,int[] exOrIndexes) {
        byte sum = 0;

        for (int exOrIndex : exOrIndexes) {
            sum += stateValues[exOrIndex - 1];
        }

        return (byte) (sum % 2 == 0 ? 0 : 1);
    }

    public static int and(int first, int second) {
        return first == 1 ? second : reverseBite(second);
    }

    public static int reverseBite(int bit) {
        return (bit == 1 ? 0 : 1);
    }

    public static boolean isReverse(int firstBite, List<Integer> caCode) {
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

    public static void printSatelliteCaSequences(OutputStream os,String prefix,int[][] satelliteCaSequences){
        BufferedWriter writer = new BufferedWriter(new PrintWriter(os));
        try {
            writer.write(prefix+"\n");
            for (int satIndex = 0; satIndex < satelliteCaSequences.length; satIndex++) {
                writer.write("Satellite "+(satIndex+1)+"\n");
                int[] caSequence = satelliteCaSequences[satIndex];
                for (int caIndex = 0; caIndex < caSequence.length; caIndex++) {
                    int chip = caSequence[caIndex];

                    writer.write(chip + " ");

                    if ((caIndex+1)%200 == 0){
                        // next line
                        writer.write("\n");
                    }
                }
                writer.write("\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
