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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DirtiesContext
    public void shouldRegisterUserSuccessfully() throws Exception {
        UserSignDto userSignDto = loginDto();

        String userSignDtoAsString = mapper.writeValueAsString(userSignDto);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignDtoAsString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.email").value("jdoe@example.com"));
    }

    @Test
    @DirtiesContext
    public void shouldReturnBadRequestForMissingFields() throws Exception {
        UserSignDto userSignDto = new UserSignDto();
        userSignDto.setUsername("johndoe");
        userSignDto.setPassword("password123");

        String userSignDtoAsString = mapper.writeValueAsString(userSignDto);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignDtoAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBadRequestForInvalidData() throws Exception {
        UserSignDto userSignDto = loginDto();
        userSignDto.setEmail("invalid_email");

        String userSignDtoAsString = mapper.writeValueAsString(userSignDto);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignDtoAsString))
                .andExpect(status().isBadRequest());
    }

    public static UserSignDto loginDto() {
        UserSignDto request = new UserSignDto();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setUsername("jdoe");
        request.setEmail("jdoe@example.com");
        request.setPassword("password123");
        return request;
    }
}
