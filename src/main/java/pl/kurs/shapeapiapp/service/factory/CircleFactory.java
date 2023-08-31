package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.*;
import pl.kurs.shapeapiapp.exceptions.EntityNotFoundException;
import pl.kurs.shapeapiapp.model.Circle;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.CircleRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.IChangeEventService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CircleFactory implements IShape {

    private final UserRepository userRepository;
    private final CircleRepository circleRepository;
    private final ModelMapper modelMapper;
    private final IChangeEventService changeEventService;

    public CircleFactory(UserRepository userRepository, CircleRepository circleRepository, ModelMapper modelMapper, IChangeEventService changeEventService) {
        this.userRepository = userRepository;
        this.circleRepository = circleRepository;
        this.modelMapper = modelMapper;
        this.changeEventService = changeEventService;
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

    @Transactional
    @Override
    public ShapeDto edit(Long id,ShapeRequestEditDto shapeRequestEditDto, String username) {
        Circle circle = circleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shape not found"));
        if (circle.getVersion() != shapeRequestEditDto.getVersion()) {
            throw new OptimisticLockingFailureException("Shape version conflict");
        }
        double oldRadius = circle.getRadius();
        circle.setRadius(shapeRequestEditDto.getParameters().get(0));
        circle.setLastModifiedAt(LocalDateTime.now());
        Circle newCircle = circleRepository.save(circle);
        Map<String, Double> parameters = new HashMap<>();
        parameters.put("oldRadius", oldRadius);
        parameters.put("newRadius", newCircle.getRadius());
        changeEventService.save(id, newCircle, username, parameters);
        return mapToDto(newCircle, username);
    }

    @Override
    public List<ShapeChangeDto> getChanges(long id) {
        return changeEventService.getChanges(id);
    }

    private Circle createCircle(ShapeRequestDto request, User username) {
        double radius = request.getParameters().get(0);

        Circle circle = new Circle();
        circle.setType(getShape());
        circle.setCreatedAt(LocalDateTime.now());
        circle.setCreatedBy(username);
        circle.setLastModifiedAt(LocalDateTime.now());
        circle.setLastModifiedBy(username.getUsername());
        circle.setRadius(radius);
        circle.setArea(circle.getPerimeter());
        circle.setPerimeter(circle.getPerimeter());
        return circle;
    }

    private CircleDto mapToDto(Circle circle, String createdByUsername) {
        CircleDto circleDto = modelMapper.map(circle, CircleDto.class);
        circleDto.setCreatedBy(createdByUsername);
        circleDto.setArea(circle.getArea());
        circleDto.setPerimeter(circle.getPerimeter());
        return circleDto;
    }
}
