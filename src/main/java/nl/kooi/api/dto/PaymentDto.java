package nl.kooi.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PaymentDto implements Serializable {
    private BigDecimal totalPayment;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;
}
