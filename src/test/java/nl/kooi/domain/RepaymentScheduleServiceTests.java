package nl.kooi.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(RepaymentScheduleService.class)
class RepaymentScheduleServiceTests {

    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    private Loan.LoanBuilder loanBuilder = Loan.builder();
    private BigDecimal initialLoan = BigDecimal.valueOf(197000);
    private BigDecimal annualInterestPercentage = BigDecimal.valueOf(2.69);

    @Test
    public void testRepaymentScheduleMonthly() {
        var loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        var repaymentSchedule = repaymentScheduleService.getRepaymentScheduleForLoan(loan);
        assertThat(repaymentSchedule.getLoanStatements().size()).isEqualTo(12);
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule, loan);
    }

    @Test
    public void testRepaymentScheduleQuarterly() {
        var loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        var repaymentSchedule = repaymentScheduleService.getRepaymentScheduleForLoan(loan);
        assertThat(repaymentSchedule.getLoanStatements().size()).isEqualTo(4);
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule, loan);
    }

    @Test
    public void testRepaymentScheduleSemiAnnually() {
        var loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.IMMEDIATE);
        var repaymentSchedule = repaymentScheduleService.getRepaymentScheduleForLoan(loan);
        assertThat(repaymentSchedule.getLoanStatements().size()).isEqualTo(2);
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule, loan);
    }

    @Test
    public void testRepaymentScheduleAnnually() {
        var loan = getLoan(Periodicity.ANNUALLY, Timing.IMMEDIATE);
        var repaymentSchedule = repaymentScheduleService.getRepaymentScheduleForLoan(loan);
        assertThat(repaymentSchedule.getLoanStatements().size()).isEqualTo(1);
        assertFirstAndLastPeriodicPaymentInSchedule(repaymentSchedule, loan);
    }

    private Loan getLoan(Periodicity periodicity, Timing timing) {
        return loanBuilder
                .initialLoan(initialLoan)
                .annualInterestPercentage(annualInterestPercentage)
                .periodicity(periodicity)
                .startdate(LocalDate.of(2020, 1, 1))
                .loanTerm(Period.ofMonths(12))
                .timing(timing)
                .build();
    }

    private Loan.LoanBuilder getBaseLoanBuilder(Periodicity periodicity, Timing timing){

        return loanBuilder
                .initialLoan(initialLoan)
                .annualInterestPercentage(annualInterestPercentage)
                .periodicity(periodicity)
                .startdate(LocalDate.of(2020, 1, 1))
                .loanTerm(Period.ofMonths(12))
                .timing(timing);
    }

    private void assertFirstAndLastPeriodicPaymentInSchedule(RepaymentSchedule repaymentSchedule, Loan loan) {
        var numberOfPayments = repaymentSchedule.getLoanStatements().get(0).getNumberOfPayments();
        assertThat(repaymentSchedule.getLoanStatements().get(0)).isEqualTo(LoanStatement.of(loan, 1));
        assertThat(repaymentSchedule.getLoanStatements().get(numberOfPayments - 1)).isEqualTo(LoanStatement.of(loan, numberOfPayments));
    }

}
