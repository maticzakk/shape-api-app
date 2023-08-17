package pl.kurs.shapeapiapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ShapeTypeValidator implements ConstraintValidator<ShapeType, String> {

    private List<String> listTypes;

    @Override
    public void initialize(ShapeType constraintAnnotation) {
        listTypes = List.of("square", "circle", "rectangle");
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!listTypes.contains(s.trim().toLowerCase())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Not supported shape! Supported shapes are: " + String.join(", ", listTypes))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
