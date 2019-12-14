package nl.kooi.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PeriodicPaymentDto implements Serializable {
    public Integer period;
    public BigDecimal totalPayment;
    public BigDecimal interestAmount;
    public BigDecimal repaymentAmount;

}
