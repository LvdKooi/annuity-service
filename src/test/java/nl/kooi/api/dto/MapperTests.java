package nl.kooi.api.dto;

import nl.kooi.domain.Loan;
import nl.kooi.domain.LoanStatement;
import nl.kooi.domain.Payment;
import nl.kooi.domain.RepaymentSchedule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static nl.kooi.domain.Periodicity.SEMI_ANNUALLY;
import static nl.kooi.domain.Timing.IMMEDIATE;
import static org.assertj.core.api.Assertions.assertThat;


class MapperTests {

    @Test
    public void loanToDtoTest() {
        LoanDto loanDto = Mapper.map(getLoan());
        LoanDto loanDtoExpectation = getLoanDtoExpectation();
        assertThat(loanDto).isEqualTo(loanDtoExpectation);

    }

    @Test
    public void paymentToDtoTest() {
        PaymentDto paymentDto = Mapper.map(getPayment());
        assertThat(paymentDto).isEqualTo(getPaymentDtoExpectation());
    }


    @Test
    public void repaymentScheduleToDtoTest() {
        var repaymentSchedule = new RepaymentSchedule(getLoan(), List.of(LoanStatement.of(getLoan(), 1), LoanStatement.of(getLoan(), 2)));
        RepaymentScheduleDto repaymentScheduleDto = Mapper.map(repaymentSchedule);
        assertThat(repaymentScheduleDto.getLoan()).isEqualTo(getLoanDtoExpectation());
        assertThat(repaymentScheduleDto.getLoanStatements().size()).isEqualTo(2);
        assertThat(repaymentScheduleDto.getLoanStatements()).isEqualTo(getLoanStatementDtosExpectation());
    }

    private Payment getPayment() {
        return new Payment(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.valueOf(9));

    }

    private PaymentDto getPaymentDtoExpectation() {
        var dto = new PaymentDto();
        dto.setTotalPayment(BigDecimal.TEN.setScale(5, RoundingMode.HALF_UP));
        dto.setInterestAmount(BigDecimal.ONE.setScale(5, RoundingMode.HALF_UP));
        dto.setRepaymentAmount(BigDecimal.valueOf(9).setScale(5, RoundingMode.HALF_UP));
        return dto;
    }

    private List<LoanStatementDto> getLoanStatementDtosExpectation() {
        return List.of(Mapper.map(LoanStatement.of(getLoan(), 1)), Mapper.map(LoanStatement.of(getLoan(), 2)));
    }


    private Loan getLoan() {
        return Loan.builder()
                .initialLoan(BigDecimal.valueOf(1000.0))
                .annualInterestPercentage(BigDecimal.ONE)
                .loanTerm(Period.ofMonths(12))
                .periodicity(SEMI_ANNUALLY)
                .startdate(LocalDate.parse("2019-01-01"))
                .timing(IMMEDIATE)
                .build();
    }

    private LoanDto getLoanDtoExpectation() {
        LoanDto loanDtoExpectation = new LoanDto();
        loanDtoExpectation.setPeriodicity(SEMI_ANNUALLY);
        loanDtoExpectation.setInitialLoan(BigDecimal.valueOf(1000.0).setScale(5, RoundingMode.HALF_UP));
        loanDtoExpectation.setAnnualInterestPercentage(BigDecimal.ONE.setScale(5, RoundingMode.HALF_UP));
        loanDtoExpectation.setStartDate(LocalDate.of(2019, 1, 1));
        loanDtoExpectation.setEndDate(LocalDate.of(2019, 12, 31));
        loanDtoExpectation.setTiming(IMMEDIATE);
        loanDtoExpectation.setLoanTermInMonths(12);

        return loanDtoExpectation;
    }
}