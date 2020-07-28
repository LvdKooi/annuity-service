package nl.kooi.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PaymentDto implements Serializable {
    public BigDecimal totalPayment;
    public BigDecimal interestAmount;
    public BigDecimal repaymentAmount;
}
