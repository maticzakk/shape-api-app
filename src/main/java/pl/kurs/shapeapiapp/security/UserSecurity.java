package pl.kurs.shapeapiapp.security;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.repository.ShapeRepository;

@Service
public class UserSecurity {

    private final ShapeRepository shapeRepository;

    public UserSecurity(ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
    }
}

