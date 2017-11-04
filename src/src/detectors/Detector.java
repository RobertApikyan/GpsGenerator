package src.detectors;

/**
 * Created by Robert on 01.10.2017.
 */
public interface Detector<R,A> {
    R detect(A a);
}
