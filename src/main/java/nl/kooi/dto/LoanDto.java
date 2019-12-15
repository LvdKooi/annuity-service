package nl.kooi.dto;

import lombok.Data;
import nl.kooi.domain.Loan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanDto implements Serializable {
    public BigDecimal initialLoan;
    public BigDecimal annualInterestPercentage;
    public Periodicity periodicity;
    public LocalDate startDate;
    public LocalDate endDate;
    public Timing timing;

    public Loan toDomain(){
        Loan.Builder loanBuilder = new Loan.Builder();
        return  loanBuilder.setInitialLoan(initialLoan)
                        .setAnnualInterestPercentage(annualInterestPercentage)
                        .setPeriodicity(periodicity)
                        .setStartdate(startDate)
                        .setEnddate(endDate)
                        .setTiming(timing)
                        .build();

    }

    }
