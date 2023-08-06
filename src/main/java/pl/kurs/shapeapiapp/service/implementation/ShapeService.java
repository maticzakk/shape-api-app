package pl.kurs.shapeapiapp.service.implementation;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.model.*;
import pl.kurs.shapeapiapp.query.FindShapesQuery;
import pl.kurs.shapeapiapp.repository.RoleRepository;
import pl.kurs.shapeapiapp.repository.ShapeRepository;
import pl.kurs.shapeapiapp.service.factory.IShape;
import pl.kurs.shapeapiapp.service.factory.dto.DtoFactory;

import java.util.List;
import java.util.Map;

@Service
public class ShapeService {

    private final ShapeRepository shapeRepository;
    private final RoleRepository roleRepository;
    private final List<IShape> shapeServices;
    private final JPAQueryFactory queryFactory;
    private final FindShapesQuery findShapesQuery;
    private final ModelMapper modelMapper;
    private final Map<String, IShape> shapes;
    private final DtoFactory dtoFactory;

    public ShapeService(ShapeRepository shapeRepository, RoleRepository roleRepository, List<IShape> shapeServices, JPAQueryFactory queryFactory, FindShapesQuery findShapesQuery, ModelMapper modelMapper, Map<String, IShape> shapes, DtoFactory dtoFactory) {
        this.shapeRepository = shapeRepository;
        this.roleRepository = roleRepository;
        this.shapeServices = shapeServices;
        this.queryFactory = queryFactory;
        this.findShapesQuery = findShapesQuery;
        this.modelMapper = modelMapper;
        this.shapes = shapes;
        this.dtoFactory = dtoFactory;
    }

    public Class<? extends ShapeDto> chooseCorrectDto(String shape) {
        return dtoFactory.createDto(shape).getClass();
    }

    @Transactional
    public ShapeDto saveShape(ShapeRequestDto shapeRequestDto, String username) {
        IShape shapeService = shapeServices.stream()
                .filter(h -> h.getShape().equalsIgnoreCase(shapeRequestDto.getType()))
                .findFirst()
                .orElseThrow();
        Shape shape= shapeService.createShape(shapeRequestDto, username);
        shapeRepository.save(shape);
        ShapeDto shapeDto = modelMapper.map(shape, ShapeDto.class);
        return shapeDto;
    }
    public Page<Shape> getAllShapes(Pageable pageable, Map<String, String> parameters) {
        Predicate predicate = findShapesQuery.toPredicate(parameters);
        return shapeRepository.findAll(predicate, pageable);
    }

//    public ShapeDto getResponse(Shape shape) {
//        return shapeServices.get(Integer.parseInt(shape.getType())).response(shape);
//        return shapes.get(shape.getType()).response(shape);
//    }
}
