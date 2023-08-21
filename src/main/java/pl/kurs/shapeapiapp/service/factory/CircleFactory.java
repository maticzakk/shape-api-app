package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.CircleDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.model.Circle;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.CircleRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import java.time.LocalDateTime;

@Component
public class CircleFactory implements IShape {

    private final UserRepository userRepository;
    private final CircleRepository circleRepository;
    private final ModelMapper modelMapper;

    public CircleFactory(UserRepository userRepository, CircleRepository circleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.circleRepository = circleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getShape() {
        return "CIRCLE";
    }

    @Override
    @Transactional
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user"));
        Circle circle = createCircle(shapeRequestDto, user);
        Circle savedCircle = circleRepository.saveAndFlush(circle);
        CircleDto circleDto = mapToDto(savedCircle, user.getUsername());

        user.addShape(savedCircle);

        return circleDto;
    }

    private Circle createCircle(ShapeRequestDto request, User username) {
        Circle circle = new Circle();
        circle.setType(getShape());
        circle.setCreatedAt(LocalDateTime.now());
        circle.setLastModifiedAt(LocalDateTime.now());
        circle.setLastModifiedBy(username.getUsername());
        circle.setRadius(request.getParameters().get(0));
        return circle;
    }

    private CircleDto mapToDto(Circle circle, String createdByUsername) {
        CircleDto circleDto = modelMapper.map(circle, CircleDto.class);
        circleDto.setCreatedBy(createdByUsername);
        circleDto.setArea(calculateArea(circle.getRadius()));
        circleDto.setPerimeter(calculatePerimeter(circle.getRadius()));
        return circleDto;
    }

    private double calculateArea(double radius) {
        return Math.PI * (Math.pow(radius, 2));
    }

    private double calculatePerimeter(double radius) {
        return (2 * Math.PI) * radius;
    }
}
