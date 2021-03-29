package nl.kooi.domain;


import nl.kooi.exception.LoanException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import static nl.kooi.domain.Periodicity.SEMI_ANNUALLY;
import static nl.kooi.domain.Timing.IMMEDIATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoanTests {


    @Test
    public void incompleteLoanObjectTest() {
        assertThrows(NullPointerException.class, () -> Loan.builder().startdate(LocalDate.parse("2017-04-01")).build());
    }

    @Test
    public void endDateAndLoanTermTest() {
        var loan = Loan.builder()
                .initialLoan(BigDecimal.valueOf(1000.0))
                .annualInterestPercentage(BigDecimal.ONE)
                .loanTerm(Period.ofMonths(12))
                .periodicity(SEMI_ANNUALLY)
                .startdate(LocalDate.parse("2019-01-01"))
                .timing(IMMEDIATE)
                .build();

        assertThat(loan.getEndDate()).isEqualTo(LocalDate.of(2019, 12, 31));
        assertThat(loan.getLoanTerm()).isEqualTo(Period.ofMonths(12));
    }

}
