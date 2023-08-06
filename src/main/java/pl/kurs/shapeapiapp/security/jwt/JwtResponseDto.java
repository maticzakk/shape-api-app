package pl.kurs.shapeapiapp.security.jwt;

import java.io.Serializable;

public class JwtResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String jwtToken;

    public JwtResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }
    public String getJwtToken() {
        return jwtToken;
    }
}
