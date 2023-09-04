package pl.kurs.shapeapiapp.converter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.UserDto;
import pl.kurs.shapeapiapp.model.Role;
import pl.kurs.shapeapiapp.model.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(MappingContext<User, UserDto> mappingContext) {
        User user = mappingContext.getSource();
        Set<String> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName());
        }

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(roles)
                .shapesCreated(user.getShapes().size())
                .build();
    }
}
