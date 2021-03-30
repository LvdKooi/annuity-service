package nl.kooi.domain.validation;

import nl.kooi.domain.Loan;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PeriodMatchesPeriodicityValidator implements ConstraintValidator<PeriodMatchesPeriodicity, Loan> {

    @Override
    public boolean isValid(Loan loan, ConstraintValidatorContext constraintValidatorContext) {
        var loanTerm = loan.getLoanTerm();

        return loanTerm != null &&
                loan.getPeriodicity() != null &&
                loanTerm.getMonths() > 0 &&
                loanTerm.getMonths() % (12 / loan.getPeriodicity().getDivisor()) == 0;
    }
}
