package pl.kurs.shapeapiapp.service.factory;

import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;


public interface IShape {
    String getShape();
    ShapeDto save(ShapeRequestDto shapeRequestDto, String username);
}
