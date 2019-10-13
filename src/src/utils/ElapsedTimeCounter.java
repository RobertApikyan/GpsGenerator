package src.utils;

public class ElapsedTimeCounter {

    private long startTime = -1;

    public void start(){
        startTime = System.nanoTime();
    }

    public long stop(){
        return System.nanoTime() - startTime;
    }
}
