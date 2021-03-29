package nl.kooi.domain;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class RepaymentScheduleService {

    public RepaymentSchedule getRepaymentScheduleForLoan(@Valid Loan loan){
        return new RepaymentSchedule(loan, getLoanStatementsForLoan(loan));
    }

    private List<LoanStatement> getLoanStatementsForLoan(Loan loan){

        var loanStatements = new ArrayList<LoanStatement>();
        var numberOfPayments = LoanStatement.of(loan, 1).getNumberOfPayments();

        for (var i = 1; i <= numberOfPayments; i++) {
            loanStatements.add(LoanStatement.of(loan, i));
        }
        return loanStatements;
    }
}
