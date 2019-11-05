package nl.kooi.domain;

import nl.kooi.dto.PeriodicPaymentDto;
import nl.kooi.dto.Timing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class PeriodicPayment {
    private static BigDecimal totalPayment;
    private static int periodNumber;
    private static BigDecimal interestAmount;
    private static BigDecimal repaymentAmount;
    private static Loan loan;
    private static int numberOfPayments;
    private static BigDecimal annualInterestRate;
    private static BigDecimal annualEffectiveDiscountRate;
    private static BigDecimal periodicInterestRate;
    private static LocalDate paymentDate;


    private PeriodicPayment(Loan loan) {
        PeriodicPayment.loan = loan;
        numberOfPayments = periodToNumberOfPayments(loan);
        annualInterestRate = loan.getAnnualInterestPercentage().divide(new BigDecimal(100));
        periodicInterestRate = determinePeriodicInterestFraction(annualInterestRate);
        annualEffectiveDiscountRate = determineAnnualEffectiveDiscountRate(annualInterestRate);
        setTotalPeriodicPayment();
    }

    public static PeriodicPayment of(Loan loan, int period) {
        PeriodicPayment periodicPayment = new PeriodicPayment(loan);

        if (period < 0 || period > numberOfPayments) {
            throw new IllegalArgumentException("Invalid period, period must be between 0 and " + numberOfPayments);
        }

        periodNumber = period;
        determineInterestAndRepaymentOfPeriod();

        return periodicPayment;
    }

    public PeriodicPaymentDto toDto() {
        PeriodicPaymentDto dto = new PeriodicPaymentDto() {
        };
        dto.period = periodNumber;
        dto.interestAmount = interestAmount;
        dto.repaymentAmount = repaymentAmount;
        return dto;
    }

    private static void determineInterestAndRepaymentOfPeriod() {
        BigDecimal residualDebt = BigDecimal.ZERO;

        for(int i = 1 ; i <= periodNumber; i++){
            if(i ==1) {
                interestAmount = loan.getTiming() == Timing.DUE ? loan.getInitialLoan().multiply(annualInterestRate) : BigDecimal.ZERO;
                repaymentAmount = BigDecimal.ZERO.compareTo(interestAmount) == 0 ? totalPayment : totalPayment.subtract(interestAmount);
                residualDebt = loan.getInitialLoan().subtract(repaymentAmount);
            }
            interestAmount = residualDebt.multiply(annualInterestRate);
            repaymentAmount = totalPayment.subtract(interestAmount);
        }
    }

    private void setTotalPeriodicPayment() {
        totalPayment = loan.getInitialLoan().divide(determineAnnuity(loan.getTiming()));
    }

    private static int periodToNumberOfPayments(Loan loan) {
        Period loanPeriod = loan.getLoanPeriod();
        switch (loan.getPeriodicity()) {
            case MONTHLY:
                return loanPeriod.getMonths();
            case QUARTERLY:
                return loanPeriod.getMonths() / 4;
            case SEMI_ANNUALY:
                return loanPeriod.getYears() * 2;
            case ANNUALY:
                return loanPeriod.getYears();
        }

        throw new IllegalArgumentException("Loan contains invalid loanPeriod.");
    }

    //    i
    private BigDecimal determinePeriodicInterestFraction(BigDecimal annualInterestFraction) {
        return (BigDecimal.ONE.add(annualInterestFraction).pow(1 / loan.getPeriodicity().getDivisor())).subtract(BigDecimal.ONE);
    }

    //    d
    private BigDecimal determineAnnualEffectiveDiscountRate(BigDecimal annualInterestFraction) {
        return annualInterestFraction.divide(BigDecimal.ONE.add(annualInterestFraction));
    }

    private BigDecimal determineAnnuity(Timing timing) {
        BigDecimal divisor = timing == Timing.IMMEDIATE ? periodicInterestRate : annualEffectiveDiscountRate;
        return BigDecimal.ONE.subtract(BigDecimal.ONE.divide(periodicInterestRate.add(BigDecimal.ONE).pow(numberOfPayments))).divide(divisor);
    }

}

