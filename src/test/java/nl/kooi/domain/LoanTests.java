package nl.kooi.domain;

import nl.kooi.dto.LoanDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;

public class LoanTests {

    @Test
    public void loanToDto() {
        LoanDto loanDto = getLoan().toDto();
        LoanDto loanDtoExpectation = getLoanDtoExpectation();

        assertThat("Dto generated from Loan object doesn't match expectation", loanDto.equals(loanDtoExpectation));

    }

    @Test
    public void loanDtoToDomain() {
        Loan loan = getLoan().toDto().toDomain();

        assertThat("Domain object generated from LoanDto object doesn't match expectation", loan.equals(getLoan()));

    }

    private Loan getLoan() {
        return new Loan.Builder()
                .setInitialLoan(1000.0)
                .setAnnualInterestPercentage(1.0)
                .setMonths(120)
                .setPeriodicity("MONTHLY")
                .setStartdate("2019-01-01")
                .setTiming("IMMEDIATE")
                .build();
    }

    private LoanDto getLoanDtoExpectation() {
        LoanDto loanDtoExpectation = new LoanDto();
        loanDtoExpectation.periodicity = Periodicity.MONTHLY;
        loanDtoExpectation.initialLoan = BigDecimal.valueOf(1000.0);
        loanDtoExpectation.annualInterestPercentage = BigDecimal.valueOf(1.0);
        loanDtoExpectation.startDate = LocalDate.of(2019, 1, 1);
        loanDtoExpectation.endDate = LocalDate.of(2028, 12, 31);
        loanDtoExpectation.timing = Timing.IMMEDIATE;

        return loanDtoExpectation;
    }
}
