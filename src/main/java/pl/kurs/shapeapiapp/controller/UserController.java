package pl.kurs.shapeapiapp.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.service.IUserService;

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
    public ResponseEntity<UserSignDto> registerUser(@RequestBody UserSignDto userSignDto) {
        User user = userService.createUser(userSignDto);
        return ResponseEntity.status(CREATED).body(modelMapper.map(user, UserSignDto.class));

    }


}
