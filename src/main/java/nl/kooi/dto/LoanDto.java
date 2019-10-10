package nl.kooi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanDto {
    BigDecimal initialLoan;
    BigDecimal interestPercentage;
    Periodicity periodicity;
}
