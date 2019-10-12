package nl.kooi.dto;

public enum Periodicity {
    MONTHLY(12), QUARTERLY(4), SEMI_ANNUALY(2), ANNUALY(1);

    private int denominator;

    private Periodicity(int denominator) {
        this.denominator = denominator;

    }

    public int getDenominator() {
        return denominator;
    }
}
