package nl.kooi.domain;

import nl.kooi.dto.LoanDto;
import nl.kooi.dto.Periodicity;
import nl.kooi.dto.Timing;
import nl.kooi.exception.InvalidDateException;
import nl.kooi.exception.LoanException;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

public class Loan {
    private BigDecimal initialLoan;
    private BigDecimal annualInterestPercentage;
    private Periodicity periodicity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Period loanPeriod;
    private Timing timing;

    private Loan(BigDecimal initialLoan,
                 BigDecimal annualInterestPercentage,
                 Periodicity periodicity,
                 Timing timing,
                 LocalDate startdate,
                 LocalDate enddate) {

        this.initialLoan = initialLoan;
        this.annualInterestPercentage = annualInterestPercentage;
        this.periodicity = periodicity;
        this.timing = timing;
        this.startDate = startdate;
        this.endDate = enddate;
        this.loanPeriod = setLoanPeriod(startdate, enddate);
    }

    public BigDecimal getInitialLoan() {
        return initialLoan;
    }

    public BigDecimal getAnnualInterestPercentage() {
        return annualInterestPercentage;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public Timing getTiming() {
        return timing;
    }

    public Period getLoanPeriod() {
        return loanPeriod;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    private Period setLoanPeriod(LocalDate startdate, LocalDate enddate) {
        if (enddate.isBefore(startdate)) {
            throw new LoanException("The enddate of a loan can't be before the startdate.");
        }

        if (Period.between(startdate, endDate.plusDays(1)).toTotalMonths() == 0) {
            throw new LoanException("The loan duration should be at least 1 month.");
        }

        if (Period.between(startdate, endDate.plusDays(1)).toTotalMonths() % (12 / periodicity.getDivisor()) != 0) {
            throw new LoanException("The periodicity doesn't match the loan duration (startdate: " + startdate + " , enddate: " + enddate+")");
        }

        return Period.between(startdate, endDate.plusDays(1));
    }

    public LoanDto toDto() {
        LoanDto dto = new LoanDto();

        dto.annualInterestPercentage = this.annualInterestPercentage;
        dto.initialLoan = this.initialLoan;
        dto.periodicity = this.periodicity;
        dto.timing = this.timing;
        dto.startDate = this.startDate;
        dto.endDate = this.endDate;

        return dto;
    }

    public static class Builder {
        private BigDecimal initialLoan;
        private BigDecimal annualInterestPercentage;
        private Periodicity periodicity;
        private Timing timing;
        private LocalDate startdate;
        private LocalDate enddate;

        public Builder() {

        }

        public Builder setInitialLoan(BigDecimal initialLoan) {
            if (initialLoan.compareTo(BigDecimal.ZERO) < 1) {
                throw new LoanException("A loan should be larger than 0.");
            }

            this.initialLoan = initialLoan;
            return this;

        }

        public Builder setAnnualInterestPercentage(BigDecimal annualInterestPercentage) {
            if (annualInterestPercentage.compareTo(BigDecimal.ZERO) <= 0) {
                throw new LoanException("The annual interest on a loan can't be equal or smaller than 0.");
            }

            this.annualInterestPercentage = annualInterestPercentage;
            return this;
        }

        public Builder setPeriodicity(Periodicity periodicity) {
            try {
                this.periodicity = periodicity;
                return this;
            } catch (IllegalArgumentException e) {
                throw new LoanException("Periodicity should contain MONTHLY, QUARTERLY, SEMI_ANNUALLY or ANNUALLY");
            }
        }

        public Builder setPeriodicity(String periodicity) {
            try {
                return setPeriodicity(Periodicity.valueOf(periodicity.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new LoanException("Periodicity should contain MONTHLY, QUARTERLY, SEMI_ANNUALLY or ANNUALLY");
            }
        }

        public Builder setTiming(Timing timing) {
            try {
                this.timing = timing;
                return this;
            } catch (IllegalArgumentException e) {
                throw new LoanException("Timing should be either IMMEDIATE or DUE.");
            }
        }


        public Builder setTiming(String timing) {
            try {
                return setTiming(Timing.valueOf(timing.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new LoanException("Timing should be either IMMEDIATE or DUE.");
            }
        }

        public Builder setStartdate(LocalDate startdate) {
            this.startdate = startdate;
            return this;
        }

        public Builder setEnddate(LocalDate enddate) {
            this.enddate = enddate;
            return this;
        }

        public Builder setStartdate(String startdate) {
            this.startdate = dateFromStringConverter(startdate);
            return this;
        }

        public Builder setEnddate(String enddate) {
            this.enddate = dateFromStringConverter(enddate);
            return this;
        }

        public Loan build() {
            return new Loan(initialLoan,
                    annualInterestPercentage,
                    periodicity,
                    timing,
                    startdate,
                    enddate);
        }


        private LocalDate dateFromStringConverter(String date) {

            try {
                if (date.length() != 10) {
                    throw new InvalidDateException("A datestring should contain 10 characters i.e. 2019-01-01.");
                }
                return LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8)));
            } catch (DateTimeException e) {
                throw new InvalidDateException("Invalid date format. A date should look like this: yyyy-mm-dd. For example: 2019-01-01");
            }

        }
    }
}
