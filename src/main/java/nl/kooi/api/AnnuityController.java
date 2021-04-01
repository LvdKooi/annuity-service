package nl.kooi.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.api.dto.LoanDto;
import nl.kooi.api.dto.Mapper;
import nl.kooi.api.dto.RepaymentScheduleDto;
import nl.kooi.domain.RepaymentScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * Created by Laurens van der Kooi on 10/10/19.
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class AnnuityController implements nl.kooi.api.controller.AnnuityApi {

    private final RepaymentScheduleService repaymentScheduleService;

    @Override
    public ResponseEntity<RepaymentScheduleDto> getRepaymentSchedule(@Valid LoanDto loan) {
        return ResponseEntity.ok(Mapper.map(repaymentScheduleService.getRepaymentScheduleForLoan(Mapper.map(loan))));
    }
}
