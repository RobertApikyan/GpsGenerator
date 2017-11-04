package src.polynomial;

import java.util.LinkedList;

/**
 * Created by Robert on 30.09.2017.
 */
public class AsyncLopper extends Thread {
    private boolean stop;
    private final LinkedList<Runnable> tasks;

    public AsyncLopper() {
        tasks = new LinkedList<Runnable>();
    }

    @Override
    public void run() {
        while (!stop) {
            executeLast();
        }
    }

    private void executeLast() {
        if (tasks.size() > 0) {
            tasks.getLast().run();
            tasks.removeLast();
        }
    }

    public void post(Runnable runnable) {
        tasks.add(runnable);
    }

    public void quit() {
        stop = true;
    }

    @Override
    public synchronized void start() {
        stop = false;
        super.start();
    }

    @FunctionalInterface
    public interface Consumer<T>{
        void accept(T t);
    }
}
