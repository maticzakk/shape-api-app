package pl.kurs.shapeapiapp.service.factory;

import pl.kurs.shapeapiapp.dto.*;

import java.util.List;


public interface IShape {
    String getShape();
    ShapeDto save(ShapeRequestDto shapeRequestDto, String username);
    ShapeDto edit(Long id, ShapeRequestEditDto shapeRequestEditDto, String username);
    List<ShapeChangeDto> getChanges(long id);

}
