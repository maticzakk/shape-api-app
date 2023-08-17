package pl.kurs.shapeapiapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraintValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null && email.contains("@");
    }

    @Override
    public void initialize(Email constraintAnnotation) {

    }
}
