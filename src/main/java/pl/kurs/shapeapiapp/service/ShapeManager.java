package pl.kurs.shapeapiapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.model.Shape;

import java.util.Map;

public interface ShapeManager {
    ShapeDto saveShape(ShapeRequestDto shapeRequestDto, String username);
    Page<Shape> getFilteredShapes(Map<String, String> parameters, Pageable pageable);
}
