package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.CircleDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.exceptions.UserNotFoundException;
import pl.kurs.shapeapiapp.model.Circle;
import pl.kurs.shapeapiapp.model.Shape;
import pl.kurs.shapeapiapp.repository.ShapeRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CircleFactory implements IShape {
    private final UserRepository userRepository;
    private final ShapeRepository shapeRepository;
    private final ModelMapper modelMapper;

    public CircleFactory(UserRepository userRepository, ShapeRepository shapeRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.shapeRepository = shapeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public String getShape() {
        return "CIRCLE";
    }

    @Override
    public Class<? extends ShapeDto> getShapeDtoClass() {
        return CircleDto.class;
    }

    @Override
    public Shape createShape(ShapeRequestDto shapeRequestDto, String username) {
        Circle circle = new Circle();
        circle.setType("CIRCLE");
        circle.setCreatedBy(userRepository.findByUsername(username).orElseThrow());
        circle.setRadius(shapeRequestDto.getParameters().get(0));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        circle.setCreatedAt(LocalDateTime.parse(formattedNow, formatter));
        circle.setLastModifiedAt(LocalDateTime.parse(formattedNow, formatter));
        circle.setLastModifiedBy(username);
        return circle;
    }

    @Override
    public ShapeDto response(Shape shape) {
        CircleDto circleDto = modelMapper.map(shape, CircleDto.class);
        circleDto.setPerimeter(circleDto.getPerimeter());
        circleDto.setArea(circleDto.getArea());
        if (shape instanceof Circle) {
            circleDto.setRadius(((Circle) shape).getRadius());
        }
        return circleDto;
    }
}
