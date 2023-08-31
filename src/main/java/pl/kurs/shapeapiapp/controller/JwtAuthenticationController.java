package pl.kurs.shapeapiapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.shapeapiapp.security.jwt.*;

@RestController
public class JwtAuthenticationController {

    private final AuthenticationService authenticationService;

    public JwtAuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/api/v1/authenticate")
    public ResponseEntity<JwtResponseDto> createAuthenticationToken(@RequestBody JwtRequestDto authenticationRequest) throws Exception {
        JwtResponseDto jwtResponseDto = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
