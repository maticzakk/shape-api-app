package pl.kurs.shapeapiapp.controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.shapeapiapp.dto.UserDto;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.service.IUserService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private IUserService userService;
    private ModelMapper modelMapper;

    public UserController(IUserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserSignDto> registerUser(@RequestBody @Valid UserSignDto userSignDto) {
        User user = userService.createUser(userSignDto);
        return ResponseEntity.status(CREATED).body(modelMapper.map(user, UserSignDto.class));

    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

}
