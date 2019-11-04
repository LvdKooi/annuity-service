package nl.kooi.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Data
public class LoanDto implements Serializable {
    public BigDecimal initialLoan;
    public BigDecimal annualInterestPercentage;
    public Periodicity periodicity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Period loanPeriod;
    public Timing timing;
    }
