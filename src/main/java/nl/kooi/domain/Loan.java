package nl.kooi.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import nl.kooi.exception.InvalidDateException;
import nl.kooi.exception.LoanException;
import nl.kooi.utils.ActuarialUtils;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

@EqualsAndHashCode
@Getter
public class Loan {
    private final @Positive BigDecimal initialLoan;
    private final @Positive BigDecimal annualInterestPercentage;
    private final BigDecimal annualInterestRate;
    private final BigDecimal periodicInterestRate;
    private final Periodicity periodicity;
    private final LocalDate startDate;
    private final Timing timing;
    private LocalDate endDate;
    private Period loanTerm;

    private Loan(@NonNull BigDecimal initialLoan,
                 @NonNull BigDecimal annualInterestPercentage,
                 @NonNull Periodicity periodicity,
                 @NonNull Timing timing,
                 @NonNull LocalDate startdate,
                 @NonNull Integer months) {

        this.initialLoan = initialLoan;
        this.annualInterestPercentage = annualInterestPercentage;
        this.annualInterestRate = annualInterestPercentage.divide(new BigDecimal(100), 10, RoundingMode.HALF_UP);
        this.periodicInterestRate = ActuarialUtils.getPeriodicInterestRate(annualInterestRate, periodicity);
        this.periodicity = periodicity;
        this.timing = timing;
        this.startDate = startdate;
        setLoanPeriodAndEnddate(startdate, months);
    }

    public static LoanBuilder builder() {
        return new LoanBuilder();
    }

    private void setLoanPeriodAndEnddate(LocalDate startdate, int months) {

        if (months <= 0) {
            throw new LoanException("The loan period can't be 0 months or less.");
        }

        int modulus = months % (12 / periodicity.getDivisor());

        if (modulus != 0) {
            throw new LoanException("The loan period doesn't match the periodicity. Valid months within the range of your input are: " +
                    (months - modulus) +
                    " and " + (months + ((12 / periodicity.getDivisor()) - modulus)));
        }

        endDate = startdate.plusMonths(months).minusDays(1);
        loanTerm = Period.ofMonths(months);

    }

    public static class LoanBuilder {
        private BigDecimal initialLoan;
        private BigDecimal annualInterestPercentage;
        private Periodicity periodicity;
        private Timing timing;
        private LocalDate startdate;
        private Integer months;

        LoanBuilder() {
        }

        public LoanBuilder initialLoan(BigDecimal initialLoan) {
            if (initialLoan.compareTo(BigDecimal.ZERO) < 1) {
                throw new LoanException("A loan should be larger than 0.");
            }
            this.initialLoan = initialLoan;
            return this;
        }

        public LoanBuilder annualInterestPercentage(BigDecimal annualInterestPercentage) {
            if (annualInterestPercentage.compareTo(BigDecimal.ZERO) <= 0) {
                throw new LoanException("The annual interest on a loan can't be equal or smaller than 0.");
            }
            this.annualInterestPercentage = annualInterestPercentage;
            return this;
        }

        public LoanBuilder periodicity(Periodicity periodicity) {
            this.periodicity = periodicity;
            return this;
        }

        public LoanBuilder timing(Timing timing) {
            this.timing = timing;
            return this;
        }

        public LoanBuilder startdate(LocalDate startdate) {
            this.startdate = startdate;
            return this;
        }

        public LoanBuilder startdate(String startdate) {
            return startdate(dateFromStringConverter(startdate));
        }

        public LoanBuilder months(Integer months) {
            this.months = months;
            return this;
        }

        public Loan build() {
            return new Loan(initialLoan, annualInterestPercentage, periodicity, timing, startdate, months);
        }

        public String toString() {
            return "Loan.LoanBuilder(initialLoan=" + this.initialLoan + ", annualInterestPercentage=" + this.annualInterestPercentage + ", periodicity=" + this.periodicity + ", timing=" + this.timing + ", startdate=" + this.startdate + ", months=" + this.months + ")";
        }

        private LocalDate dateFromStringConverter(String date) {

            try {
                if (date.length() != 10) {
                    throw new InvalidDateException("A datestring should contain 10 characters i.e. 2019-01-01.");
                }
                return LocalDate.parse(date);
            } catch (DateTimeException e) {
                throw new InvalidDateException("Invalid date format. A date should look like this: yyyy-mm-dd. For example: 2019-01-01");
            }

        }
    }
}
