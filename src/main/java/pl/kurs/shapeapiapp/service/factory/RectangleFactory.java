package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.*;
import pl.kurs.shapeapiapp.exceptions.EntityNotFoundException;
import pl.kurs.shapeapiapp.exceptions.UserNotFoundException;
import pl.kurs.shapeapiapp.model.Rectangle;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.RectangleRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.IChangeEventService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RectangleFactory implements IShape {

    private final UserRepository userRepository;
    private final RectangleRepository rectangleRepository;
    private final ModelMapper modelMapper;
    private final IChangeEventService changeEventService;

    public RectangleFactory(UserRepository userRepository, RectangleRepository repository, ModelMapper modelMapper, IChangeEventService changeEventService) {
        this.userRepository = userRepository;
        this.rectangleRepository = repository;
        this.modelMapper = modelMapper;
        this.changeEventService = changeEventService;
    }

    @Override
    public String getShape() {
        return "RECTANGLE";
    }

    @Transactional
    @Override
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Not found user"));
        Rectangle rectangle = createRectangle(shapeRequestDto, user);
        Rectangle savedRectangle = rectangleRepository.save(rectangle);
        RectangleDto rectangleDto = mapToDto(savedRectangle, user.getUsername());

        user.addShape(savedRectangle);
        return rectangleDto;
    }

    @Transactional
    @Override
    public ShapeDto edit(Long id, ShapeRequestEditDto shapeRequestEditDto, String username) {
        Rectangle rectangle = rectangleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shape not found"));
        double oldHeight = rectangle.getHeight();
        double oldWidth = rectangle.getWidth();
        rectangle.setHeight(shapeRequestEditDto.getParameters().get(0));
        rectangle.setWidth(shapeRequestEditDto.getParameters().get(1));
        rectangle.setLastModifiedAt(LocalDateTime.now());
        Rectangle newRectangle = rectangleRepository.save(rectangle);
        Map<String, Double> parameters = new HashMap<>();
        parameters.put("oldHeight", oldHeight);
        parameters.put("oldWidth", oldWidth);
        parameters.put("newHeight", newRectangle.getHeight());
        parameters.put("newWidth", newRectangle.getWidth());
        changeEventService.save(id, newRectangle, username, parameters);
        return mapToDto(newRectangle, username);
    }

    @Override
    public List<ShapeChangeDto> getChanges(long id) {
        return changeEventService.getChanges(id);
    }

    public Rectangle createRectangle(ShapeRequestDto request, User username) {
        Rectangle rectangle = new Rectangle();
        rectangle.setType("RECTANGLE");
        rectangle.setHeight(request.getParameters().get(0));
        rectangle.setWidth(request.getParameters().get(1));
        rectangle.setCreatedBy(username);
        rectangle.setCreatedAt(LocalDateTime.now());
        rectangle.setLastModifiedAt(LocalDateTime.now());
        rectangle.setLastModifiedBy(username.getUsername());
        return rectangle;
    }

    private RectangleDto mapToDto(Rectangle rectangle, String createdByUsername) {
        RectangleDto rectangleDto = modelMapper.map(rectangle, RectangleDto.class);
        rectangleDto.setCreatedBy(createdByUsername);
        rectangleDto.setArea(calculateArea(rectangle.getHeight(), rectangle.getWidth()));
        rectangleDto.setPerimeter(calculatePerimeter(rectangle.getHeight(), rectangle.getWidth()));
        return rectangleDto;
    }

    private double calculateArea(double height, double width) {
        return width * height;
    }
    private double calculatePerimeter(double height, double width) {
        return (2 *width) + (2 * height);
    }
}
