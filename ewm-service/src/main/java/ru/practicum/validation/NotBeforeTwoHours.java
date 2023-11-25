package ru.practicum.validation;

import ru.practicum.validation.DateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeValidator.class)
public @interface NotBeforeTwoHours {
    String message() default "Дата не может быть раньше, чем два часа от текущего времени";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
