package ru.practicum.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateTimeValidator implements ConstraintValidator<NotBeforeTwoHours, LocalDateTime> {
    @Override
    public void initialize(NotBeforeTwoHours constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDateTime twoHoursLater = LocalDateTime.now().plusHours(2);
        return value.isAfter(twoHoursLater) || value.isEqual(twoHoursLater);
    }
}
