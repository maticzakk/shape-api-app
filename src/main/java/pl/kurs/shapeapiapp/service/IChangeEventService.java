package pl.kurs.shapeapiapp.service;

import pl.kurs.shapeapiapp.dto.ShapeChangeDto;
import pl.kurs.shapeapiapp.model.Shape;

import java.util.List;
import java.util.Map;

public interface IChangeEventService {

    void save(Long id, Shape editedShape, String username, Map<String, Double> oldProperties);
    List<ShapeChangeDto> getChanges(long shapeId);
}
