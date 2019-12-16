package nl.kooi.domain;

import nl.kooi.dto.LoanDto;
import nl.kooi.dto.Periodicity;
import nl.kooi.dto.Timing;

import java.math.BigDecimal;
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
        this.endDate = enddate.plusDays(1);
        this.loanPeriod = Period.between(startdate, endDate);

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

    public LoanDto toDto() {
        LoanDto dto = new LoanDto();

        dto.annualInterestPercentage = this.annualInterestPercentage;
        dto.initialLoan = this.initialLoan;
        dto.periodicity = this.periodicity;
        dto.timing = this.timing;

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
            this.initialLoan = initialLoan;
            return this;

        }

        public Builder setAnnualInterestPercentage(BigDecimal annualInterestPercentage) {
            this.annualInterestPercentage = annualInterestPercentage;
            return this;
        }

        public Builder setPeriodicity(Periodicity periodicity) {
            this.periodicity = periodicity;
            return this;
        }

        public Builder setTiming(Timing timing) {
            this.timing = timing;
            return this;
        }

        public Builder setStartdate(LocalDate startdate) {
            this.startdate = startdate;
            return this;
        }

        public Builder setEnddate(LocalDate enddate) {
            this.enddate = enddate;
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
    }
}
