package pl.kurs.shapeapiapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.security.jwt.JwtRequestDto;
import pl.kurs.shapeapiapp.utils.TestUtils;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }


    @Test
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
    public void shouldReturnBadRequestForInvalidData() throws Exception {
        UserSignDto userSignDto = loginDto();
        userSignDto.setEmail("invalid_email");

        String userSignDtoAsString = mapper.writeValueAsString(userSignDto);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignDtoAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAllUsersSuccessfully() throws Exception {
        // Przygotowanie tokenu dla 2 uzytkownikow
        String token = loginUserAndGetToken();
        String token2 = loginUser2AndGetToken();

        // Wywołanie metody i sprawdzenie odpowiedzi
        mockMvc.perform(get("/api/v1/users")
                .header("Authorization", "Bearer " + token)
                .header("Authorization", "Bearer " + token2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2))) // Upewnij się, że to odpowiada liczbie użytkowników
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.content[0].username").value("jdoe"))
                .andExpect(jsonPath("$.content[0].email").value("jdoe@example.com"))
                .andExpect(jsonPath("$.content[1].firstName").value("Andrzej"))
                .andExpect(jsonPath("$.content[1].lastName").value("Nowak"))
                .andExpect(jsonPath("$.content[1].username").value("anowak"))
                .andExpect(jsonPath("$.content[1].email").value("anowak@example.com"))
                .andExpect(jsonPath("$.pageable.offset").value(0))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(20))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.sort.empty").value(true))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.empty").value(false));
    }

    private String loginUserAndGetToken() throws Exception {
        UserSignDto userSignDto = loginDto();
        String signUpRequestDtoAsString = mapper.writeValueAsString(userSignDto);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequestDtoAsString))
                .andExpect(status().isCreated());

        JwtRequestDto jwtRequestDto = new JwtRequestDto("jdoe", "password123");
        String jwtRequestDtoAsString = mapper.writeValueAsString(jwtRequestDto);

        MvcResult jwtRequestResponse = mockMvc.perform(post("/api/v1/authenticate")
                .with(httpBasic("jdoe", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestDtoAsString))
                .andReturn();

        return TestUtils.getTokenFromJson(jwtRequestResponse.getResponse().getContentAsString());
    }

    private String loginUser2AndGetToken() throws Exception {
        UserSignDto request = new UserSignDto();
        request.setFirstName("Andrzej");
        request.setLastName("Nowak");
        request.setUsername("anowak");
        request.setEmail("anowak@example.com");
        request.setPassword("password123");
        String signUpRequestDtoAsString = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequestDtoAsString))
                .andExpect(status().isCreated());

        JwtRequestDto jwtRequestDto = new JwtRequestDto("anowak", "password123");
        String jwtRequestDtoAsString = mapper.writeValueAsString(jwtRequestDto);

        MvcResult jwtRequestResponse = mockMvc.perform(post("/api/v1/authenticate")
                .with(httpBasic("anowak", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestDtoAsString))
                .andReturn();

        return TestUtils.getTokenFromJson(jwtRequestResponse.getResponse().getContentAsString());
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
