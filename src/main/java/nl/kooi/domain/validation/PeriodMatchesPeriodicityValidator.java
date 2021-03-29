package nl.kooi.domain.validation;

import nl.kooi.domain.Loan;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PeriodMatchesPeriodicityValidator implements ConstraintValidator<PeriodMatchesPeriodicity, Loan> {

    @Override
    public boolean isValid(Loan loan, ConstraintValidatorContext constraintValidatorContext) {
        return loan.getLoanTerm().getMonths() % (12 / loan.getPeriodicity().getDivisor()) == 0;
    }
}
