package pl.kurs.shapeapiapp.service.factory;

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
import java.time.LocalDateTime;

@Component
public class RectangleFactory implements IShape {

    private final UserRepository userRepository;
    private final RectangleRepository repository;
    private final ModelMapper modelMapper;

    public RectangleFactory(UserRepository userRepository, RectangleRepository repository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.modelMapper = modelMapper;
    }


    @Override
    public String getShape() {
        return "RECTANGLE";
    }

    @Override
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        Rectangle rectangle = createRectangle(shapeRequestDto, username);
        Rectangle savedRectangle = repository.save(rectangle);
        RectangleDto rectangleDto = mapToDto(savedRectangle);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("USERNAME_NOT_FOUND"));
        user.addShape(savedRectangle);
        return rectangleDto;
    }

    public Rectangle createRectangle(ShapeRequestDto request, String username) {
        Rectangle rectangle = new Rectangle();
        rectangle.setType("RECTANGLE");
        rectangle.setCreatedBy(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("NIE ZNALEZIONO UZYTKOWNIKA")));
        rectangle.setHeight(request.getParameters().get(0));
        rectangle.setWidth(request.getParameters().get(1));
        rectangle.setCreatedAt(LocalDateTime.now());
        rectangle.setLastModifiedAt(LocalDateTime.now());
        rectangle.setLastModifiedBy(username);
        return rectangle;
    }

    private RectangleDto mapToDto(Rectangle rectangle) {
        RectangleDto rectangleDto = modelMapper.map(rectangle, RectangleDto.class);
        rectangleDto.setCreatedBy(rectangle.getCreatedBy().getUsername());
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
