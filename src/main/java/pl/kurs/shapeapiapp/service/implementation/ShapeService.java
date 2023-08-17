package pl.kurs.shapeapiapp.service.implementation;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.model.Shape;
import pl.kurs.shapeapiapp.query.FindShapesQuery;
import pl.kurs.shapeapiapp.repository.ShapeRepository;
import pl.kurs.shapeapiapp.service.ShapeManager;
import pl.kurs.shapeapiapp.service.factory.IShape;

import java.util.List;
import java.util.Map;

@Service
public class ShapeService  implements ShapeManager {

    private final ShapeRepository shapeRepository;
    private final List<IShape> shapes;
    private final FindShapesQuery findShapesQuery;

    public ShapeService(ShapeRepository shapeRepository, List<IShape> shapes, FindShapesQuery findShapesQuery) {
        this.shapeRepository = shapeRepository;
        this.shapes = shapes;
        this.findShapesQuery = findShapesQuery;
    }

    @Override
    @Transactional
    public ShapeDto saveShape(ShapeRequestDto shapeRequestDto, String username) {
        IShape shape = getShapeType(shapeRequestDto.getType());
        return shape.save(shapeRequestDto, username);
    }

    @Transactional
    @Override
    public Page<Shape> getFilteredShapes(Map<String, String> parameters, Pageable pageable) {
        Predicate predicate = findShapesQuery.toPredicate(parameters);
        return shapeRepository.findAll(predicate, pageable);
    }

    private IShape getShapeType(String type) {
        return shapes.stream()
                .filter(x -> x.getShape().equalsIgnoreCase(type)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported shape type"));
    }
}
