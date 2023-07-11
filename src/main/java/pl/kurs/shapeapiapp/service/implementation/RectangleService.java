package pl.kurs.shapeapiapp.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.kurs.shapeapiapp.dto.RectangleDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.exceptions.UserNotFoundException;
import pl.kurs.shapeapiapp.model.Rectangle;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.RectangleRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.IShapeService;
import pl.kurs.shapeapiapp.service.factory.RectangleFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RectangleService implements IShapeService {

    private final RectangleRepository rectangleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RectangleFactory rectangleFactory;

    public RectangleService(RectangleRepository repository, UserRepository userRepository, ModelMapper modelMapper, RectangleFactory rectangleFactory) {
        this.rectangleRepository = repository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.rectangleFactory = rectangleFactory;
    }

    @Override
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        Rectangle rectangle = rectangleFactory.createRectangle(shapeRequestDto, username);
        Rectangle rectangleToSave = rectangleRepository.save(rectangle);
        RectangleDto rectangleDto = modelMapper.map(rectangleToSave, RectangleDto.class);
        rectangleDto.setCreatedBy(rectangle.getCreatedBy().getUsername());
        rectangleDto.setArea(rectangle.calculateArea());
        rectangleDto.setPerimeter(rectangle.calculatePerimeter());
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("NIE ZNALEZIONO UZYTKOWNIKA"));
        user.addShape(rectangleToSave);
        return rectangleDto;
    }

    @Override
    public List<ShapeDto> filter(Map<String, String> parameters) {
        String type = parameters.get("type") == null ? null : parameters.get("type").toUpperCase();
        String createdBy = parameters.get("createdBy");
        LocalDateTime createdAtFrom = parameters.containsKey("createdAtFrom") ? LocalDateTime.parse(parameters.get("createdAtFrom"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        LocalDateTime createdAtTo = parameters.containsKey("createdAtTo") ? LocalDateTime.parse(parameters.get("createdAtTo"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        Double areaFrom = parameters.containsKey("areaFrom") ? Double.parseDouble(parameters.get("areaFrom")) :  null;
        Double areaTo = parameters.containsKey("areaTo") ? Double.parseDouble(parameters.get("areaTo")) : null;
        Double perimeterFrom = parameters.containsKey("perimeterFrom") ? Double.parseDouble(parameters.get("perimeterFrom")) : null;
        Double perimeterTo = parameters.containsKey("perimeterTo") ? Double.parseDouble(parameters.get("perimeterTo")) : null;
        Double heightFrom = parameters.containsKey("heightFrom") ? Double.parseDouble(parameters.get("heightFrom")) : null;
        Double heightTo = parameters.containsKey("heightTo") ? Double.parseDouble(parameters.get("heightTo")) : null;
        Double widthFrom = parameters.containsKey("widthFrom") ? Double.parseDouble(parameters.get("widthFrom")) : null;
        Double widthTo = parameters.containsKey("widthTo") ? Double.parseDouble(parameters.get("widthTo")) : null;

        List<RectangleDto> rectangleDtos = rectangleRepository.getFilteredRectangles(type, areaFrom, areaTo, perimeterFrom, perimeterTo, createdAtFrom, createdAtTo, createdBy, heightFrom, heightTo, widthFrom, widthTo)
                .stream().map(rectangle -> {
                    RectangleDto rectangleDto = modelMapper.map(rectangle, RectangleDto.class);
                    rectangleDto.setCreatedBy(rectangle.getCreatedBy().getUsername());
                    rectangleDto.setArea(rectangle.calculateArea());
                    rectangleDto.setPerimeter(rectangle.calculatePerimeter());
                    return rectangleDto;
                })
                .collect(Collectors.toList());


        return List.copyOf(rectangleDtos);
    }

    @Override
    public String getShape() {
        return "RECTANGLE";
    }
}
