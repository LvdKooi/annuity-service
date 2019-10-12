package nl.kooi.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementDto implements Serializable {
    public LocalDate statementDate;
    public BigDecimal openingBalance;
    public PeriodicPaymentDto payment;
    public BigDecimal closingBalance;
}
