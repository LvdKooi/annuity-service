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
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.DUE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getPaymentsList().size(), is(12));
    }

    @Test
    public void testRepaymentScheduleQuarterly() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.DUE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getPaymentsList().size(), is(4));
    }

    @Test
    public void testRepaymentScheduleSemiAnnually() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.DUE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getPaymentsList().size(), is(2));
    }

    @Test
    public void testRepaymentScheduleAnnually() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.DUE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getPaymentsList().size(), is(1));
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

}
