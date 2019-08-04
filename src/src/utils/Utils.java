package src.utils;

public class Utils {

    private Utils(){}

    // returns elapsed nano time
    public static long timeWatch(Runnable block){
        long startTime = System.nanoTime();
        block.run();
        return System.nanoTime() - startTime;
    }
}
