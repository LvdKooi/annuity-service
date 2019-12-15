package nl.kooi.domain;

import nl.kooi.dto.Periodicity;
import nl.kooi.dto.Timing;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PeriodicPaymentTests {

    private Loan.Builder loanBuilder = new Loan.Builder();
    private BigDecimal initionalLoan = BigDecimal.valueOf(197000);
    private BigDecimal annualInterestPercentage = BigDecimal.valueOf(2.69);


    @Test
    public void testFirstPaymentMonthlyDue() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.DUE);
        PeriodicPayment periodicPayment = PeriodicPayment.of(loan, 1);

        assertThat(periodicPayment.getNumberOfPayments(), is(360));

        assertTotalPaymentIs(periodicPayment, BigDecimal.valueOf(794.60));
        assertInterestAmountIs(periodicPayment, BigDecimal.valueOf(436.26));
        assertRepaymentAmountIs(periodicPayment, BigDecimal.valueOf(358.35));

        assertInterestPlusRepaymentEqualsTotalPayment(periodicPayment);
    }

    @Test
    public void testLastPaymentMonthlyDue() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.DUE);
        PeriodicPayment periodicPayment = PeriodicPayment.of(loan, 360);

        assertThat(periodicPayment.getNumberOfPayments(), is(360));

        assertTotalPaymentIs(periodicPayment, BigDecimal.valueOf(794.60));
        assertInterestAmountIs(periodicPayment, BigDecimal.valueOf(1.76));
        assertRepaymentAmountIs(periodicPayment, BigDecimal.valueOf(792.85));

        assertInterestPlusRepaymentEqualsTotalPayment(periodicPayment);
    }

    @Test
    public void testFirstPaymentMonthlyImmediate() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        PeriodicPayment periodicPayment = PeriodicPayment.of(loan, 1);

        assertThat(periodicPayment.getNumberOfPayments(), is(360));

        assertTotalPaymentIs(periodicPayment, BigDecimal.valueOf(792.85));
        assertInterestAmountIs(periodicPayment, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(periodicPayment, BigDecimal.valueOf(792.85));

        assertInterestPlusRepaymentEqualsTotalPayment(periodicPayment);
    }

    @Test
    public void testLastPaymentMonthlyImmediate() {
        Loan loan = getLoan(Periodicity.MONTHLY, Timing.IMMEDIATE);
        PeriodicPayment periodicPayment = PeriodicPayment.of(loan, 360);

        assertThat(periodicPayment.getNumberOfPayments(), is(360));

        assertTotalPaymentIs(periodicPayment, BigDecimal.valueOf(792.85));
        assertInterestAmountIs(periodicPayment, BigDecimal.valueOf(1.75));
        assertRepaymentAmountIs(periodicPayment, BigDecimal.valueOf(791.10));

        assertInterestPlusRepaymentEqualsTotalPayment(periodicPayment);
    }

    @Test
    public void testFirstPaymentQuarterlyDue() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.DUE);
        PeriodicPayment periodicPayment = PeriodicPayment.of(loan, 1);

        assertThat(periodicPayment.getNumberOfPayments(), is(120));

        assertTotalPaymentIs(periodicPayment, BigDecimal.valueOf(2389.10));
        assertInterestAmountIs(periodicPayment, BigDecimal.valueOf(1311.67));
        assertRepaymentAmountIs(periodicPayment, BigDecimal.valueOf(1077.43));

        assertInterestPlusRepaymentEqualsTotalPayment(periodicPayment);
    }

    @Test
    public void testLastPaymentQuarterlyDue() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.DUE);
        PeriodicPayment periodicPayment = PeriodicPayment.of(loan, 120);

        assertThat(periodicPayment.getNumberOfPayments(), is(120));

        assertTotalPaymentIs(periodicPayment, BigDecimal.valueOf(2389.10));
        assertInterestAmountIs(periodicPayment, BigDecimal.valueOf(15.80));
        assertRepaymentAmountIs(periodicPayment, BigDecimal.valueOf(2373.30));

        assertInterestPlusRepaymentEqualsTotalPayment(periodicPayment);
    }

    @Test
    public void testFirstPaymentQuarterlyImmediate() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        PeriodicPayment periodicPayment = PeriodicPayment.of(loan, 1);

        assertThat(periodicPayment.getNumberOfPayments(), is(120));

        assertTotalPaymentIs(periodicPayment, BigDecimal.valueOf(2373.30));
        assertInterestAmountIs(periodicPayment, BigDecimal.valueOf(0.00));
        assertRepaymentAmountIs(periodicPayment, BigDecimal.valueOf(2373.30));

        assertInterestPlusRepaymentEqualsTotalPayment(periodicPayment);
    }

    @Test
    public void testLastPaymentQuarterlyImmediate() {
        Loan loan = getLoan(Periodicity.QUARTERLY, Timing.IMMEDIATE);
        PeriodicPayment periodicPayment = PeriodicPayment.of(loan, 120);

        assertThat(periodicPayment.getNumberOfPayments(), is(120));

        assertTotalPaymentIs(periodicPayment, BigDecimal.valueOf(2373.30));
        assertInterestAmountIs(periodicPayment, BigDecimal.valueOf(15.70));
        assertRepaymentAmountIs(periodicPayment, BigDecimal.valueOf(2357.60));

        assertInterestPlusRepaymentEqualsTotalPayment(periodicPayment);
    }

    private Loan getLoan(Periodicity periodicity, Timing timing) {
        return loanBuilder
                .setInitialLoan(initionalLoan)
                .setAnnualInterestPercentage(annualInterestPercentage)
                .setPeriodicity(periodicity)
                .setStartdate(LocalDate.of(2017, 04, 01))
                .setEnddate(LocalDate.of(2047, 03, 31))
                .setTiming(timing)
                .build();

    }

    private static void assertTotalPaymentIs(PeriodicPayment periodicPayment, BigDecimal totalPayment) {
        assertThat(periodicPayment.getTotalPayment().setScale(2, RoundingMode.HALF_UP), is(totalPayment.setScale(2, RoundingMode.HALF_UP)));
    }

    private static void assertRepaymentAmountIs(PeriodicPayment periodicPayment, BigDecimal repaymentAmount) {
        assertThat(periodicPayment.getRepaymentAmount().setScale(2, RoundingMode.HALF_UP), is(repaymentAmount.setScale(2, RoundingMode.HALF_UP)));
    }

    private static void assertInterestAmountIs(PeriodicPayment periodicPayment, BigDecimal interestAmount) {
        assertThat(periodicPayment.getInterestAmount().setScale(2, RoundingMode.HALF_UP), is(interestAmount.setScale(2, RoundingMode.HALF_UP)));
    }

    private static void assertInterestPlusRepaymentEqualsTotalPayment(PeriodicPayment periodicPayment) {
        assertThat(periodicPayment.getRepaymentAmount().add(periodicPayment.getInterestAmount()).setScale(2, RoundingMode.HALF_UP), is(periodicPayment.getTotalPayment().setScale(2, RoundingMode.HALF_UP)));
    }
}
