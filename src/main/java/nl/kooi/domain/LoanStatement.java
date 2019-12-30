package nl.kooi.domain;

import nl.kooi.dto.LoanStatementDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import static nl.kooi.utils.ActuarialUtils.getAnnuity;

public class LoanStatement {
    private Loan loan;
    private int periodNumber;
    private int numberOfPayments;
    private LocalDate date;
    private Payment payment;
    private BigDecimal balance;
    private BigDecimal totalInterest;


    private LoanStatement(Loan loan, int period) {
        this.loan = loan;
        numberOfPayments = periodToNumberOfPayments(loan.getLoanPeriod(), loan.getPeriodicity());
        periodNumber = period;
        balance = BigDecimal.ZERO;
        totalInterest = BigDecimal.ZERO;
        setDateOfPeriod();
        setBalanceTotalInterestAndPayment();
    }

    public static LoanStatement of(Loan loan, int period) {
        int numberOfPayments = periodToNumberOfPayments(loan.getLoanPeriod(), loan.getPeriodicity());

        if (period < 0 || period > numberOfPayments) {
            throw new IllegalArgumentException("Invalid period, period must be between 0 and " + numberOfPayments);
        }

        return new LoanStatement(loan, period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanStatement that = (LoanStatement) o;
        return numberOfPayments == that.numberOfPayments &&
                loan.equals(that.loan) &&
                payment.equals(that.payment) &&
                balance.equals(that.balance) &&
                totalInterest.equals(that.totalInterest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loan, numberOfPayments, payment, balance, totalInterest);
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

    public LoanStatementDto toDto() {
        LoanStatementDto dto = new LoanStatementDto();
        dto.period = periodNumber;
        dto.balance = balance;
        dto.totalInterest = totalInterest;
        dto.payment = payment.toDto();
        dto.date = date;
        return dto;
    }

    private void setBalanceTotalInterestAndPayment() {
        BigDecimal repaymentAmount;
        BigDecimal interestAmount;
        BigDecimal totalPayment = getTotalPeriodicPayment();

        for (int i = 1; i <= getPeriodNumber(); i++) {

//            the difference between due and immediate payment is expressed at the first payment
            if (i == 1) {
                interestAmount = loan.getTiming() == Timing.IMMEDIATE ? getLoan().getInitialLoan().multiply(loan.getPeriodicInterestRate()) : BigDecimal.ZERO;
                totalInterest = totalInterest.add(interestAmount);
                repaymentAmount = BigDecimal.ZERO.compareTo(interestAmount) == 0 ? totalPayment : totalPayment.subtract(interestAmount);
                balance = loan.getInitialLoan().subtract(repaymentAmount);
            } else {
                interestAmount = balance.multiply(loan.getPeriodicInterestRate());
                totalInterest = totalInterest.add(interestAmount);
                repaymentAmount = totalPayment.subtract(interestAmount);
                balance = balance.subtract(repaymentAmount);

            }

            if (i == periodNumber) {
                payment = new Payment(totalPayment, interestAmount, repaymentAmount);
            }
        }
    }

    private BigDecimal getTotalPeriodicPayment() {
        return loan.getInitialLoan().divide(getAnnuity(getLoan().getTiming(), loan.getPeriodicInterestRate(), getNumberOfPayments()), 10, RoundingMode.HALF_UP);
    }

    private void setDateOfPeriod() {
        int monthsBetweenPaymentDates = 12 / loan.getPeriodicity().getDivisor();

        date = loan.getTiming() == Timing.IMMEDIATE ? loan.getStartDate().withDayOfMonth(1).plusMonths((monthsBetweenPaymentDates * periodNumber)).minusDays(1) : loan.getStartDate().withDayOfMonth(1).plusMonths((periodNumber - 1) * monthsBetweenPaymentDates);

    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public Payment getPayment() {
        return payment;
    }

    public Loan getLoan() {
        return loan;
    }

    public int getNumberOfPayments() {
        return numberOfPayments;
    }

}

