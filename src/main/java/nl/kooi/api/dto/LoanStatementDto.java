package nl.kooi.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementDto implements Serializable {
    private Integer period;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    private PaymentDto payment;
    private BigDecimal balance;
    private BigDecimal totalInterest;

}
