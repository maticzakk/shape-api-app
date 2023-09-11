package pl.kurs.shapeapiapp.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.UserDto;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.exceptions.EmailAlreadyExistException;
import pl.kurs.shapeapiapp.exceptions.UsernameAlreadyExistException;
import pl.kurs.shapeapiapp.model.Role;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.RoleRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.IUserService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService implements IUserService{

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
        roleRepository.save(new Role("ADMIN"));
    }

    @Transactional
    @Override
    public User createUser(UserSignDto signDto) {
        if (userRepository.existsWithLockingByUsername(signDto.getUsername())) {
            throw new UsernameAlreadyExistException("Username already exist");
        }
        if (userRepository.existsWithLockingByEmail(signDto.getEmail())) {
            throw new EmailAlreadyExistException("Email already exist");
        }
        User user = new User();
        user.setFirstName(signDto.getFirstName());
        user.setLastName(signDto.getLastName());
        user.setUsername(signDto.getUsername());
        user.setEmail(signDto.getEmail());
        user.setPassword(passwordEncoder.encode(signDto.getPassword()));
        user.addRole(roleRepository.findByName("CREATOR"));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<Long> usersId = userRepository.findIds(pageable);
        List<User> users = userRepository.findAllWithRolesAndShapesByIds(usersId.getContent());

        List<UserDto> userDtos = users.stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, usersId.getTotalElements());
    }


    private UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
