package pl.kurs.shapeapiapp.service.implementation;

import com.querydsl.core.types.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.kurs.shapeapiapp.dto.ShapeChangeDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestEditDto;
import pl.kurs.shapeapiapp.model.Shape;
import pl.kurs.shapeapiapp.query.FindShapesQuery;
import pl.kurs.shapeapiapp.repository.ChangeEventRepository;
import pl.kurs.shapeapiapp.repository.ShapeRepository;
import pl.kurs.shapeapiapp.service.ShapeManager;
import pl.kurs.shapeapiapp.service.factory.IShape;

import java.util.List;
import java.util.Map;

@Service
public class ShapeService implements ShapeManager {

    private final ShapeRepository shapeRepository;
    private final List<IShape> shapes;
    private final FindShapesQuery findShapesQuery;
    private final ChangeEventRepository changeEventRepository;
    private final ModelMapper modelMapper;

    public ShapeService(ShapeRepository shapeRepository, List<IShape> shapes, FindShapesQuery findShapesQuery, ChangeEventRepository changeEventRepository, ModelMapper modelMapper) {
        this.shapeRepository = shapeRepository;
        this.shapes = shapes;
        this.findShapesQuery = findShapesQuery;
        this.changeEventRepository = changeEventRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ShapeDto saveShape(ShapeRequestDto shapeRequestDto, String username) {
        IShape shape = getShapeType(shapeRequestDto.getType());
        return shape.save(shapeRequestDto, username);
    }

    @Override
    public Page<Shape> getFilteredShapes(Map<String, String> parameters, Pageable pageable) {
        Predicate predicate = findShapesQuery.toPredicate(parameters);
        return shapeRepository.findAll(predicate, pageable);
    }


    @Override
    @Transactional
    public ShapeDto editShape(Long id, ShapeRequestEditDto editShapeDto, String username) {
        IShape shape = getShapeType(id);
        return shape.edit(id, editShapeDto, username);
    }



    @Override
    public List<ShapeChangeDto> getChanges(Long id) {
        IShape shape = getShapeType(id);
        return shape.getChanges(id);
    }

    private IShape getShapeType(String type) {
        return shapes.stream()
                .filter(x -> x.getShape().equalsIgnoreCase(type)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported shape type"));
    }

    private IShape getShapeType(long id) {
        String type = shapeRepository.getTypeById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shape not found"));
        return getShapeType(type);
    }
}
