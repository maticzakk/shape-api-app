package pl.kurs.shapeapiapp.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.model.Role;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.RoleRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.IUserService;

import javax.annotation.PostConstruct;

@Component
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void initRoles() {
        roleRepository.save(new Role("CREATOR"));
    }

    @Override
    public User createUser(UserSignDto signDto) {
        User user = new User();
        user.setFirstName(signDto.getFirstName());
        user.setLastName(signDto.getLastName());
        user.setUsername(signDto.getUsername());
        user.setEmail(signDto.getEmail());
        user.setPassword(passwordEncoder.encode(signDto.getPassword()));
        user.addRole(roleRepository.findByName("CREATOR"));
        return userRepository.save(user);
    }
}
