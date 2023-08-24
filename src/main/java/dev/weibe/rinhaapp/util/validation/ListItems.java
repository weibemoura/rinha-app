package dev.weibe.rinhaapp.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = ListItemsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListItems {

    String message() default "The input list cannot contain more than 4 movies.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
