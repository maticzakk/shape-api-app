package pl.kurs.shapeapiapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.shapeapiapp.dto.LoginDto;
import pl.kurs.shapeapiapp.dto.StatusDto;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.security.AuthenticationService;
import pl.kurs.shapeapiapp.service.IUserService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final AuthenticationService authenticationService;
    private final IUserService userManager;

    public UserController(AuthenticationService authenticationService, IUserService userManager) {
        this.authenticationService = authenticationService;
        this.userManager = userManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<StatusDto> authenticateUser(@RequestBody LoginDto loginDto) {
        authenticationService.authenticate(loginDto);
        return new  ResponseEntity<>(new StatusDto("LOGOWANIE UZYTKOWNIKA " + loginDto.getUsername() + " PRZEBIEGLO POMYSLNIE"), OK);
    }

    @PostMapping("/register")
    public ResponseEntity<StatusDto> registerUser(@RequestBody UserSignDto signUpDto) {
        userManager.createUser(signUpDto);
        return new ResponseEntity<>(new StatusDto("REJESTRACJA UZYTKOWNIKA: " + signUpDto.getUsername() + " PRZEBIEGLA POMYSLNIE"), CREATED);
    }
}
