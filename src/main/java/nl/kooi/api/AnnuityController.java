package nl.kooi.api;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.api.dto.LoanDto;
import nl.kooi.api.dto.Mapper;
import nl.kooi.api.dto.RepaymentScheduleDto;
import nl.kooi.domain.Loan;
import nl.kooi.domain.Periodicity;
import nl.kooi.domain.RepaymentSchedule;
import nl.kooi.domain.Timing;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


/**
 * Created by Laurens van der Kooi on 10/10/19.
 */

@RequestMapping(path = "/annuity")
@RestController
@Validated
@Slf4j
public class AnnuityController {

    @PostMapping(path = "/repayment-schedule", produces = "application/json")
    @ApiOperation("Generates a repayment schedule for an annuity loan.")
    public RepaymentScheduleDto getRepaymentSchedule(@RequestBody LoanDto loan, @RequestParam int months) {
        var loanObject = Loan.builder()
                .initialLoan(loan.getInitialLoan())
                .annualInterestPercentage(loan.getAnnualInterestPercentage())
                .periodicity(loan.getPeriodicity())
                .timing(loan.getTiming())
                .startdate(loan.getStartDate())
                .months(months)
                .build();

        return Mapper.map(new RepaymentSchedule(loanObject));
    }
}
