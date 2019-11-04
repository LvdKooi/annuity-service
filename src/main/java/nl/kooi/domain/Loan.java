package nl.kooi.domain;

import nl.kooi.dto.Periodicity;
import nl.kooi.dto.Timing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class Loan {
    private BigDecimal initialLoan;
    private BigDecimal annualInterestPercentage;
    private Periodicity periodicity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Period loanPeriod;
    private Timing timing;
    private BigDecimal annualInterestRate;
    private BigDecimal annualEffectiveDiscountRate;
    private BigDecimal periodicInterestRate;
    private int periods;


    public Loan(BigDecimal initialLoan,
                BigDecimal annualInterestPercentage,
                Periodicity periodicity,
                Timing timing,
                LocalDate startdate,
                Period loanPeriod) {

        this.initialLoan = initialLoan;
        this.annualInterestPercentage = annualInterestPercentage;
        this.periodicity = periodicity;
        this.timing = timing;
        this.annualInterestRate = annualInterestPercentage.divide(new BigDecimal(100));
        this.periodicInterestRate = determinePeriodicInterestFraction(annualInterestPercentage);
        this.annualEffectiveDiscountRate = determineAnnualEffectiveDiscountRate(annualInterestPercentage);
        this.startDate = startdate;
        this.loanPeriod= loanPeriod;
        this.endDate = startdate.plus(loanPeriod);
        this.periods = periodToPeriods(loanPeriod,periodicity);
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

    private static int periodToPeriods(Period loanPeriod, Periodicity periodicity ) {
        switch (periodicity) {
            case MONTHLY:
                return loanPeriod.getMonths();
            case QUARTERLY:
                return loanPeriod.getMonths() / 4;
            case SEMI_ANNUALY:
                return loanPeriod.getYears() * 2;
            case ANNUALY:
                return loanPeriod.getYears();
        }

        throw new IllegalArgumentException("Loan contains invalid loanPeriod.");
    }
}
