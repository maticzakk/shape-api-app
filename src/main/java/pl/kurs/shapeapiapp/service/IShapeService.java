package pl.kurs.shapeapiapp.service;

import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;

import java.util.List;
import java.util.Map;

public interface IShapeService {
    String getShape();
    ShapeDto save(ShapeRequestDto shapeRequestDto, String username);
    List<ShapeDto> filter(Map<String, String> parameters);
}
