package pl.kurs.shapeapiapp.service.factory;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.exceptions.UserNotFoundException;
import pl.kurs.shapeapiapp.model.Circle;
import pl.kurs.shapeapiapp.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class CircleFactory {
    private final UserRepository userRepository;

    public CircleFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Circle createCircle(ShapeRequestDto request, String username) {
        Circle circle = new Circle();
        circle.setType("CIRCLE");
        circle.setCreatedBy(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("NIE ZNALEZIONO UZYTKOWNIKA")));
        circle.setRadius(request.getParameters().get(0));
        circle.setCreatedAt(LocalDateTime.now());
        circle.setLastModifiedAt(LocalDateTime.now());
        circle.setLastModifiedBy(username);
        return circle;
    }
}
