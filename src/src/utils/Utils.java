package src.utils;

public class Utils {

    private Utils(){}

    // returns elapsed nano time
    public static long timeWatch(Runnable block){
        ElapsedTimeCounter counter = new ElapsedTimeCounter();
        counter.start();
        block.run();
        return counter.stop();
    }


    public static void timeWatchPrint(String label,Runnable block){
        System.out.println(label+" elapsedTime= "+timeWatch(block));
    }

}
