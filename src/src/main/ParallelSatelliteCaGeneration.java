package src.main;

import src.generators.CaGenerator;
import src.generators.DataUtils;
import src.polynomial.GenericPolynomial;
import src.polynomial.PolynomialOne;
import src.polynomial.PolynomialState;
import src.polynomial.PolynomialTwo;
import src.polynomial.collectors.SatelliteCollector;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static src.generators.DataUtils.printSatelliteCaSequences;
import static src.satellites.SatellitesCaFactory.*;

public class ParallelSatelliteCaGeneration {

    public static void main(String[] args) {
        runParallel();
        runSequential();
    }

    private static void runSequential() {
        BufferedOutputStream bos = null;
        try {
            int[][] codes = sequentialGeneration();
             bos = new BufferedOutputStream(
                    new FileOutputStream("/Users/robert/Desktop/Project_docs/tez/sequential.txt"));
            printSatelliteCaSequences(bos,"Parallel Generation",codes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void runParallel() {
        BufferedOutputStream bos = null;
        try {
            int[][] codes = sequentialGeneration();
            bos = new BufferedOutputStream(
                    new FileOutputStream("/Users/robert/Desktop/Project_docs/tez/parallel.txt"));
            printSatelliteCaSequences(bos,"Parallel Generation",codes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int[][] parallelGeneration() {
        int[][] values = new int[SAT_COUNT][COMPLETE_CA];
        List<SatelliteCollector> collectors = createSatelliteCollectors();

        GenericPolynomial p1 = new GenericPolynomial(new int[]{3, 10}, 10);
        GenericPolynomial p2 = new GenericPolynomial(new int[]{2, 3, 6, 8, 9, 10}, 10);

        for (int caIndex = 0; caIndex < COMPLETE_CA; caIndex++) {
            int p1OutPut = p1.process();
            PolynomialState p2State = p2.captureState();
            for (int collectorIndex = 0; collectorIndex < collectors.size(); collectorIndex++) {
                SatelliteCollector collector = collectors.get(collectorIndex);
                int p2OutPut = collector.apply(p2State);
                values[collectorIndex][caIndex] = DataUtils.exOr(p1OutPut,p2OutPut);
            }
            p2.process();
        }

        return values;
    }

    private static int[][] sequentialGeneration(){
        int[][] values = new int[SAT_COUNT][COMPLETE_CA];

        List<int[]> goldNumbers = getGoldNumbersintArrayList();

        for (int satelliteIndex = 0; satelliteIndex < SAT_COUNT; satelliteIndex++) {
            CaGenerator generator = new CaGenerator(new PolynomialOne(),new PolynomialTwo(goldNumbers.get(satelliteIndex)));
            for (int caIndex = 0; caIndex < COMPLETE_CA; caIndex++) {
                int chip = generator.generate();
                values[satelliteIndex][caIndex] = chip;
            }
        }

        return values;
    }
}
