package pl.kurs.shapeapiapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.kurs.shapeapiapp.ShapeApiAppApplication;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestDto;
import pl.kurs.shapeapiapp.dto.ShapeRequestEditDto;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.security.jwt.JwtRequestDto;
import pl.kurs.shapeapiapp.utils.TestUtils;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShapeApiAppApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ShapeServiceTest {


    @Autowired
    private ShapeManager shapeService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldUpdateShapeMethodThrowOptimisticLockingExceptionTryingToUpdateFigureAtTheSameTime() throws Exception {
        String token = loginUserAndGetToken();
        //given
        ShapeRequestDto addShapeRequestDto = new ShapeRequestDto("RECTANGLE", List.of(5.0, 2.0));

        ShapeDto shape = shapeService.saveShape(addShapeRequestDto, "jsmith");
        Long shapeId = shape.getId();

        ShapeRequestEditDto update = new ShapeRequestEditDto(List.of(8.0, 2.0));
        ShapeRequestEditDto update2 = new ShapeRequestEditDto(List.of(15.0, 3.0));

        //when - then
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<Void> task1 = () -> {
            shapeService.editShape(shapeId, update, getUsernameFromContext());
            return null;
        };

        Callable<Void> task2 = () -> {
            shapeService.editShape(shapeId, update2, getUsernameFromContext());
            return null;
        };

        Future<Void> future1 = executorService.submit(task1);
        Future<Void> future2 = executorService.submit(task2);


        try {
            future1.get();
            future2.get();
            Assertions.fail("Expected an ObjectOptimisticLockingFailureException to be thrown");
        } catch (ExecutionException | InterruptedException e) {
            Throwable cause = e.getCause();
            System.out.println(cause);
            assertTrue(cause instanceof ObjectOptimisticLockingFailureException);
            assertTrue(cause.getClass().getSimpleName().contains("ObjectOptimisticLockingFailureException"));
        }

        executorService.shutdown();
    }

    private String getUsernameFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
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
