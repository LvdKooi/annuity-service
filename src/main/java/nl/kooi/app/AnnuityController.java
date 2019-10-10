package nl.kooi.app;

import lombok.extern.slf4j.Slf4j;
import nl.kooi.dto.RepaymentScheduleDto;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Laurens van der Kooi on 10/10/19.
 */

@RequestMapping(path = "/annuity")
@RestController
@Slf4j

public class AnnuityController {

    @RequestMapping(path = "/repaymentschedule", method = GET, produces = "application/json")
    public RepaymentScheduleDto getRepaymentSchedule() {
        return new RepaymentScheduleDto();
    }

}
