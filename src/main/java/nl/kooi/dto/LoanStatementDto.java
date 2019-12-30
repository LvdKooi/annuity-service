package nl.kooi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementDto implements Serializable {
    public Integer period;

    @JsonFormat(pattern="yyyy-MM-dd")
    public LocalDate date;
    public PaymentDto payment;
    public BigDecimal balance;
    public BigDecimal totalInterest;

}
