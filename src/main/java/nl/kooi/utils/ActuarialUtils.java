package nl.kooi.utils;

import lombok.experimental.UtilityClass;
import nl.kooi.domain.Periodicity;
import nl.kooi.domain.Timing;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class ActuarialUtils {

    //    i
    public BigDecimal getPeriodicInterestRate(BigDecimal annualInterestFraction, Periodicity periodicity) {
        var periodInterestPlusOne = Math.pow((BigDecimal.ONE.add(annualInterestFraction)).setScale(10, RoundingMode.HALF_UP).doubleValue(),
                1 / (double) periodicity.getDivisor());
        return BigDecimal.valueOf(periodInterestPlusOne - 1);
    }

    //    d
    public BigDecimal getPeriodicEffectiveDiscountRate(BigDecimal periodicInterestRate) {
        return periodicInterestRate.divide(BigDecimal.ONE.add(periodicInterestRate), 10, RoundingMode.HALF_UP);
    }

    //    an / än
    public BigDecimal getAnnuity(Timing timing, BigDecimal periodicInterestRate, int numberOfPayments) {
        var divisor = timing == Timing.DUE ? getPeriodicEffectiveDiscountRate(periodicInterestRate) : periodicInterestRate;
        var denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.divide((periodicInterestRate.add(BigDecimal.ONE)).pow(numberOfPayments), 10, RoundingMode.HALF_UP));
        return denominator.divide(divisor, 10, RoundingMode.HALF_UP);
    }

}
