package src.polynomial.phaseSelector;

import src.polynomial.PolynomialState;

public interface PhaseSelector {
    int apply(PolynomialState state);
}
