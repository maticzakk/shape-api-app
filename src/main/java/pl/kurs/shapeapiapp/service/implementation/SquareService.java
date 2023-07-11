package pl.kurs.shapeapiapp.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.dto.SquareDto;
import pl.kurs.shapeapiapp.model.Square;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.SquareRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.IShapeService;
import pl.kurs.shapeapiapp.service.factory.SquareFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SquareService implements IShapeService {

    private final SquareRepository squareRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SquareFactory squareFactory;

    public SquareService(SquareRepository squareRepository, UserRepository userRepository, ModelMapper modelMapper, SquareFactory squareFactory) {
        this.squareRepository = squareRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.squareFactory = squareFactory;
    }

    @Override
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        Square square = squareFactory.createSquare(shapeRequestDto, username);
        Square squareToSave = squareRepository.save(square);
        SquareDto squareDto = modelMapper.map(squareToSave, SquareDto.class);
        squareDto.setCreatedBy(square.getCreatedBy().getUsername());
        squareDto.setArea(square.calculateArea());
        squareDto.setPerimeter(square.calculatePerimeter());
        User user = userRepository.findByUsername(username).orElseThrow();
        user.addShape(squareToSave);
        return squareDto;
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

        List<SquareDto> squareDtos = squareRepository.getFilteredSquares(type, areaFrom, areaTo, perimeterFrom, perimeterTo, createdAtFrom, createdAtTo, createdBy, heightFrom, heightTo)
                .stream().map(square -> {
                    SquareDto squareDto = modelMapper.map(square, SquareDto.class);
                    squareDto.setCreatedBy(square.getCreatedBy().getUsername());
                    squareDto.setArea(square.calculateArea());
                    squareDto.setPerimeter(square.calculatePerimeter());
                    return squareDto;
                })
                .collect(Collectors.toList());


        return List.copyOf(squareDtos);
    }

    @Override
    public String getShape() {
        return "SQUARE";
    }
}
