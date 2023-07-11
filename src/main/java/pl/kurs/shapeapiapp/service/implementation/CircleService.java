package pl.kurs.shapeapiapp.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.kurs.shapeapiapp.dto.CircleDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.exceptions.UserNotFoundException;
import pl.kurs.shapeapiapp.model.Circle;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.CircleRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.IShapeService;
import pl.kurs.shapeapiapp.service.factory.CircleFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CircleService implements IShapeService {
    private final CircleRepository circleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CircleFactory circleFactory;

    public CircleService(CircleRepository circleRepository, UserRepository userRepository, ModelMapper modelMapper, CircleFactory circleFactory) {

        this.circleRepository = circleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.circleFactory = circleFactory;
    }

    @Override
    public String getShape() {
        return "CIRCLE";
    }

    @Override
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        Circle circle = circleFactory.createCircle(shapeRequestDto, username);
        Circle circleToSave = circleRepository.save(circle);
        CircleDto circleDto = modelMapper.map(circleToSave, CircleDto.class);
        circleDto.setCreatedBy(circle.getCreatedBy().getUsername());
        circleDto.setArea(circle.calculateArea());
        circleDto.setPerimeter(circle.calculatePerimeter());
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("NIE ZNALEZIONO UZYTKOWNIKA"));
        user.addShape(circleToSave);
        return circleDto;
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
        Double radiusFrom = parameters.containsKey("radiusFrom") ? Double.parseDouble(parameters.get("radiusFrom")) : null;
        Double radiusTo = parameters.containsKey("radiusTo") ? Double.parseDouble(parameters.get("radiusTo")) : null;

        List<CircleDto> circleDtos = circleRepository.getFilteredCircles(type, areaFrom, areaTo, perimeterFrom, perimeterTo, createdAtFrom, createdAtTo, createdBy, radiusFrom, radiusTo)
                .stream().map(circle -> {
                    CircleDto circleDto = modelMapper.map(circle, CircleDto.class);
                    circleDto.setCreatedBy(circle.getCreatedBy().getUsername());
                    circleDto.setArea(circle.calculateArea());
                    circleDto.setPerimeter(circle.calculatePerimeter());
                    return circleDto;
                })
                .collect(Collectors.toList());


        return List.copyOf(circleDtos);
    }
}
