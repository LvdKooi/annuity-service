package nl.kooi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RepaymentScheduleDto implements Serializable {
   public LoanDto loan;
   public List<PeriodicPaymentDto> periodicPaymentList;
}
