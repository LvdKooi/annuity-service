package nl.kooi.api.dto;


import nl.kooi.domain.*;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;

public class Mapper {

    private static final ModelMapper modelMapper = new ModelMapper();
    private static final Converter<BigDecimal, BigDecimal> toScale = new AbstractConverter<>() {
        protected BigDecimal convert(BigDecimal source) {
            return source == null ? null : source.setScale(5, RoundingMode.HALF_UP);
        }
    };

    static {
        modelMapper.addConverter(toScale);
    }

    public static LoanDto map(Loan loan) {
        return modelMapper.map(loan, LoanDto.class);
    }

    public static Loan map(LoanDto loanDto) {
        return Loan.builder()
                .initialLoan(loanDto.getInitialLoan())
                .annualInterestPercentage(loanDto.getAnnualInterestPercentage())
                .periodicity(loanDto.getPeriodicity())
                .timing(loanDto.getTiming())
                .startdate(loanDto.getStartDate())
                .loanTerm(Period.ofMonths(loanDto.getLoanTermInMonths()))
                .repaymentType(loanDto.getRepaymentType())
                .build();
    }

    public static LoanStatementDto map(LoanStatement loanStatement) {
        return modelMapper.map(loanStatement, LoanStatementDto.class);
    }

    public static RepaymentScheduleDto map(RepaymentSchedule repaymentSchedule) {
        return modelMapper.map(repaymentSchedule, RepaymentScheduleDto.class);
    }

    public static PaymentDto map(Payment payment) {
        return modelMapper.map(payment, PaymentDto.class);
    }

}
