package nl.kooi.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LoanDto implements Serializable {
    public BigDecimal initialLoan;
    public BigDecimal interestPercentage;
    public Periodicity periodicity;
    public Timing timing;
    }
