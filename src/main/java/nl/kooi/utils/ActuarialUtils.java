package nl.kooi.utils;

import nl.kooi.dto.Periodicity;
import nl.kooi.dto.Timing;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ActuarialUtils {

    //    i
    public static BigDecimal determinePeriodicInterestFraction(BigDecimal annualInterestFraction, Periodicity periodicity) {
        double periodInterestPlusOne = Math.pow((BigDecimal.ONE.add(annualInterestFraction)).setScale(10, RoundingMode.HALF_UP).doubleValue(),
                1 / (double) periodicity.getDivisor());
        return BigDecimal.valueOf(periodInterestPlusOne - 1);
    }

    //    d
    public static BigDecimal determineAnnualEffectiveDiscountRate(BigDecimal periodicInterestRate) {
        return periodicInterestRate.divide(BigDecimal.ONE.add(periodicInterestRate), 10, RoundingMode.HALF_UP);
    }

    //    an / än
    public static BigDecimal determineAnnuity(Timing timing, Periodicity periodicity, BigDecimal periodicInterestRate, int numberOfPayments) {
        BigDecimal divisor = timing == Timing.IMMEDIATE ? determineAnnualEffectiveDiscountRate(periodicInterestRate) : periodicInterestRate;
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.divide((periodicInterestRate.add(BigDecimal.ONE)).pow(numberOfPayments), 10, RoundingMode.HALF_UP));
        return denominator.divide(divisor, 10, RoundingMode.HALF_UP);
    }

}
