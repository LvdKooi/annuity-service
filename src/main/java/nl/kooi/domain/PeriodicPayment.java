package nl.kooi.domain;

import nl.kooi.dto.PeriodicPaymentDto;
import nl.kooi.dto.Timing;

import java.math.BigDecimal;
import java.time.Period;

public class PeriodicPayment {
    private BigDecimal totalPayment;
    private Integer period;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;
    private Loan loan;
    private int numberOfPayments;
    private BigDecimal annualInterestRate;
    private BigDecimal annualEffectiveDiscountRate;
    private BigDecimal periodicInterestRate;

    public PeriodicPayment(Loan loan, Integer period) {
        this.period = period;
        this.loan = loan;
        this.numberOfPayments = periodToNumberOfPayments(loan);
        this.annualInterestRate = loan.getAnnualInterestPercentage().divide(new BigDecimal(100));
        this.periodicInterestRate = determinePeriodicInterestFraction(annualInterestRate);
        this.annualEffectiveDiscountRate = determineAnnualEffectiveDiscountRate(annualInterestRate);
        setTotalPeriodicPayment();
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public BigDecimal getPeriodicInterestRate() {
        return periodicInterestRate;
    }

    public PeriodicPaymentDto toDto() {
        PeriodicPaymentDto dto = new PeriodicPaymentDto() {
        };
        dto.period = period;
        dto.interestAmount = interestAmount;
        dto.repaymentAmount = repaymentAmount;
        return dto;
    }

    //    TODO: implement me!
    private void determineInterestAndRepaymentOfPeriod(int periodNumber) {

    }

    private void setTotalPeriodicPayment() {
        totalPayment = loan.getInitialLoan().divide(determineAnnuity(loan.getTiming()));
    }

    private static int periodToNumberOfPayments(Loan loan) {
        Period loanPeriod = loan.getLoanPeriod();
        switch (loan.getPeriodicity()) {
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

    //    i
    private BigDecimal determinePeriodicInterestFraction(BigDecimal annualInterestFraction) {
        return (BigDecimal.ONE.add(annualInterestFraction).pow(1 / loan.getPeriodicity().getDivisor())).subtract(BigDecimal.ONE);
    }

    //    d
    private BigDecimal determineAnnualEffectiveDiscountRate(BigDecimal annualInterestFraction) {
        return annualInterestFraction.divide(BigDecimal.ONE.add(annualInterestFraction));
    }

    private BigDecimal determineAnnuity(Timing timing) {
        BigDecimal divisor = timing == Timing.IMMEDIATE ? periodicInterestRate : annualEffectiveDiscountRate;
        return BigDecimal.ONE.subtract(BigDecimal.ONE.divide(periodicInterestRate.add(BigDecimal.ONE).pow(numberOfPayments))).divide(divisor);
    }

}

