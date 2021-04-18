package demos.chat.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InEnumValidator implements ConstraintValidator<InEnumConstraint, String> {
    private Set<String> allowedValues;

    @Override
    public void initialize(InEnumConstraint enumValue) {
        this.allowedValues = new HashSet<>(Arrays.asList(enumValue.value()));
    }

    @Override
    public boolean isValid(String enumValue, ConstraintValidatorContext context) {
        return this.allowedValues.contains(enumValue);
    }

}
