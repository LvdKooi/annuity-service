package nl.kooi.api.dto;


import nl.kooi.domain.Loan;
import nl.kooi.domain.LoanStatement;
import nl.kooi.domain.Payment;
import nl.kooi.domain.RepaymentSchedule;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;

public class Mapper {

    private static ModelMapper modelMapper = new ModelMapper();

    private static Converter<BigDecimal, BigDecimal> toScale = new AbstractConverter<BigDecimal, BigDecimal>() {
        protected BigDecimal convert(BigDecimal source) {
            return source == null ? null : source.setScale(5, RoundingMode.HALF_UP);
        }
    };

    static {
        modelMapper.addConverter(toScale);
    }

    public static LoanDto map(Loan loan) {
        var mapped = modelMapper.map(loan, LoanDto.class);
        mapped.setLoanTermInMonths(loan.getLoanTerm().getMonths());
        return mapped;
    }

    public static Loan map(LoanDto loanDto) {
        return Loan.builder()
                .initialLoan(loanDto.getInitialLoan())
                .annualInterestPercentage(loanDto.getAnnualInterestPercentage())
                .periodicity(loanDto.getPeriodicity())
                .timing(loanDto.getTiming())
                .startdate(loanDto.getStartDate())
                .loanTerm(Period.ofMonths(loanDto.getLoanTermInMonths()))
                .build();
    }

    public static LoanStatementDto map(LoanStatement loanStatement) {
        return modelMapper.map(loanStatement, LoanStatementDto.class);
    }

    public static RepaymentScheduleDto map(RepaymentSchedule repaymentSchedule) {
        var mapped =  modelMapper.map(repaymentSchedule, RepaymentScheduleDto.class);
        mapped.setLoan(Mapper.map(repaymentSchedule.getLoan()));
        return mapped;
    }

    public static PaymentDto map(Payment payment) {
        return modelMapper.map(payment, PaymentDto.class);
    }

}
