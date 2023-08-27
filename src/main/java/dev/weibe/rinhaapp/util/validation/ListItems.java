package dev.weibe.rinhaapp.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = ListItemsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListItems {

    String message() default "Invalid stack item";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
