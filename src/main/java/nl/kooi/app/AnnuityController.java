package nl.kooi.app;

import lombok.extern.slf4j.Slf4j;
import nl.kooi.dto.LoanDto;
import nl.kooi.dto.Periodicity;
import nl.kooi.dto.RepaymentScheduleDto;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Laurens van der Kooi on 10/10/19.
 */

@RequestMapping(path = "/annuity")
@RestController
@Slf4j

public class AnnuityController {

    @GetMapping(path = "/repaymentschedule", produces = "application/json")
    public RepaymentScheduleDto getRepaymentSchedule(@RequestParam("loan") String loan, @RequestParam("interest") String interest, @RequestParam("periodicity") String periodicity) {

        LoanDto loanDto = new LoanDto();

        loanDto.initialLoan = new BigDecimal(loan);
        loanDto.interestPercentage = new BigDecimal(interest);
        loanDto.periodicity = Periodicity.valueOf(periodicity);




        return new RepaymentScheduleDto();

    }

}
