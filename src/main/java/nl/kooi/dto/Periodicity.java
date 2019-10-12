package nl.kooi.dto;

public enum Periodicity {
    MONTHLY(12), QUARTERLY(4), SEMI_ANNUALY(2), ANNUALY(1);

    private int divisor;

    Periodicity(int divisor) {
        this.divisor = divisor;
    }

    public int getDivisor() {
        return divisor;
    }
}
