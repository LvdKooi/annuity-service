package nl.kooi.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.kooi.domain.validation.PeriodMatchesPeriodicity;
import nl.kooi.utils.ActuarialUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Validated
@PeriodMatchesPeriodicity
public class Loan {
    private @Positive BigDecimal initialLoan;
    private @Positive BigDecimal annualInterestPercentage;
    private @NotNull BigDecimal annualInterestRate;
    private BigDecimal periodicInterestRate;
    private @NotNull Periodicity periodicity;
    private @NotNull LocalDate startDate;
    private @NotNull Timing timing;
    private LocalDate endDate;
    private @NotNull Period loanTerm;

    @Builder
    private Loan(BigDecimal initialLoan,
                 BigDecimal annualInterestPercentage,
                 Periodicity periodicity,
                 Timing timing,
                 LocalDate startdate,
                 Period loanTerm) {

        this.initialLoan = initialLoan;
        this.annualInterestPercentage = annualInterestPercentage;
        this.annualInterestRate = annualInterestPercentage.divide(new BigDecimal(100), 10, RoundingMode.HALF_UP);
        this.periodicInterestRate = ActuarialUtils.getPeriodicInterestRate(annualInterestRate, periodicity);
        this.periodicity = periodicity;
        this.timing = timing;
        this.startDate = startdate;
        this.loanTerm = loanTerm;
        setLoanPeriodAndEnddate(startdate, loanTerm);
    }

    private void setLoanPeriodAndEnddate(LocalDate startdate, Period loanTerm) {
        var months = loanTerm.getMonths();
        endDate = startdate.plusMonths(months).minusDays(1);
    }

}
