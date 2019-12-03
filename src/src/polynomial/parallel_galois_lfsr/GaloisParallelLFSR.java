package src.polynomial.parallel_galois_lfsr;

import com.sun.istack.internal.Nullable;
import src.polynomial.GaloisLfsr;
import src.polynomial.PolynomialState;
import src.polynomial.polynomial_processor.PolynomialProcessor;
import src.utils.ElapsedTimeCounter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static src.utils.Utils.timeWatch;

public class GaloisParallelLFSR {

    public static void main(String[] args) {

        final GaloisParallelLFSR lfsr = new GaloisParallelLFSR();
        // Initial state of Galois registers, all set to 1
        final int[] initialState = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        // The feedback indexes
        final int[] exOrIndexes = new int[]{2, 3, 6, 8, 9, initialState.length - 1};
        // The maximum period of GaloisLFSR, period = 2^registersCount - 1
        final int maxRun = (int) Math.pow(2, initialState.length) - 1;

        // Parallel generation
        final ElapsedTimeCounter etc = new ElapsedTimeCounter();
        etc.start();
        lfsr.generate(initialState, exOrIndexes, 0, outputBits -> {
            final long timeElapseInParallel = etc.stop();
            System.out.println("Parallel\t" + timeElapseInParallel + "\t" + Arrays.toString(outputBits));
        });

        // Linear generation
        final int[] linearOutputBits = new int[maxRun];
        final GaloisLfsr polynomial = new GaloisLfsr(exOrIndexes, initialState.length);
        final long timeElapseInSequential = timeWatch(() -> {
            for (int i = 0; i < maxRun; i++) {
                linearOutputBits[i] = polynomial.process();
            }
        });

        System.out.println("Sequential\t" + timeElapseInSequential + "\t" + Arrays.toString(linearOutputBits));
    }

    // ThreadPool for parallel generation
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(100,
            100,
            100000,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(100000, false) {
            }, runnable -> {
        final Thread thread = new Thread(runnable);
        thread.setDaemon(false);
        return thread;
    });

    private boolean skipParallelBits;

    private volatile boolean isFinished = false;

    public GaloisParallelLFSR() {
        this.skipParallelBits = false;
    }

    public GaloisParallelLFSR(boolean skipParallelBits) {
        this.skipParallelBits = skipParallelBits;
    }

    private Consumer<int[]> onCompleteListener;
    @Nullable
    private BiConsumer<Integer, Integer> onEachStepGeneration;

    public void generate(int[] initialState,
                         int[] exOrIndexes,
                         int step,
                         Consumer<int[]> onCompleteListener) {
        generate(initialState, exOrIndexes, new int[]{initialState.length}, step, onCompleteListener, null);
    }

    public void generate(int[] initialState,
                         int[] exOrIndexes,
                         int[] outputRegisters,
                         int step,
                         Consumer<int[]> onCompleteListener) {
        generate(initialState, exOrIndexes, outputRegisters, step, onCompleteListener, null);
    }

    public void generate(int[] initialState,
                         int[] exOrIndexes,
                         int[] outputRegisters,
                         int step,
                         Consumer<int[]> onCompleteListener,
                         BiConsumer<Integer, Integer> onEachStepGeneration) {
        this.onCompleteListener = onCompleteListener;
        this.onEachStepGeneration = onEachStepGeneration;
        // Create the initial state
        final PolynomialState state = new PolynomialState(initialState);
        // The maximum period of GaloisLFSR, period = 2^registersCount - 1
        int maxRun = (int) Math.pow(2, state.getValues().length) - 1;
        // Initialize the output array with length maxRun
        int[] output = new int[maxRun];
        isFinished = false;
        // Start the generation
        generate(state, exOrIndexes, outputRegisters, step, output);
    }

    private void generate(PolynomialState state,
                          final int[] exOrIndexes,
                          final int[] outputRegisters,
                          int step,
                          final int[] output) {

        // Defines the registers count
        final int registersCount = state.getValues().length;
        // The maximum period of GaloisLFSR, period = 2^registersCount - 1
        final int maxRun = (int) Math.pow(2, registersCount) - 1;
        // Create the GaloisLFSR with specified feedback positions and registers count
        final PolynomialProcessor lfsr = new GaloisLfsr(exOrIndexes, registersCount, outputRegisters);
        // Set current state of GaloisLFSR
        lfsr.setState(state);

        // Hold required states for defining upcoming state with state modulo addition
        final HashMap<Integer, PolynomialState> states = new HashMap<Integer, PolynomialState>();

        // Put the So state
        states.put(step, null);
        // Define the required states
        for (int feedbackPosition : exOrIndexes) {
            states.put(step + feedbackPosition -1, null);
        }

        // Start output generation for provided step
        for (int generationStep = step; generationStep < step + registersCount; generationStep++) {

            // Check for end of the generation
            if (generationStep == maxRun) {
                isFinished = true;
                onCompleteListener.accept(output);
                break;
            }

            // Check for saving state value
            if (states.containsKey(generationStep)) {
                states.put(generationStep, lfsr.captureState());
            }

            // Generates and save the output bit
            final int outputBit = lfsr.process();
            output[generationStep] = outputBit;
            if (onEachStepGeneration != null) {
                onEachStepGeneration.accept(generationStep, outputBit);
            }

            // Check if all required states for parallel generation are ready
            if (generationStep == Collections.max(states.keySet())) {
                // Defining the step of state from state modulo addition
                final int parallelStep = step + registersCount;
                // Check if the parallelStep is not out of required generation output
                if (parallelStep <= maxRun) {
                    // Sum the collected states
                    PolynomialState sumState = states.values().iterator().next();
                    for (PolynomialState nextState : states.values()) {
                        if (sumState != nextState) {
                            sumState = sumState.exOr(nextState);
                        }
                    }
                    final PolynomialState finalState = sumState;

                    Runnable nextGeneration = () -> {
                        // Pass the calculated state, exOrIndexes, parallelStep, output
                        generate(finalState, exOrIndexes, outputRegisters, parallelStep, output);
                    };
                    // Post the parallel generation to the new Java Thread
                    if (skipParallelBits) {
                        nextGeneration.run();
                    } else {
                        executor.execute(nextGeneration);
                    }

                    // we break parallel bits generation, mostly it's needed for correlation
                    if (skipParallelBits) {
                        break;
                    }
                }
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}
