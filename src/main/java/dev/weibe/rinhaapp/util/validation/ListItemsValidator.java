package dev.weibe.rinhaapp.util.validation;

import java.util.List;
import java.util.function.Predicate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ListItemsValidator implements ConstraintValidator<ListItems, List<Object>> {

    private final Predicate<Object> stackItemPred = (object) -> {
        if (object instanceof String stack) {
            return !stack.isEmpty() && stack.length() <= 32;
        }
        return false;
    };

    @Override
    public boolean isValid(List<Object> value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.stream().allMatch(stackItemPred);
    }
}
