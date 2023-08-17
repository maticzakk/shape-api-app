package pl.kurs.shapeapiapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.security.jwt.JwtRequestDto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JwtAuthenticationControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    public void shouldReturnJwtResponseDtoOnSuccessfulAuthentication() throws Exception {
        UserSignDto userSignDto = loginDto();
        String signUpRequestDtoAsString = mapper.writeValueAsString(userSignDto);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequestDtoAsString))
                .andExpect(status().isCreated());

        JwtRequestDto jwtRequestDto = new JwtRequestDto("jsmith", "password123");
        String jwtRequestDtoAsString = mapper.writeValueAsString(jwtRequestDto);

        mockMvc.perform(post("/api/v1/authenticate")
                .with(httpBasic("jsmith", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestDtoAsString))
                .andReturn();
    }

    public static UserSignDto loginDto() {
        UserSignDto request = new UserSignDto();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setUsername("jsmith");
        request.setEmail("jsmith@gmail.com");
        request.setPassword("password123");
        return request;
    }
}
