package nl.kooi.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {PeriodMatchesPeriodicityValidator.class}
)
public @interface PeriodMatchesPeriodicity {
    String message() default "The loan term doesn't match the periodicity.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
