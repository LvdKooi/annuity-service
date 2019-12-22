package nl.kooi.app;


import lombok.extern.slf4j.Slf4j;
import nl.kooi.domain.Loan;
import nl.kooi.domain.RepaymentSchedule;
import nl.kooi.dto.RepaymentScheduleDto;
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


    @GetMapping(path = "/repaymentschedule", produces = "application/json")
    public RepaymentScheduleDto getRepaymentSchedule(@NonNull @RequestParam("loan") String loan,
                                                     @NonNull @RequestParam("interest") String interest,
                                                     @NonNull @RequestParam("periodicity") String periodicity,
                                                     @NonNull @RequestParam("timing") String timing,
                                                     @NonNull @RequestParam("startdate") String startdate,
                                                     @NonNull @RequestParam("enddate") String enddate) {

        Loan loanObject = new Loan.Builder()
                .setInitialLoan(new BigDecimal(loan.trim()))
                .setAnnualInterestPercentage(new BigDecimal(interest.trim()))
                .setPeriodicity(periodicity)
                .setTiming(timing)
                .setStartdate(startdate)
                .setEnddate(enddate)
                .build();

        return new RepaymentSchedule(loanObject).toDto();

    }

}
