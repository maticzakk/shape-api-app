package pl.kurs.shapeapiapp.strategy;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.model.Rectangle;
import pl.kurs.shapeapiapp.model.Shape;

import java.util.List;

@Service("RECTANGLE")
public class RectangleStrategy implements ICreationStrategy {
    @Override
    public Shape createShape(List<Double> parameters) {
        double width = parameters.get(0);
        double height = parameters.get(1);
        return new Rectangle(width, height);
    }

}
