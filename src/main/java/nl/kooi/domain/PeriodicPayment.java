package nl.kooi.domain;

import nl.kooi.dto.PeriodicPaymentDto;
import nl.kooi.dto.Periodicity;
import nl.kooi.dto.Timing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;

import static nl.kooi.utils.ActuarialUtils.determineAnnuity;
import static nl.kooi.utils.ActuarialUtils.determinePeriodicInterestFraction;


public class PeriodicPayment {
    private BigDecimal totalPayment;
    private int periodNumber;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;
    private Loan loan;
    private int numberOfPayments;
    private BigDecimal annualInterestRate;
    private BigDecimal periodicInterestRate;


    private PeriodicPayment(Loan loan, int period) {
        this.loan = loan;
        numberOfPayments = periodToNumberOfPayments(loan.getLoanPeriod(), loan.getPeriodicity());
        annualInterestRate = loan.getAnnualInterestPercentage().divide(new BigDecimal(100), 10, RoundingMode.HALF_UP);
        periodicInterestRate = determinePeriodicInterestFraction(getAnnualInterestRate(), loan.getPeriodicity());
        periodNumber = period;
        setTotalPeriodicPayment();
        determineInterestAndRepaymentOfPeriod();
    }

    public static PeriodicPayment of(Loan loan, int period) {
        int numberOfPayments = periodToNumberOfPayments(loan.getLoanPeriod(), loan.getPeriodicity());

        if (period < 0 || period > numberOfPayments) {
            throw new IllegalArgumentException("Invalid period, period must be between 0 and " + numberOfPayments);
        }

        return new PeriodicPayment(loan, period);
    }

    private static int periodToNumberOfPayments(Period loanPeriod, Periodicity periodicity) {
        switch (periodicity) {
            case MONTHLY:
                return (int) loanPeriod.toTotalMonths();
            case QUARTERLY:
                return (int) loanPeriod.toTotalMonths() / 3;
            case SEMI_ANNUALY:
                return loanPeriod.getYears() * 2;
            case ANNUALY:
                return loanPeriod.getYears();
        }

        throw new IllegalArgumentException("Loan contains invalid loanPeriod.");
    }

    public PeriodicPaymentDto toDto() {
        PeriodicPaymentDto dto = new PeriodicPaymentDto() {
        };
        dto.period = getPeriodNumber();
        dto.totalPayment = getTotalPayment();
        dto.interestAmount = getInterestAmount();
        dto.repaymentAmount = getRepaymentAmount();
        return dto;
    }

    private void determineInterestAndRepaymentOfPeriod() {
        BigDecimal residualDebt = BigDecimal.ZERO;

        for (int i = 1; i <= getPeriodNumber(); i++) {

//            the difference between due and immediate payment is expressed at the first payment
            if (i == 1) {
                interestAmount = getLoan().getTiming() == Timing.DUE ? getLoan().getInitialLoan().multiply(getPeriodicInterestRate()) : BigDecimal.ZERO;
                repaymentAmount = BigDecimal.ZERO.compareTo(getInterestAmount()) == 0 ? getTotalPayment() : getTotalPayment().subtract(getInterestAmount());
                residualDebt = getLoan().getInitialLoan().subtract(getRepaymentAmount());
            } else {
                interestAmount = residualDebt.multiply(getPeriodicInterestRate());
                repaymentAmount = getTotalPayment().subtract(getInterestAmount());
                residualDebt = residualDebt.subtract(getRepaymentAmount());

            }
        }
    }

    private void setTotalPeriodicPayment() {
        totalPayment = getLoan().getInitialLoan().divide(determineAnnuity(getLoan().getTiming(), getLoan().getPeriodicity(), getPeriodicInterestRate(), getNumberOfPayments()), 10, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public Loan getLoan() {
        return loan;
    }

    public int getNumberOfPayments() {
        return numberOfPayments;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public BigDecimal getPeriodicInterestRate() {
        return periodicInterestRate;
    }
}

