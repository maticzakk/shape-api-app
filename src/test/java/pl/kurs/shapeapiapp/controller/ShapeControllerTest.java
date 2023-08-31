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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.shapeapiapp.dto.*;
import pl.kurs.shapeapiapp.model.Shape;
import pl.kurs.shapeapiapp.repository.ShapeRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.security.jwt.JwtRequestDto;
import pl.kurs.shapeapiapp.utils.TestUtils;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ShapeControllerTest {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShapeRepository shapeRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        shapeRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
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
        List<Shape> savedShape = shapeRepository.findAll();
        assertThat(savedShape).hasSize(1);

    }
    @Test
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

        List<Shape> savedShape = shapeRepository.findAll();
        assertThat(savedShape).hasSize(1);
    }

    @Test
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

        List<Shape> savedShape = shapeRepository.findAll();
        assertThat(savedShape).hasSize(1);
    }

    @Test
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
    void shouldDenyAccessForUnauthorizedUser() throws Exception {
        ShapeRequestDto requestDto = new ShapeRequestDto("CIRCLE", Collections.singletonList(5.0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
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
                .param("areaTo", "30.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].type").value("SQUARE"))
                .andExpect(jsonPath("$.content[0].area").value(25.0))
                .andExpect(jsonPath("$.content[0].perimeter").value(20.0));

    }

    @Test
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
                .andExpect(jsonPath("$.content[0].area").value(78.53981633974483))
                .andExpect(jsonPath("$.content[0].perimeter").value(31.41592653589793));
    }

    @Test
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
                .andExpect(jsonPath("$.content[0].area").value(78.53981633974483))
                .andExpect(jsonPath("$.content[0].perimeter").value(31.41592653589793));
    }

    @Test
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

    @Test
    void shouldMakeChangesInShape() throws Exception {
        String token = loginUserAndGetToken();

        // Wysyłanie żądania dodania kwadratu i potwierdzenie dodania
        ShapeRequestDto addShapeRequestDto = new ShapeRequestDto("SQUARE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        SquareDto squareDto = mapper.readValue(result.getResponse().getContentAsString(), SquareDto.class);
        long id = squareDto.getId();

        ShapeRequestEditDto editDto = new ShapeRequestEditDto(List.of(10.0));
        String editDtoAsString = mapper.writeValueAsString(editDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(editDtoAsString))
                .andDo(print())
                .andExpect(status().isOk()) // Oczekiwany status 200 OK
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.type").value("SQUARE"))
                .andExpect(jsonPath("$.createdBy").value("jsmith"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedBy").value("jsmith"))
                .andExpect(jsonPath("$.area").value(100.0))
                .andExpect(jsonPath("$.perimeter").value(40.0))
                .andExpect(jsonPath("$.height").value(10.0));

    }

    @Test
    void shouldReturn404WhenShapeDoesNotExist() throws Exception {
        String token = loginUserAndGetToken();

        ShapeRequestEditDto editDto = new ShapeRequestEditDto(List.of(10.0));
        String editDtoAsString = mapper.writeValueAsString(editDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/shapes/{id}", 999) // Id nieistniejące
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(editDtoAsString))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldHandleOptimisticLocking() throws Exception {
        String token = loginUserAndGetToken();

        ShapeRequestDto addShapeRequestDto = new ShapeRequestDto("RECTANGLE", List.of(5.0, 2.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        MvcResult addResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addShapeRequestDtoAsString))
                .andExpect(status().isCreated())
                .andReturn();

        ShapeDto addedShapeDto = mapper.readValue(addResult.getResponse().getContentAsString(), ShapeDto.class);

        // Pobranie ID dodanego prostokąta
        Long rectangleId = addedShapeDto.getId();

        // Edycja prostokąta przez pierwszego użytkownika
        ShapeRequestEditDto editShapeRequestDto = new ShapeRequestEditDto(List.of(10.0, 3.0));
        String editShapeRequestDtoAsString = mapper.writeValueAsString(editShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + rectangleId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(editShapeRequestDtoAsString))
                .andExpect(status().isOk());

        String anotherToken = loginUser2AndGetToken();

        ShapeRequestEditDto editShapeRequestDtoAnotherUser = new ShapeRequestEditDto(List.of(15.0, 4.0));
        String editShapeRequestDtoAnotherUserAsString = mapper.writeValueAsString(editShapeRequestDtoAnotherUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/shapes/" + rectangleId)
                .header("Authorization", "Bearer " + anotherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(editShapeRequestDtoAnotherUserAsString))
                .andExpect(status().isConflict());
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
        request.setLastName("Smith");
        request.setUsername("jsmith");
        request.setEmail("jsmith@gmail.com");
        request.setPassword("password123");
        return request;
    }
}
