package nl.kooi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import nl.kooi.domain.Loan;
import nl.kooi.domain.Periodicity;
import nl.kooi.domain.Timing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Data
public class LoanDto implements Serializable {
    public BigDecimal initialLoan;
    public BigDecimal annualInterestPercentage;
    public Periodicity periodicity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate endDate;
    public Timing timing;

    public Loan toDomain() {
        Loan.Builder loanBuilder = new Loan.Builder();
        return loanBuilder.setInitialLoan(initialLoan)
                .setAnnualInterestPercentage(annualInterestPercentage)
                .setPeriodicity(periodicity)
                .setStartdate(startDate)
                .setMonths((int) Period.between(startDate, endDate.plusDays(1)).toTotalMonths())
                .setTiming(timing)
                .build();

    }

}
