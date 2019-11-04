package nl.kooi.domain;

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


    public Loan(BigDecimal initialLoan,
                BigDecimal annualInterestPercentage,
                Periodicity periodicity,
                Timing timing,
                LocalDate startdate,
                Period loanPeriod) {

        this.initialLoan = initialLoan;
        this.annualInterestPercentage = annualInterestPercentage;
        this.periodicity = periodicity;
        this.timing = timing;
             this.startDate = startdate;
        this.loanPeriod = loanPeriod;
        this.endDate = startdate.plus(loanPeriod);

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

    public Period getLoanPeriod(){
        return loanPeriod;
    }

}
