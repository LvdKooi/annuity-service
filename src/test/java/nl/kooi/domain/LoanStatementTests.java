package nl.kooi.domain;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LoanStatementTests {
    private Loan.Builder loanBuilder = new Loan.Builder();
    private BigDecimal initionalLoan = BigDecimal.valueOf(197000);
    private BigDecimal annualInterestPercentage = BigDecimal.valueOf(2.69);

    @Test
    public void testFirstPaymentMonthlyDue() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        assertThat(loanStatement.getDate(), is(LocalDate.of(2017, 4, 30)));
        assertThat(loanStatement.getNumberOfPayments(), is(360));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(794.60));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(436.26));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(358.35));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testLastPaymentMonthlyDue() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 360);

        assertThat(loanStatement.getDate(), is(LocalDate.of(2047, 3, 31)));
        assertThat(loanStatement.getNumberOfPayments(), is(360));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(794.60));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(1.76));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(792.85));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testFirstPaymentMonthlyImmediate() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        assertThat(loanStatement.getNumberOfPayments(), is(360));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2017, 4, 1)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(792.85));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(792.85));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testLastPaymentMonthlyImmediate() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 360);

        assertThat(loanStatement.getNumberOfPayments(), is(360));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2047, 3, 1)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(792.85));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(1.75));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(791.10));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testFirstPaymentQuarterlyDue() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        assertThat(loanStatement.getNumberOfPayments(), is(120));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2017, 6, 30)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(2389.10));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(1311.67));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(1077.43));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testLastPaymentQuarterlyDue() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 120);

        assertThat(loanStatement.getNumberOfPayments(), is(120));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2047, 3, 31)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(2389.10));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(15.80));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(2373.30));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testFirstPaymentQuarterlyImmediate() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        assertThat(loanStatement.getNumberOfPayments(), is(120));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2017, 4, 1)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(2373.30));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(2373.30));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testLastPaymentQuarterlyImmediate() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 120);

        assertThat(loanStatement.getNumberOfPayments(), is(120));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2047, 1, 1)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(2373.30));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(15.70));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(2357.60));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testFirstPaymentSemiAnnualyDue() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        assertThat(loanStatement.getNumberOfPayments(), is(60));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2017, 9, 30)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(4794.10));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(2632.07));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(2162.03));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testLastPaymentSemiAnnualyDue() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 60);

        assertThat(loanStatement.getNumberOfPayments(), is(60));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2047, 3, 31)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(4794.10));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(63.21));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(4730.89));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testFirstPaymentSemiAnnualyImmediate() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        assertThat(loanStatement.getNumberOfPayments(), is(60));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2017, 4, 1)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(4730.89));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(4730.89));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testLastPaymentSemiAnnualyImmediate() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 60);

        assertThat(loanStatement.getNumberOfPayments(), is(60));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2046, 10, 1)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(4730.89));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(62.37));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(4668.52));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testFirstPaymentAnnualyDue() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        assertThat(loanStatement.getNumberOfPayments(), is(30));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2018, 3, 31)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(9652.25));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(5299.30));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(4352.95));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testLastPaymentAnnualyDue() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 30);

        assertThat(loanStatement.getNumberOfPayments(), is(30));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2047, 3, 31)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(9652.25));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(252.84));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(9399.41));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testFirstPaymentAnnualyImmediate() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        assertThat(loanStatement.getNumberOfPayments(), is(30));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2017, 4, 1)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(9399.41));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(9399.41));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    @Test
    public void testLastPaymentAnnualyImmediate() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 30);

        assertThat(loanStatement.getNumberOfPayments(), is(30));
        assertThat(loanStatement.getDate(), is(LocalDate.of(2046, 4, 1)));

        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(9399.41));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(246.22));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(9153.19));

        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);
    }

    private Loan getLoan(Periodicity periodicity, Timing timing) {
        return loanBuilder
                .setInitialLoan(initionalLoan)
                .setAnnualInterestPercentage(annualInterestPercentage)
                .setPeriodicity(periodicity)
                .setStartdate(LocalDate.of(2017, 4, 1))
                .setMonths(360)
                .setTiming(timing)
                .build();

    }

    private static void assertTotalPaymentIs(LoanStatement loanStatement, BigDecimal totalPayment) {
        assertThat(loanStatement.getPayment().getTotalPayment().setScale(2, RoundingMode.HALF_UP), is(totalPayment.setScale(2, RoundingMode.HALF_UP)));
    }

    private static void assertRepaymentAmountIs(LoanStatement loanStatement, BigDecimal repaymentAmount) {
        assertThat(loanStatement.getPayment().getRepaymentAmount().setScale(2, RoundingMode.HALF_UP), is(repaymentAmount.setScale(2, RoundingMode.HALF_UP)));
    }

    private static void assertInterestAmountIs(LoanStatement loanStatement, BigDecimal interestAmount) {
        assertThat(loanStatement.getPayment().getInterestAmount().setScale(2, RoundingMode.HALF_UP), is(interestAmount.setScale(2, RoundingMode.HALF_UP)));
    }

    private static void assertInterestPlusRepaymentEqualsTotalPayment(LoanStatement loanStatement) {
        assertThat(loanStatement.getPayment().getRepaymentAmount().add(loanStatement.getPayment().getInterestAmount()).setScale(2, RoundingMode.HALF_UP), is(loanStatement.getPayment().getTotalPayment().setScale(2, RoundingMode.HALF_UP)));
    }

    private static void assertTotalInterestIs(LoanStatement loanStatement, BigDecimal totalInterest) {
        assertThat(loanStatement.getTotalInterest().setScale(2, RoundingMode.HALF_UP), is(totalInterest.setScale(2, RoundingMode.HALF_UP)));
    }

    private static void assertBalanceIs(LoanStatement loanStatement, BigDecimal balance) {
        assertThat(loanStatement.getBalance().setScale(2, RoundingMode.HALF_UP), is(balance.setScale(2, RoundingMode.HALF_UP)));
    }
}


