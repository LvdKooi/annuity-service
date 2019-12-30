package nl.kooi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    public BigDecimal totalPayment;
    public BigDecimal interestAmount;
    public BigDecimal repaymentAmount;
}
