package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;
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
        Rectangle newRectangle = rectangleRepository.saveAndFlush(rectangle);
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

    private Rectangle createRectangle(ShapeRequestDto request, User username) {
        double height = request.getParameters().get(0);
        double width = request.getParameters().get(1);

        Rectangle rectangle = new Rectangle();
        rectangle.setType(getShape());
        rectangle.setHeight(height);
        rectangle.setWidth(width);
        rectangle.setCreatedBy(username);
        rectangle.setLastModifiedBy(username.getUsername());
        rectangle.setArea(rectangle.getArea());
        rectangle.setPerimeter(rectangle.getPerimeter());
        return rectangle;
    }

    private RectangleDto mapToDto(Rectangle rectangle, String createdByUsername) {
        RectangleDto rectangleDto = modelMapper.map(rectangle, RectangleDto.class);
        rectangleDto.setCreatedBy(createdByUsername);
        rectangleDto.setArea(rectangle.getArea());
        rectangleDto.setPerimeter(rectangle.getPerimeter());
        return rectangleDto;
    }
}
