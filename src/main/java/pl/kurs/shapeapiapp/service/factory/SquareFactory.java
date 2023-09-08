package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.*;
import pl.kurs.shapeapiapp.exceptions.EntityNotFoundException;
import pl.kurs.shapeapiapp.exceptions.UserNotFoundException;
import pl.kurs.shapeapiapp.model.*;
import pl.kurs.shapeapiapp.repository.SquareRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.IChangeEventService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SquareFactory implements IShape{
    private final UserRepository userRepository;
    private final SquareRepository squareRepository;
    private final ModelMapper modelMapper;
    private final IChangeEventService changeEventService;

    public SquareFactory(UserRepository userRepository, SquareRepository squareRepository, ModelMapper modelMapper, IChangeEventService changeEventService) {
        this.userRepository = userRepository;
        this.squareRepository = squareRepository;
        this.modelMapper = modelMapper;
        this.changeEventService = changeEventService;
    }

    @Override
    public String getShape() {
        return "SQUARE";
    }

    private Square createSquare(ShapeRequestDto shapeRequestDto, User username) {
        Square square = new Square();
        square.setType(getShape());
        square.setCreatedBy(username);
        square.setLastModifiedBy(username.getUsername());
        double height = shapeRequestDto.getParameters().get(0);
        square.setHeight(height);
        square.setArea(square.getArea());
        square.setPerimeter(square.getPerimeter());
        return square;
    }

    @Override
    @Transactional
    public ShapeDto save(ShapeRequestDto shapeRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Not found user"));
        Square square = createSquare(shapeRequestDto, user);
        Square savedSquare = squareRepository.save(square);
        user.addShape(savedSquare);
        SquareDto squareDto = mapToDto(savedSquare);
        return squareDto;
    }


    @Override
    @Transactional
    public ShapeDto edit(Long id, ShapeRequestEditDto shapeRequestEditDto, String username) {
        Square square = squareRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shape not found"));
        double oldHeight = square.getHeight();
        square.setHeight(shapeRequestEditDto.getParameters().get(0));
        square.setLastModifiedAt(LocalDateTime.now());
        square.setLastModifiedBy(username);
        Square newSquare = squareRepository.saveAndFlush(square);

        Map<String, Double> parameters = new HashMap<>();
        parameters.put("oldHeight", oldHeight);
        parameters.put("newHeight", newSquare.getHeight());
        changeEventService.save(id, newSquare, username, parameters);
        return mapToDto(newSquare);
    }

    @Override
    public List<ShapeChangeDto> getChanges(long id) {
        return changeEventService.getChanges(id);
    }

    private SquareDto mapToDto(Square square) {
        SquareDto squareDto = modelMapper.map(square, SquareDto.class);
        squareDto.setArea(square.getArea());
        squareDto.setPerimeter(square.getPerimeter());
        return squareDto;
    }
}
