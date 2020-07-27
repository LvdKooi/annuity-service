package nl.kooi.domain;

import nl.kooi.dto.LoanDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static nl.kooi.domain.Periodicity.MONTHLY;
import static nl.kooi.domain.Timing.IMMEDIATE;
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
        return Loan.builder()
                .initialLoan(BigDecimal.valueOf(1000.0))
                .annualInterestPercentage(BigDecimal.ONE)
                .months(120)
                .periodicity(MONTHLY)
                .startdate(LocalDate.parse("2019-01-01"))
                .timing(IMMEDIATE)
                .build();
    }

    private LoanDto getLoanDtoExpectation() {
        LoanDto loanDtoExpectation = new LoanDto();
        loanDtoExpectation.periodicity = MONTHLY;
        loanDtoExpectation.initialLoan = BigDecimal.valueOf(1000.0);
        loanDtoExpectation.annualInterestPercentage = BigDecimal.ONE;
        loanDtoExpectation.startDate = LocalDate.of(2019, 1, 1);
        loanDtoExpectation.endDate = LocalDate.of(2028, 12, 31);
        loanDtoExpectation.timing = IMMEDIATE;

        return loanDtoExpectation;
    }
}
