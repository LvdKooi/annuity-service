package nl.kooi.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.kooi.exception.LoanException;
import nl.kooi.utils.ActuarialUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;


@EqualsAndHashCode
@Getter
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
        numberOfPayments = periodToNumberOfPayments(loan.getLoanTerm(), loan.getPeriodicity());
        periodNumber = period;
        balance = BigDecimal.ZERO;
        totalInterest = BigDecimal.ZERO;
        setDateOfPeriod();
        setBalanceTotalInterestAndPayment();
    }

    public static LoanStatement of(Loan loan, int period) {
        var numberOfPayments = periodToNumberOfPayments(loan.getLoanTerm(), loan.getPeriodicity());

        if (period < 0 || period > numberOfPayments) {
            throw new IllegalArgumentException("Invalid period, period must be between 0 and " + numberOfPayments);
        }

        return new LoanStatement(loan, period);
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

    private void setBalanceTotalInterestAndPayment() {
        var repaymentAmount = BigDecimal.ZERO;
        var interestAmount = BigDecimal.ZERO;
        var totalPayment = getTotalPeriodicPayment();

        for (int i = 1; i <= getPeriodNumber(); i++) {

//            the difference between due and immediate payment is expressed at the first payment
            if (i == 1) {
                interestAmount = getLoan().getTiming() == Timing.IMMEDIATE ? getLoan().getInitialLoan().multiply(getLoan().getPeriodicInterestRate()) : BigDecimal.ZERO;
                totalInterest = getTotalInterest().add(interestAmount);
                repaymentAmount = BigDecimal.ZERO.compareTo(interestAmount) == 0 ? totalPayment : totalPayment.subtract(interestAmount);
                balance = getLoan().getInitialLoan().subtract(repaymentAmount).max(BigDecimal.ZERO);
            } else {
                interestAmount = getBalance().multiply(getLoan().getPeriodicInterestRate());
                totalInterest = getTotalInterest().add(interestAmount);
                repaymentAmount = totalPayment.subtract(interestAmount);
                balance = getBalance().subtract(repaymentAmount).max(BigDecimal.ZERO);

            }

            if (i == getPeriodNumber()) {
                payment = new Payment(totalPayment, interestAmount, repaymentAmount);
            }
        }
    }

    private BigDecimal getTotalPeriodicPayment() {
        switch (getLoan().getRepaymentType()) {
            case ANNUITY:
                return getLoan().getInitialLoan().divide(ActuarialUtils.getAnnuity(getLoan().getTiming(), getLoan().getPeriodicInterestRate(), getNumberOfPayments()), 10, RoundingMode.HALF_UP);
            case STRAIGHT_LINE:
                return getLoan().getInitialLoan().divide(BigDecimal.valueOf(getNumberOfPayments()), 10, RoundingMode.HALF_UP);
            default:
                throw new LoanException(String.format("Not existing repayment type: %s", getLoan().getRepaymentType()));
        }
    }

    private void setDateOfPeriod() {
        var monthsBetweenPaymentDates = 12 / getLoan().getPeriodicity().getDivisor();

        date = getLoan().getTiming() == Timing.IMMEDIATE ? getLoan().getStartDate().withDayOfMonth(1).plusMonths(((long) monthsBetweenPaymentDates * getPeriodNumber())).minusDays(1) : getLoan().getStartDate().withDayOfMonth(1).plusMonths((long) (getPeriodNumber() - 1) * monthsBetweenPaymentDates);

    }
}


