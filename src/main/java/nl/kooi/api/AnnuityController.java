package nl.kooi.api;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.api.dto.Mapper;
import nl.kooi.domain.Loan;
import nl.kooi.domain.Periodicity;
import nl.kooi.domain.RepaymentSchedule;
import nl.kooi.domain.Timing;
import nl.kooi.api.dto.RepaymentScheduleDto;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


/**
 * Created by Laurens van der Kooi on 10/10/19.
 */

@RequestMapping(path = "/annuity")
@RestController
@Validated
@Slf4j

public class AnnuityController {

    @GetMapping(path = "/repayment-schedule", produces = "application/json")
    @ApiOperation("Generates a repayment schedule for an annuity loan.")
    public RepaymentScheduleDto getRepaymentSchedule(@NonNull @RequestParam("loan") String loan,
                                                     @NonNull @RequestParam("interest") String interest,
                                                     @NonNull @RequestParam("periodicity") Periodicity periodicity,
                                                     @NonNull @RequestParam("timing") Timing timing,
                                                     @NonNull @RequestParam("start-date") String startdate,
                                                     @NonNull @RequestParam("loan-term-in-months") int months) {



        var loanObject = Loan.builder()
                .initialLoan(new BigDecimal(loan.trim()))
                .annualInterestPercentage(new BigDecimal(interest.trim()))
                .periodicity(periodicity)
                .timing(timing)
                .startdate(startdate)
                .months(months)
                .build();

        return Mapper.map(new RepaymentSchedule(loanObject));

    }

}
