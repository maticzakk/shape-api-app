package pl.kurs.shapeapiapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.shapeapiapp.dto.ShapeChangeDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestEditDto;
import pl.kurs.shapeapiapp.model.Shape;

import java.util.List;
import java.util.Map;

public interface ShapeManager {
    ShapeDto saveShape(ShapeRequestDto shapeRequestDto, String username);
    Page<Shape> getFilteredShapes(Map<String, String> parameters, Pageable pageable);
    ShapeDto editShape(Long id, ShapeRequestEditDto editShapeDto, String username);
    List<ShapeChangeDto> getChanges(Long id);
}
