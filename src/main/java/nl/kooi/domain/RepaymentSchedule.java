package nl.kooi.domain;

import nl.kooi.dto.LoanStatementDto;
import nl.kooi.dto.RepaymentScheduleDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepaymentSchedule {
    private Loan loan;
    private List<LoanStatement> loanStatements;

    public RepaymentSchedule(Loan loan) {
        this.loan = loan;
        setPaymentList(loan);
    }

    public List<LoanStatement> getLoanStatements() {
        return loanStatements;
    }

    public Loan getLoan(){
        return this.loan;
    }

    private void setPaymentList(Loan loan) {
        loanStatements = new ArrayList<>();
        int numberOfPayments = LoanStatement.of(loan, 1).getNumberOfPayments();

        for (int i = 1; i <= numberOfPayments; i++) {
            loanStatements.add(LoanStatement.of(loan, i));
        }
    }

    public RepaymentScheduleDto toDto() {
        RepaymentScheduleDto dto = new RepaymentScheduleDto();
        List<LoanStatementDto> loanStatementDtoList = loanStatements.stream().map(LoanStatement::toDto).collect(Collectors.toList());

        dto.loan = loan.toDto();
        dto.loanStatements = loanStatementDtoList;

        return dto;

    }
}