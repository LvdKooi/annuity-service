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
        return modelMapper.map(loan, LoanDto.class);
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
