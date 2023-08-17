package pl.kurs.shapeapiapp.security.jwt;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtRequestDto {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
