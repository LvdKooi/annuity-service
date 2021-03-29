package nl.kooi.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Periodicity {
    MONTHLY(12), QUARTERLY(4), SEMI_ANNUALLY(2), ANNUALLY(1);

    @Getter
    private final int divisor;
}
