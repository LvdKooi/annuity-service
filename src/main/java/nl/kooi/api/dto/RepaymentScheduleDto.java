package nl.kooi.api.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RepaymentScheduleDto implements Serializable {
   private LoanDto loan;
   private List<LoanStatementDto> loanStatements;
}
