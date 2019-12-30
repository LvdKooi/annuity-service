package nl.kooi.domain;

import nl.kooi.dto.PeriodicPaymentDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import static nl.kooi.utils.ActuarialUtils.getAnnuity;

public class PeriodicPayment {
    private BigDecimal totalPayment;
    private int periodNumber;
    private LocalDate date;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;
    private Loan loan;
    private int numberOfPayments;

    private PeriodicPayment(Loan loan, int period) {
        this.loan = loan;
        numberOfPayments = periodToNumberOfPayments(loan.getLoanPeriod(), loan.getPeriodicity());
        periodNumber = period;
        setDateOfPeriod();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodicPayment that = (PeriodicPayment) o;
        return periodNumber == that.periodNumber &&
                Objects.equals(totalPayment, that.totalPayment) &&
                Objects.equals(interestAmount, that.interestAmount) &&
                Objects.equals(repaymentAmount, that.repaymentAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPayment, periodNumber, interestAmount, repaymentAmount);
    }

    private static int periodToNumberOfPayments(Period loanPeriod, Periodicity periodicity) {
        switch (periodicity) {
            case MONTHLY:
                return (int) loanPeriod.toTotalMonths();
            case QUARTERLY:
                return (int) loanPeriod.toTotalMonths() / 3;
            case SEMI_ANNUALLY:
                return (int) loanPeriod.toTotalMonths() / 6;
            case ANNUALLY:
                return (int) loanPeriod.toTotalMonths() / 12;
        }

        throw new IllegalArgumentException("Loan contains invalid loanPeriod.");
    }

    public PeriodicPaymentDto toDto() {
        PeriodicPaymentDto dto = new PeriodicPaymentDto() {
        };
        dto.period = periodNumber;
        dto.totalPayment = totalPayment;
        dto.interestAmount = interestAmount;
        dto.repaymentAmount = repaymentAmount;
        dto.date = date;
        return dto;
    }

    private void determineInterestAndRepaymentOfPeriod() {
        BigDecimal residualDebt = BigDecimal.ZERO;

        for (int i = 1; i <= getPeriodNumber(); i++) {

//            the difference between due and immediate payment is expressed at the first payment
            if (i == 1) {
                interestAmount = loan.getTiming() == Timing.IMMEDIATE ? getLoan().getInitialLoan().multiply(loan.getPeriodicInterestRate()) : BigDecimal.ZERO;
                repaymentAmount = BigDecimal.ZERO.compareTo(getInterestAmount()) == 0 ? getTotalPayment() : getTotalPayment().subtract(getInterestAmount());
                residualDebt = loan.getInitialLoan().subtract(getRepaymentAmount());
            } else {
                interestAmount = residualDebt.multiply(loan.getPeriodicInterestRate());
                repaymentAmount = getTotalPayment().subtract(getInterestAmount());
                residualDebt = residualDebt.subtract(getRepaymentAmount());

            }
        }
    }

    private void setTotalPeriodicPayment() {
        totalPayment = loan.getInitialLoan().divide(getAnnuity(getLoan().getTiming(), loan.getPeriodicInterestRate(), getNumberOfPayments()), 10, RoundingMode.HALF_UP);
    }

    private void setDateOfPeriod() {
        int monthsBetweenPaymentDates = 12 / loan.getPeriodicity().getDivisor();

        date = loan.getTiming() == Timing.IMMEDIATE ? loan.getStartDate().withDayOfMonth(1).plusMonths((monthsBetweenPaymentDates * periodNumber)).minusDays(1) : loan.getStartDate().withDayOfMonth(1).plusMonths((periodNumber - 1) * monthsBetweenPaymentDates);

    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public LocalDate getDate() {
        return date;
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

}

