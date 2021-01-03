package nl.kooi.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import nl.kooi.domain.Periodicity;
import nl.kooi.domain.Timing;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanDto implements Serializable {
    @Positive
    private BigDecimal initialLoan;
    @Positive
    private BigDecimal annualInterestPercentage;
    @NotNull
    private Periodicity periodicity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private Timing timing;

}
