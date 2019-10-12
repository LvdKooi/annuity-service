package nl.kooi.domain;

import nl.kooi.dto.Periodicity;
import nl.kooi.dto.Timing;

import java.math.BigDecimal;

public class Loan {
    private BigDecimal initialLoan;
    private BigDecimal annualInterestPercentage;
    private Periodicity periodicity;
    private Timing timing;
    private BigDecimal annualInterestRate;
    private BigDecimal annualEffectiveDiscountRate;
    private BigDecimal periodicInterestRate;


    public Loan(BigDecimal initialLoan,
                BigDecimal annualInterestPercentage,
                Periodicity periodicity,
                Timing timing) {

        this.initialLoan = initialLoan;
        this.annualInterestPercentage = annualInterestPercentage;
        this.periodicity = periodicity;
        this.timing = timing;
        this.annualInterestRate = annualInterestPercentage.divide(new BigDecimal(100));
        this.periodicInterestRate = determinePeriodicInterestFraction(annualInterestPercentage);
        this.annualEffectiveDiscountRate = determineAnnualEffectiveDiscountRate(annualInterestPercentage);

    }

    private BigDecimal determinePeriodicInterestFraction(BigDecimal annualInterestFraction) {

        return (BigDecimal.ONE.add(annualInterestFraction).pow(1 / getPeriodicity().getDivisor())).subtract(BigDecimal.ONE);
    }

    private BigDecimal determineAnnualEffectiveDiscountRate(BigDecimal annualInterestFraction) {
        return annualInterestFraction.divide(BigDecimal.ONE.add(annualInterestFraction));
    }

    public BigDecimal getInitialLoan() {
        return initialLoan;
    }

    public BigDecimal getAnnualInterestPercentage() {
        return annualInterestPercentage;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public Timing getTiming() {
        return timing;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public BigDecimal getPeriodicInterestRate() {
        return periodicInterestRate;
    }
}
