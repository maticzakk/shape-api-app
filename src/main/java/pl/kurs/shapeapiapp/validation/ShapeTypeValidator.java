package pl.kurs.shapeapiapp.validation;

import pl.kurs.shapeapiapp.service.factory.IShape;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShapeTypeValidator implements ConstraintValidator<ShapeType, String> {

    private final List<String> shapes;

    public ShapeTypeValidator(List<IShape> shapeFactories) {
        this.shapes = shapeFactories.stream().map(IShape::getShape).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(s)
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .map(shapes::contains)
                .orElse(true);
    }
}
