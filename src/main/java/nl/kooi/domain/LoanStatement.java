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
        return getNumberOfPayments() == that.getNumberOfPayments() &&
                getLoan().equals(that.getLoan()) &&
                getPayment().equals(that.getPayment()) &&
                getBalance().equals(that.getBalance()) &&
                getTotalInterest().equals(that.getTotalInterest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLoan(), getNumberOfPayments(), getPayment(), getBalance(), getTotalInterest());
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
        dto.period = getPeriodNumber();
        dto.balance = getBalance();
        dto.totalInterest = getTotalInterest();
        dto.payment = getPayment().toDto();
        dto.date = getDate();
        return dto;
    }

    private void setBalanceTotalInterestAndPayment() {
        BigDecimal repaymentAmount;
        BigDecimal interestAmount;
        BigDecimal totalPayment = getTotalPeriodicPayment();

        for (int i = 1; i <= getPeriodNumber(); i++) {

//            the difference between due and immediate payment is expressed at the first payment
            if (i == 1) {
                interestAmount = getLoan().getTiming() == Timing.IMMEDIATE ? getLoan().getInitialLoan().multiply(getLoan().getPeriodicInterestRate()) : BigDecimal.ZERO;
                totalInterest = getTotalInterest().add(interestAmount);
                repaymentAmount = BigDecimal.ZERO.compareTo(interestAmount) == 0 ? totalPayment : totalPayment.subtract(interestAmount);
                balance = getLoan().getInitialLoan().subtract(repaymentAmount);
            } else {
                interestAmount = getBalance().multiply(getLoan().getPeriodicInterestRate());
                totalInterest = getTotalInterest().add(interestAmount);
                repaymentAmount = totalPayment.subtract(interestAmount);
                balance = getBalance().subtract(repaymentAmount);

            }

            if (i == getPeriodNumber()) {
                payment = new Payment(totalPayment, interestAmount, repaymentAmount);
            }
        }
    }

    private BigDecimal getTotalPeriodicPayment() {
        return getLoan().getInitialLoan().divide(getAnnuity(getLoan().getTiming(), getLoan().getPeriodicInterestRate(), getNumberOfPayments()), 10, RoundingMode.HALF_UP);
    }

    private void setDateOfPeriod() {
        int monthsBetweenPaymentDates = 12 / getLoan().getPeriodicity().getDivisor();

        date = getLoan().getTiming() == Timing.IMMEDIATE ? getLoan().getStartDate().withDayOfMonth(1).plusMonths((monthsBetweenPaymentDates * getPeriodNumber())).minusDays(1) : getLoan().getStartDate().withDayOfMonth(1).plusMonths((getPeriodNumber() - 1) * monthsBetweenPaymentDates);

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

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }
}

