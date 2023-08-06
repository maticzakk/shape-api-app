package pl.kurs.shapeapiapp.strategy;

import pl.kurs.shapeapiapp.model.Shape;

import java.util.List;

public interface ICreationStrategy {
    Shape createShape(List<Double> parameters);


}
