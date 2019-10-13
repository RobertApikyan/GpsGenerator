package src.main;

import static src.utils.Utils.timeWatchPrint;

public class RecursionVsIteration {

    public static void main(String[] args) {
        final int iteration = 8000;
        final RecClass rec = new RecClass();
        timeWatchPrint("Recursion", () -> {
            rec.recMethod(iteration);
        });
        timeWatchPrint("Iteration", () -> {
            int iterationCount = iteration;
            while (iterationCount!=0){
                iterationCount--;
            }
        });
    }

    public static class RecClass{
        public void recMethod(int count){
            count--;
            if (count != 0){
                recMethod(count);
            }
        }
    }
}
