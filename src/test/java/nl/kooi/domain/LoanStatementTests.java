package nl.kooi.domain;



import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;


class LoanStatementTests {
    private final Loan.LoanBuilder loanBuilder = Loan.builder();
    private final BigDecimal initialLoan = BigDecimal.valueOf(197000);
    private final BigDecimal annualInterestPercentage = BigDecimal.valueOf(2.69);

    @Test
    public void testFirstPaymentMonthlyImmediate() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        //      test general information
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2017, 4, 30));
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(360);

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(794.60));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(436.26));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(358.35));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(436.26));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(197000 - 358.35));
    }

    @Test
    public void testLastPaymentMonthlyImmediate() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 360);

        //      test general information
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2047, 3, 31));
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(360);

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(794.60));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(1.76));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(792.85));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(89057.70));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(0.00));
    }

    @Test
    public void testFirstPaymentMonthlyDue() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(360);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2017, 4, 1));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(792.85));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(792.85));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(0.00));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(197000 - 792.85));
    }

    @Test
    public void testLastPaymentMonthlyDue() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 360);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(360);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2047, 3, 1));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(792.85));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(1.75));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(791.10));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertBalanceIs(loanStatement, BigDecimal.valueOf(0.00));
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(88425.62));
    }

    @Test
    public void testFirstPaymentQuarterlyImmediate() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(120);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2017, 6, 30));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(2389.10));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(1311.67));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(1077.43));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(1311.67));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(197000 - 1077.43));
    }

    @Test
    public void testLastPaymentQuarterlyImmediate() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 120);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(120);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2047, 3, 31));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(2389.10));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(15.80));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(2373.30));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(89691.64));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(0.00));
    }

    @Test
    public void testFirstPaymentQuarterlyDue() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(120);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2017, 4, 1));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(2373.30));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(2373.30));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(0.00));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(197000 - 2373.30));
    }

    @Test
    public void testLastPaymentQuarterlyDue() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 120);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(120);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2047, 1, 1));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(2373.30));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(15.70));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(2357.60));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(87795.41));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(0.00));
    }

    @Test
    public void testFirstPaymentSemiAnnualyImmediate() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(60);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2017, 9, 30));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(4794.10));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(2632.07));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(2162.03));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(2632.07));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(197000 - 2162.03));
    }

    @Test
    public void testLastPaymentSemiAnnualyImmediate() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 60);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(60);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2047, 3, 31));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(4794.10));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(63.21));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(4730.89));

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(90646.06));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(0.00));
    }

    @Test
    public void testFirstPaymentSemiAnnualyDue() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(60);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2017, 4, 1));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(4730.89));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(4730.89));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(0.00));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(197000 - 4730.89));
    }

    @Test
    public void testLastPaymentSemiAnnualyDue() {
        Loan loan = getLoan(Periodicity.SEMI_ANNUALLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 60);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(60);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2046, 10, 1));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(4730.89));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(62.37));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(4668.52));

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(86853.57));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(0.00));
    }

    @Test
    public void testFirstPaymentAnnualyImmediate() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(30);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2018, 3, 31));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(9652.25));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(5299.30));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(4352.95));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(5299.30));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(197000 - 4352.95));
    }

    @Test
    public void testLastPaymentAnnualyImmediate() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.IMMEDIATE);
        LoanStatement loanStatement = LoanStatement.of(loan, 30);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(30);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2047, 3, 31));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(9652.25));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(252.84));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(9399.41));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(92567.65));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(0.00));
    }

    @Test
    public void testFirstPaymentAnnualyDue() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 1);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(30);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2017, 4, 1));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(9399.41));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(9399.41));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(0.00));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(197000 - 9399.41));
    }

    @Test
    public void testLastPaymentAnnualyDue() {
        Loan loan = getLoan(Periodicity.ANNUALLY, Timing.DUE);
        LoanStatement loanStatement = LoanStatement.of(loan, 30);

        //      test general information
        assertThat(loanStatement.getNumberOfPayments()).isEqualTo(30);
        assertThat(loanStatement.getDate()).isEqualTo(LocalDate.of(2046, 4, 1));

        //      test payment
        assertTotalPaymentIs(loanStatement, BigDecimal.valueOf(9399.41));
        assertInterestAmountIs(loanStatement, BigDecimal.valueOf(246.22));
        assertRepaymentAmountIs(loanStatement, BigDecimal.valueOf(9153.19));
        assertInterestPlusRepaymentEqualsTotalPayment(loanStatement);

        //      test balance and totalInterest
        assertTotalInterestIs(loanStatement, BigDecimal.valueOf(84982.32));
        assertBalanceIs(loanStatement, BigDecimal.valueOf(0.00));
    }

    private Loan getLoan(Periodicity periodicity, Timing timing) {
        return loanBuilder
                .initialLoan(initialLoan)
                .annualInterestPercentage(annualInterestPercentage)
                .periodicity(periodicity)
                .startdate(LocalDate.of(2017, 4, 1))
                .loanTerm(Period.ofMonths(360))
                .timing(timing)
                .repaymentType(RepaymentType.ANNUITY)
                .build();
    }

    private static void assertTotalPaymentIs(LoanStatement loanStatement, BigDecimal totalPayment) {
        assertThat(loanStatement.getPayment().getTotalPayment().setScale(2, RoundingMode.HALF_UP)).isEqualTo(totalPayment.setScale(2, RoundingMode.HALF_UP));
    }

    private static void assertRepaymentAmountIs(LoanStatement loanStatement, BigDecimal repaymentAmount) {
        assertThat(loanStatement.getPayment().getRepaymentAmount().setScale(2, RoundingMode.HALF_UP)).isEqualTo(repaymentAmount.setScale(2, RoundingMode.HALF_UP));
    }

    private static void assertInterestAmountIs(LoanStatement loanStatement, BigDecimal interestAmount) {
        assertThat(loanStatement.getPayment().getInterestAmount().setScale(2, RoundingMode.HALF_UP)).isEqualTo(interestAmount.setScale(2, RoundingMode.HALF_UP));
    }

    private static void assertInterestPlusRepaymentEqualsTotalPayment(LoanStatement loanStatement) {
        assertThat(loanStatement.getPayment().getRepaymentAmount().add(loanStatement.getPayment().getInterestAmount()).setScale(2, RoundingMode.HALF_UP)).isEqualTo(loanStatement.getPayment().getTotalPayment().setScale(2, RoundingMode.HALF_UP));
    }

    private static void assertTotalInterestIs(LoanStatement loanStatement, BigDecimal totalInterest) {
        assertThat(loanStatement.getTotalInterest().setScale(2, RoundingMode.HALF_UP)).isEqualTo(totalInterest.setScale(2, RoundingMode.HALF_UP));
    }

    private static void assertBalanceIs(LoanStatement loanStatement, BigDecimal balance) {
        assertThat(loanStatement.getBalance().setScale(2, RoundingMode.HALF_UP)).isEqualTo(balance.setScale(2, RoundingMode.HALF_UP));
    }
}


