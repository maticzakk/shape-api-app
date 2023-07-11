package pl.kurs.shapeapiapp.service.factory;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.model.Square;
import pl.kurs.shapeapiapp.repository.ShapeRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SquareFactory {
    private final UserRepository userRepository;
    private final ShapeRepository shapeRepository;
    private final ModelMapper modelMapper;

    public SquareFactory(UserRepository userRepository, ShapeRepository shapeRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.shapeRepository = shapeRepository;
        this.modelMapper = modelMapper;
    }
    public Square createSquare(ShapeRequestDto request, String username) {
        Square square = new Square();
        square.setType("SQUARE");
        square.setCreatedBy(userRepository.findByUsername(username).orElseThrow());
        square.setHeight(request.getParameters().get(0));

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        square.setCreatedAt(LocalDateTime.parse(formattedNow, formatter));
        square.setLastModifiedAt(LocalDateTime.parse(formattedNow, formatter));
        square.setLastModifiedBy(username);
        return square;
    }


}
