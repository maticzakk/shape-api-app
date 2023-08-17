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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.security.jwt.JwtRequestDto;
import pl.kurs.shapeapiapp.utils.TestUtils;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ShapeControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DirtiesContext
    void shouldAddCircle() throws Exception {
        // Logowanie użytkownika
        String token = loginUserAndGetToken();

        // Wysyłanie żądania dodania koła i potwierdzenie dodania
        ShapeRequestDto addShapeRequestDto = new ShapeRequestDto("CIRCLE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type").value("CIRCLE"))
                .andExpect(jsonPath("$.createdBy").value("jsmith"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedBy").value("jsmith"))
                .andExpect(jsonPath("$.area").value(78.53981633974483))
                .andExpect(jsonPath("$.perimeter").value(31.41592653589793))
                .andExpect(jsonPath("$.radius").value(5.0));
    }
    @Test
    @DirtiesContext
    void shouldAddSquare() throws Exception {
        String token = loginUserAndGetToken();

        // Wysyłanie żądania dodania kwadratu i potwierdzenie dodania
        ShapeRequestDto addShapeRequestDto = new ShapeRequestDto("SQUARE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type").value("SQUARE"))
                .andExpect(jsonPath("$.createdBy").value("jsmith"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedBy").value("jsmith"))
                .andExpect(jsonPath("$.area").value(25.0))
                .andExpect(jsonPath("$.perimeter").value(20.0))
                .andExpect(jsonPath("$.height").value(5.0));
    }

    @Test
    @DirtiesContext
    void shouldAddRectangle() throws Exception {
        String token = loginUserAndGetToken();

        // Wysyłanie żądania dodania kwadratu i potwierdzenie dodania
        ShapeRequestDto addShapeRequestDto = new ShapeRequestDto("RECTANGLE", List.of(5.0, 2.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type").value("RECTANGLE"))
                .andExpect(jsonPath("$.createdBy").value("jsmith"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedBy").value("jsmith"))
                .andExpect(jsonPath("$.area").value(10.0))
                .andExpect(jsonPath("$.perimeter").value(14.0))
                .andExpect(jsonPath("$.height").value(5.0))
                .andExpect(jsonPath("$.width").value(2.0));
    }

    @Test
    @DirtiesContext
    void shouldReturnBadRequestForInvalidShapeType() throws Exception {
        String token = loginUserAndGetToken();

        // Wysyłanie żądania dodania figury z niepoprawnym typem
        ShapeRequestDto addShapeRequestDto = new ShapeRequestDto("INVALID_TYPE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void shouldDenyAccessForUnauthorizedUser() throws Exception {
        ShapeRequestDto requestDto = new ShapeRequestDto("CIRCLE", Collections.singletonList(5.0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    @DirtiesContext
    void shouldValidateInputDataAndReturnBadRequest() throws Exception {
        String token = loginUserAndGetToken();
        ShapeRequestDto requestDto = new ShapeRequestDto("", Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DirtiesContext
    public void shouldReturnFilteredShapesByAreaTo() throws Exception {
        String token = loginUserAndGetToken();

        // Dodawanie przykładowego koła
        ShapeRequestDto addCircleRequestDto = new ShapeRequestDto("CIRCLE", List.of(5.0));
        String addCircleRequestDtoAsString = mapper.writeValueAsString(addCircleRequestDto);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addCircleRequestDtoAsString))
                .andExpect(status().isCreated());

        // Dodawanie przykładowego kwadratu
        ShapeRequestDto addSquareRequestDto = new ShapeRequestDto("SQUARE", List.of(5.0));
        String addSquareRequestDtoAsString = mapper.writeValueAsString(addSquareRequestDto);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addSquareRequestDtoAsString))
                .andExpect(status().isCreated());

        // Filtracja kształtów
        mockMvc.perform(get("/api/v1/shapes/parameters")
                .header("Authorization", "Bearer " + token)
                //.param("type", "SQUARE")
                .param("areaTo", "30.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].type").value("SQUARE"))
                .andExpect(jsonPath("$.content[0].area").value(25.0))
                .andExpect(jsonPath("$.content[0].perimeter").value(20.0));

    }

    @Test
    @DirtiesContext
    public void shouldReturnFilteredShapesByType() throws Exception {
        String token = loginUserAndGetToken();

        // Dodawanie przykładowego koła
        ShapeRequestDto addCircleRequestDto = new ShapeRequestDto("CIRCLE", List.of(5.0));
        String addCircleRequestDtoAsString = mapper.writeValueAsString(addCircleRequestDto);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addCircleRequestDtoAsString))
                .andExpect(status().isCreated());

        // Dodawanie przykładowego kwadratu
        ShapeRequestDto addSquareRequestDto = new ShapeRequestDto("SQUARE", List.of(5.0));
        String addSquareRequestDtoAsString = mapper.writeValueAsString(addSquareRequestDto);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addSquareRequestDtoAsString))
                .andExpect(status().isCreated());

        // Filtracja kształtów
        mockMvc.perform(get("/api/v1/shapes/parameters")
                .header("Authorization", "Bearer " + token)
                .param("type", "SQUARE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].type").value("SQUARE"))
                .andExpect(jsonPath("$.content[0].area").value(25.0))
                .andExpect(jsonPath("$.content[0].perimeter").value(20.0));
    }

    @Test
    @DirtiesContext
    public void shouldReturnFilteredShapesBetweenAreaTo100_0() throws Exception {
        String token = loginUserAndGetToken();

        // Dodawanie przykładowego koła
        ShapeRequestDto addCircleRequestDto = new ShapeRequestDto("CIRCLE", List.of(5.0));
        String addCircleRequestDtoAsString = mapper.writeValueAsString(addCircleRequestDto);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addCircleRequestDtoAsString))
                .andExpect(status().isCreated());

        // Dodawanie przykładowego kwadratu
        ShapeRequestDto addSquareRequestDto = new ShapeRequestDto("SQUARE", List.of(5.0));
        String addSquareRequestDtoAsString = mapper.writeValueAsString(addSquareRequestDto);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addSquareRequestDtoAsString))
                .andExpect(status().isCreated());

        ShapeRequestDto addSquareRequestDto1 = new ShapeRequestDto("SQUARE", List.of(100.0));
        String addSquareRequestDtoAsString1 = mapper.writeValueAsString(addSquareRequestDto1);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addSquareRequestDtoAsString1))
                .andExpect(status().isCreated());

        // Filtracja kształtów
        mockMvc.perform(get("/api/v1/shapes/parameters")
                .header("Authorization", "Bearer " + token)
                .param("areaTo", "100.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[1].type").value("SQUARE"))
                .andExpect(jsonPath("$.content[1].area").value(25.0))
                .andExpect(jsonPath("$.content[1].perimeter").value(20.0))
                .andExpect(jsonPath("$.content[0].type").value("CIRCLE"))
                .andExpect(jsonPath("$.content[0].area").value(78.5))
                .andExpect(jsonPath("$.content[0].perimeter").value(31.4));
    }

    @Test
    @DirtiesContext
    public void shouldReturnFilteredShapesBetweenPerimeterTo100_0() throws Exception {
        String token = loginUserAndGetToken();

        // Dodawanie przykładowego koła
        ShapeRequestDto addCircleRequestDto = new ShapeRequestDto("CIRCLE", List.of(5.0));
        String addCircleRequestDtoAsString = mapper.writeValueAsString(addCircleRequestDto);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addCircleRequestDtoAsString))
                .andExpect(status().isCreated());

        // Dodawanie przykładowego kwadratu
        ShapeRequestDto addSquareRequestDto = new ShapeRequestDto("SQUARE", List.of(5.0));
        String addSquareRequestDtoAsString = mapper.writeValueAsString(addSquareRequestDto);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addSquareRequestDtoAsString))
                .andExpect(status().isCreated());

        ShapeRequestDto addSquareRequestDto1 = new ShapeRequestDto("SQUARE", List.of(100.0));
        String addSquareRequestDtoAsString1 = mapper.writeValueAsString(addSquareRequestDto1);

        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addSquareRequestDtoAsString1))
                .andExpect(status().isCreated());

        // Filtracja kształtów
        mockMvc.perform(get("/api/v1/shapes/parameters")
                .header("Authorization", "Bearer " + token)
                .param("areaTo", "100.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[1].type").value("SQUARE"))
                .andExpect(jsonPath("$.content[1].area").value(25.0))
                .andExpect(jsonPath("$.content[1].perimeter").value(20.0))
                .andExpect(jsonPath("$.content[0].type").value("CIRCLE"))
                .andExpect(jsonPath("$.content[0].area").value(78.5))
                .andExpect(jsonPath("$.content[0].perimeter").value(31.4));
    }

    @Test
    @DirtiesContext
    public void shouldReturnFilteredShapesWithAreaFrom90_0() throws Exception {
        String token = loginUserAndGetToken();

        // Dodawanie kształtów
        ShapeRequestDto addCircleRequestDto = new ShapeRequestDto("CIRCLE", List.of(5.0));
        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addCircleRequestDto)))
                .andExpect(status().isCreated());

        ShapeRequestDto addSquareRequestDto = new ShapeRequestDto("SQUARE", List.of(10.0));
        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addSquareRequestDto)))
                .andExpect(status().isCreated());

        // Filtracja kształtów na podstawie minimalnej powierzchni
        mockMvc.perform(get("/api/v1/shapes/parameters")
                .header("Authorization", "Bearer " + token)
                .param("areaFrom", "90.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].type").value("SQUARE"))
                .andExpect(jsonPath("$.content[0].area").value(100.0))
                .andExpect(jsonPath("$.content[0].perimeter").value(40.0));

    }

    @Test
    @DirtiesContext
    public void shouldReturnFilteredShapesWithPerimeterFrom35_0() throws Exception {
        String token = loginUserAndGetToken();

        // Dodawanie kształtów
        ShapeRequestDto addCircleRequestDto = new ShapeRequestDto("CIRCLE", List.of(5.0));
        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addCircleRequestDto)))
                .andExpect(status().isCreated());

        ShapeRequestDto addSquareRequestDto = new ShapeRequestDto("SQUARE", List.of(10.0));
        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addSquareRequestDto)))
                .andExpect(status().isCreated());

        ShapeRequestDto addRectangleRequestDto = new ShapeRequestDto("RECTANGLE", List.of(15.0, 20.0));
        mockMvc.perform(post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addRectangleRequestDto)))
                .andExpect(status().isCreated());

        // Filtracja kształtów na podstawie minimalnego obwodu
        mockMvc.perform(get("/api/v1/shapes/parameters")
                .header("Authorization", "Bearer " + token)
                .param("perimeterFrom", "35.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[1].type").value("SQUARE"))
                .andExpect(jsonPath("$.content[1].area").value(100.0))
                .andExpect(jsonPath("$.content[1].perimeter").value(40.0))
                .andExpect(jsonPath("$.content[0].type").value("RECTANGLE"))
                .andExpect(jsonPath("$.content[0].area").value(300.0))
                .andExpect(jsonPath("$.content[0].perimeter").value(70.0));
    }

    private String loginUserAndGetToken() throws Exception {
        UserSignDto userSignDto = loginDto();
        String signUpRequestDtoAsString = mapper.writeValueAsString(userSignDto);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequestDtoAsString))
                .andExpect(status().isCreated());

        JwtRequestDto jwtRequestDto = new JwtRequestDto("jsmith", "password123");
        String jwtRequestDtoAsString = mapper.writeValueAsString(jwtRequestDto);

        MvcResult jwtRequestResponse = mockMvc.perform(post("/api/v1/authenticate")
                .with(httpBasic("jsmith", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestDtoAsString))
                .andReturn();

        return TestUtils.getTokenFromJson(jwtRequestResponse.getResponse().getContentAsString());
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