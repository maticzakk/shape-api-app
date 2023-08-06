package pl.kurs.shapeapiapp.strategy;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.model.Shape;
import pl.kurs.shapeapiapp.model.Square;

import java.util.List;

@Service("SQUARE")
public class SquareStrategy implements ICreationStrategy{

    @Override
    public Shape createShape(List<Double> parameters) {
        double width = parameters.get(0);
        return new Square(width);
    }


}
