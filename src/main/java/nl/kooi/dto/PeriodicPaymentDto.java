package nl.kooi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PeriodicPaymentDto implements Serializable {
    public Integer period;

    @JsonFormat(pattern="yyyy-MM-dd")
    public LocalDate date;
    public BigDecimal totalPayment;
    public BigDecimal interestAmount;
    public BigDecimal repaymentAmount;
}
