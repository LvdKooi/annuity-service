package nl.kooi.domain;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RepaymentScheduleTests {
    private Loan.Builder loanBuilder = new Loan.Builder();
    private BigDecimal initionalLoan = BigDecimal.valueOf(197000);
    private BigDecimal annualInterestPercentage = BigDecimal.valueOf(2.69);

    @Test
    public void testRepaymentScheduleMonthly() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getPaymentsList().size(), is(12));
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule,loan);
    }

    @Test
    public void testRepaymentScheduleQuarterly() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getPaymentsList().size(), is(4));
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule,loan);
    }

    @Test
    public void testRepaymentScheduleSemiAnnually() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.IMMEDIATE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getPaymentsList().size(), is(2));
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule,loan);
    }

    @Test
    public void testRepaymentScheduleAnnually() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.IMMEDIATE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getPaymentsList().size(), is(1));
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule,loan);
    }

    private Loan getLoan(Periodicity periodicity, Timing timing) {
        return loanBuilder
                .setInitialLoan(initionalLoan)
                .setAnnualInterestPercentage(annualInterestPercentage)
                .setPeriodicity(periodicity)
                .setStartdate(LocalDate.of(2020, 1, 1))
                .setMonths(12)
                .setTiming(timing)
                .build();

    }

    private void assertFirstAndLastPeriodicPaymentInSchedule(RepaymentSchedule repaymentSchedule, Loan loan) {
        int numberOfPayments = repaymentSchedule.getPaymentsList().get(0).getNumberOfPayments();
        assertThat("First periodic payment is different than expectation", repaymentSchedule.getPaymentsList().get(0).equals(LoanStatement.of(loan, 1)));
        assertThat("Last periodic payment is different than expectation", repaymentSchedule.getPaymentsList().get(numberOfPayments - 1).equals(LoanStatement.of(loan, numberOfPayments)));

    }

}
