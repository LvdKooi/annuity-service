package nl.kooi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PeriodicPaymentDto {
    BigDecimal totalPayment;
    BigDecimal interestAmount;
    BigDecimal repaymentAmount;
}
