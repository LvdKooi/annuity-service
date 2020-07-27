package nl.kooi.domain;

import lombok.Getter;
import nl.kooi.dto.LoanStatementDto;
import nl.kooi.dto.RepaymentScheduleDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RepaymentSchedule {
    private final Loan loan;
    private List<LoanStatement> loanStatements;

    public RepaymentSchedule(Loan loan) {
        this.loan = loan;
        setPaymentList(loan);
    }


    private void setPaymentList(Loan loan) {
        loanStatements = new ArrayList<>();
        var numberOfPayments = LoanStatement.of(loan, 1).getNumberOfPayments();

        for (var i = 1; i <= numberOfPayments; i++) {
            loanStatements.add(LoanStatement.of(loan, i));
        }
    }

    public RepaymentScheduleDto toDto() {
        var dto = new RepaymentScheduleDto();
        var loanStatementDtoList = loanStatements.stream().map(LoanStatement::toDto).collect(Collectors.toList());

        dto.loan = loan.toDto();
        dto.loanStatements = loanStatementDtoList;

        return dto;

    }
}