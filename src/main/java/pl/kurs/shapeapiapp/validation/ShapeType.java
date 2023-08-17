package pl.kurs.shapeapiapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ShapeTypeValidator.class)
public @interface ShapeType {
    String message() default "Unsupported shape type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
