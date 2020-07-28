package nl.kooi.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import nl.kooi.domain.Loan;
import nl.kooi.domain.Periodicity;
import nl.kooi.domain.Timing;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Data
public class LoanDto implements Serializable {
    @Positive
    public BigDecimal initialLoan;
    @Positive
    public BigDecimal annualInterestPercentage;
    @NotNull
    public Periodicity periodicity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    public LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate endDate;

    @NotNull
    public Timing timing;

}
