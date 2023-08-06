package pl.kurs.shapeapiapp.service.factory;

import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.model.Shape;


public interface IShape {
    String getShape();

//    ShapeDto save(ShapeRequestDto shapeRequestDto, String username);
//    List<ShapeDto> filter(Map<String, String> parameters);
    Shape createShape(ShapeRequestDto shapeRequestDto, String username);
    ShapeDto response(Shape shape);
    Class<? extends ShapeDto> getShapeDtoClass();
}
