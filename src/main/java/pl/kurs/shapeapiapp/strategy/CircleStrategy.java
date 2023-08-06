package pl.kurs.shapeapiapp.strategy;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.model.Circle;
import pl.kurs.shapeapiapp.model.Shape;

import java.util.List;

@Service("CIRCLE")
public class CircleStrategy implements ICreationStrategy {
    @Override
    public Shape createShape(List<Double> parameters) {
        double radius = parameters.get(0);
        return new Circle(radius);
    }


}
