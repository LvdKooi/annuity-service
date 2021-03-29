package nl.kooi.api;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.api.dto.LoanDto;
import nl.kooi.api.dto.Mapper;
import nl.kooi.api.dto.RepaymentScheduleDto;
import nl.kooi.domain.RepaymentScheduleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Laurens van der Kooi on 10/10/19.
 */

@RequestMapping(path = "/annuity")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AnnuityController {

    private final RepaymentScheduleService repaymentScheduleService;

    @PostMapping(path = "/repayment-schedule")
    @ApiOperation("Generates a repayment schedule for an annuity loan.")
    public RepaymentScheduleDto getRepaymentSchedule(@RequestBody LoanDto loan) {
        return Mapper.map(repaymentScheduleService.getRepaymentScheduleForLoan(Mapper.map(loan)));
    }
}
