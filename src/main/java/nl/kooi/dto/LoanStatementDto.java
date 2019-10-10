package nl.kooi.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementDto {
    LocalDate statementDate;
    BigDecimal openingBalance;
    PeriodicPaymentDto payment;
    BigDecimal closingBalance;
}
