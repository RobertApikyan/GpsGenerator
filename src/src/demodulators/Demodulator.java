package src.demodulators;

/**
 * Created by Robert on 01.10.2017.
 */
public interface Demodulator<R,A> {
    R demodulate(A data);
}
