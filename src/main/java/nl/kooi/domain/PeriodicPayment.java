package nl.kooi.domain;

import nl.kooi.dto.PeriodicPaymentDto;
import nl.kooi.dto.Timing;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        annualInterestRate = loan.getAnnualInterestPercentage().divide(new BigDecimal(100),10,RoundingMode.HALF_UP);
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

        for (int i = 1; i <= periodNumber; i++) {

//            the difference between due and immediate payment are expressed at the first payment
            if (i == 1) {
                interestAmount = loan.getTiming() == Timing.DUE ? loan.getInitialLoan().multiply(periodicInterestRate) : BigDecimal.ZERO;
                repaymentAmount = BigDecimal.ZERO.compareTo(interestAmount) == 0 ? totalPayment : totalPayment.subtract(interestAmount);
                residualDebt = loan.getInitialLoan().subtract(repaymentAmount);
            } else {
                interestAmount = residualDebt.multiply(periodicInterestRate);
                repaymentAmount = totalPayment.subtract(interestAmount);
                residualDebt = residualDebt.subtract(repaymentAmount);

            }
        }
    }

    private void setTotalPeriodicPayment() {
        totalPayment = loan.getInitialLoan().divide(determineAnnuity(loan.getTiming()), 10, RoundingMode.HALF_UP);
    }

    private static int periodToNumberOfPayments(Loan loan) {
        Period loanPeriod = loan.getLoanPeriod();
        switch (loan.getPeriodicity()) {
            case MONTHLY:
                return (int) loanPeriod.toTotalMonths();
            case QUARTERLY:
                return (int) loanPeriod.toTotalMonths() / 4;
            case SEMI_ANNUALY:
                return loanPeriod.getYears() * 2;
            case ANNUALY:
                return loanPeriod.getYears();
        }

        throw new IllegalArgumentException("Loan contains invalid loanPeriod.");
    }

    //    i
    private BigDecimal determinePeriodicInterestFraction(BigDecimal annualInterestFraction) {
        double periodInterestPlusOne = Math.pow((BigDecimal.ONE.add(annualInterestFraction)).setScale(10,RoundingMode.HALF_UP).doubleValue(),
                1 / (double) loan.getPeriodicity().getDivisor());
        return BigDecimal.valueOf(periodInterestPlusOne -1);
    }

    //    d
    private BigDecimal determineAnnualEffectiveDiscountRate(BigDecimal annualInterestFraction) {
        return periodicInterestRate.divide(BigDecimal.ONE.add(periodicInterestRate), 10, RoundingMode.HALF_UP);
    }

    private BigDecimal determineAnnuity(Timing timing) {
        BigDecimal divisor = timing == Timing.IMMEDIATE ? annualEffectiveDiscountRate: periodicInterestRate;
        BigDecimal denominator  = BigDecimal.ONE.subtract(BigDecimal.ONE.divide((periodicInterestRate.add(BigDecimal.ONE)).pow(numberOfPayments), 10, RoundingMode.HALF_UP));
        return denominator.divide(divisor,10, RoundingMode.HALF_UP);
    }

    public PeriodicPaymentDto toDto(PeriodicPayment periodicPayment) {
        PeriodicPaymentDto dto = new PeriodicPaymentDto();
        dto.period = periodNumber;
        dto.totalPayment = totalPayment;
        dto.interestAmount = interestAmount;
        dto.repaymentAmount = repaymentAmount;

        return dto;
    }

}

