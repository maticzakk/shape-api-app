package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.dto.SquareDto;
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

    private Square createSquare(ShapeRequestDto shapeRequestDto, String username) {
        Square square = new Square();
        square.setType(getShape());
        square.setCreatedBy(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USERNAME_NOT_FOUND")));
        square.setCreatedAt(LocalDateTime.now());
        square.setLastModifiedAt(LocalDateTime.now());
        square.setLastModifiedBy(username);
        square.setHeight(shapeRequestDto.getParameters().get(0));
        return square;
    }

    @Override
    @Transactional
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        Square square = createSquare(shapeRequestDto, username);
        Square savedSquare = squareRepository.save(square);
        SquareDto squareDto = mapToDto(savedSquare);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USERNAME_NOT_FOUND"));
        user.addShape(savedSquare);

        return squareDto;
    }

    private SquareDto mapToDto(Square square) {
        SquareDto squareDto = modelMapper.map(square, SquareDto.class);
        squareDto.setCreatedBy(square.getCreatedBy().getUsername());
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
