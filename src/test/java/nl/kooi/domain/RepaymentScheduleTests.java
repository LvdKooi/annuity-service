package nl.kooi.domain;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


public class RepaymentScheduleTests {
    private Loan.LoanBuilder loanBuilder = Loan.builder();
    private BigDecimal initialLoan = BigDecimal.valueOf(197000);
    private BigDecimal annualInterestPercentage = BigDecimal.valueOf(2.69);

    @Test
    public void testRepaymentScheduleMonthly() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getLoanStatements().size()).isEqualTo(12);
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule, loan);
    }

    @Test
    public void testRepaymentScheduleQuarterly() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getLoanStatements().size()).isEqualTo(4);
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule, loan);
    }

    @Test
    public void testRepaymentScheduleSemiAnnually() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.IMMEDIATE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getLoanStatements().size()).isEqualTo(2);
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule, loan);
    }

    @Test
    public void testRepaymentScheduleAnnually() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.IMMEDIATE);
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule(loan);
        assertThat(repaymentSchedule.getLoanStatements().size()).isEqualTo(1);
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule, loan);
    }

    private Loan getLoan(Periodicity periodicity, Timing timing) {
        return loanBuilder
                .initialLoan(initialLoan)
                .annualInterestPercentage(annualInterestPercentage)
                .periodicity(periodicity)
                .startdate(LocalDate.of(2020, 1, 1))
                .months(12)
                .timing(timing)
                .build();
    }

    private void assertFirstAndLastPeriodicPaymentInSchedule(RepaymentSchedule repaymentSchedule, Loan loan) {
        int numberOfPayments = repaymentSchedule.getLoanStatements().get(0).getNumberOfPayments();
        assertThat(repaymentSchedule.getLoanStatements().get(0)).isEqualTo(LoanStatement.of(loan, 1));
        assertThat(repaymentSchedule.getLoanStatements().get(numberOfPayments - 1)).isEqualTo(LoanStatement.of(loan, numberOfPayments));
    }

}
