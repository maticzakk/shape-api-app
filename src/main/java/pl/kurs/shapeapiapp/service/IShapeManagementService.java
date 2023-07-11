package pl.kurs.shapeapiapp.service;

import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;

import java.util.List;
import java.util.Map;

public interface IShapeManagementService {
    ShapeDto saveShape(ShapeRequestDto shapeRequestDto, String username);
    List<ShapeDto> getFiltered(Map<String, String> parameters);
}
