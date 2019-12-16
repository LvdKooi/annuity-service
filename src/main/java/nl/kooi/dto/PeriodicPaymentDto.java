package nl.kooi.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PeriodicPaymentDto implements Serializable {
    public Integer period;
    public LocalDate date;
    public BigDecimal totalPayment;
    public BigDecimal interestAmount;
    public BigDecimal repaymentAmount;
}
