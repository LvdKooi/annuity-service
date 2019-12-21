package nl.kooi.app;


import lombok.extern.slf4j.Slf4j;
import nl.kooi.domain.Loan;
import nl.kooi.domain.RepaymentSchedule;
import nl.kooi.dto.Periodicity;
import nl.kooi.dto.RepaymentScheduleDto;
import nl.kooi.dto.Timing;
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
@Slf4j

public class AnnuityController {

    @GetMapping(path = "/repaymentschedule", produces = "application/json")
    public RepaymentScheduleDto getRepaymentSchedule(@RequestParam("loan") String loan, @RequestParam("interest") String interest, @RequestParam("periodicity") String periodicity, @RequestParam("timing") String timing, @RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate) {

        Loan loanObject = new Loan.Builder()
                .setInitialLoan(new BigDecimal(loan.trim()))
                .setAnnualInterestPercentage(new BigDecimal(interest.trim()))
                .setPeriodicity(Periodicity.valueOf(periodicity))
                .setTiming(Timing.valueOf(timing))
                .setStartdate(startdate)
                .setEnddate(enddate)
                .build();

        return new RepaymentSchedule(loanObject).toDto();

    }

}
