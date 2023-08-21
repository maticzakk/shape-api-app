package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.dto.SquareDto;
import pl.kurs.shapeapiapp.exceptions.UserNotFoundException;
import pl.kurs.shapeapiapp.model.Square;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.SquareRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class SquareFactory implements IShape{
    private final UserRepository userRepository;
    private final SquareRepository squareRepository;
    private final ModelMapper modelMapper;

    public SquareFactory(UserRepository userRepository, SquareRepository squareRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.squareRepository = squareRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String getShape() {
        return "SQUARE";
    }

    private Square createSquare(ShapeRequestDto shapeRequestDto, User username) {
        Square square = new Square();
        square.setType(getShape());
        square.setCreatedAt(LocalDateTime.now());
        square.setLastModifiedAt(LocalDateTime.now());
        square.setLastModifiedBy(username.getUsername());
        square.setHeight(shapeRequestDto.getParameters().get(0));
        return square;
    }

    @Override
    @Transactional
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Not found user"));
        Square square = createSquare(shapeRequestDto, user);
        Square savedSquare = squareRepository.save(square);
        SquareDto squareDto = mapToDto(savedSquare, user.getUsername());

        user.addShape(savedSquare);

        return squareDto;
    }

    private SquareDto mapToDto(Square square, String createdByUsername) {
        SquareDto squareDto = modelMapper.map(square, SquareDto.class);
        squareDto.setCreatedBy(createdByUsername);
        squareDto.setArea(calculateArea(square.getHeight()));
        squareDto.setPerimeter(calculatePerimeter(square.getHeight()));
        return squareDto;
    }

    private double calculateArea(double height) {
        return height * height;
    }
    private double calculatePerimeter(double height) {
        return 4 * height;
    }
}
