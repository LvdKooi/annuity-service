package nl.kooi.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class RepaymentSchedule {
    private final Loan loan;
    private final List<LoanStatement> loanStatements;
}