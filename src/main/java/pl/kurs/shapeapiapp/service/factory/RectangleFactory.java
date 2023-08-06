//package pl.kurs.shapeapiapp.service.factory;
//
//import org.springframework.stereotype.Service;
//import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
//import pl.kurs.shapeapiapp.exceptions.UserNotFoundException;
//import pl.kurs.shapeapiapp.model.Rectangle;
//import pl.kurs.shapeapiapp.repository.UserRepository;
//
//import java.time.LocalDateTime;
//
//@Service
//public class RectangleFactory {
//
//    private final UserRepository userRepository;
//
//    public RectangleFactory(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public Rectangle createRectangle(ShapeRequestDto request, String username) {
//        Rectangle rectangle = new Rectangle();
//        rectangle.setType("RECTANGLE");
//        rectangle.setCreatedBy(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("NIE ZNALEZIONO UZYTKOWNIKA")));
//        rectangle.setHeight(request.getParameters().get(0));
//        rectangle.setWidth(request.getParameters().get(1));
//        rectangle.setCreatedAt(LocalDateTime.now());
//        rectangle.setLastModifiedAt(LocalDateTime.now());
//        rectangle.setLastModifiedBy(username);
//        return rectangle;
//    }
//}
