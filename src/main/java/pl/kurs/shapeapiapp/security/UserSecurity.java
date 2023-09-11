package pl.kurs.shapeapiapp.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.repository.ShapeRepository;

@Service
public class UserSecurity {

    private final ShapeRepository shapeRepository;

    public UserSecurity(ShapeRepository shapeRepository) {
        this.shapeRepository = shapeRepository;
    }

    public boolean isResourceCreator(Long shapeId) {
        return shapeRepository.getCreatedByUsernameById(shapeId).map(username -> SecurityContextHolder.getContext()
                .getAuthentication().getName().equalsIgnoreCase(username)).orElse(false);
    }
}

