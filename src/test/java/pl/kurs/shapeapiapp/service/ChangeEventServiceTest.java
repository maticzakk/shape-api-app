package pl.kurs.shapeapiapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.kurs.shapeapiapp.dto.ShapeChangeDto;
import pl.kurs.shapeapiapp.model.ChangeEvent;
import pl.kurs.shapeapiapp.model.Role;
import pl.kurs.shapeapiapp.model.Shape;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.ChangeEventRepository;
import pl.kurs.shapeapiapp.repository.UserRepository;
import pl.kurs.shapeapiapp.service.implementation.ChangeEventService;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChangeEventServiceTest {

    @InjectMocks
    private ChangeEventService changeManager;
    @Mock
    private ChangeEventRepository changeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;


    @Test
    void should_save_change() {
        Shape editedShape = new Shape();
        editedShape.setId(1L);
        editedShape.setLastModifiedBy("user1");
        editedShape.setLastModifiedAt(LocalDateTime.now());

        User createdBy = new User();
        createdBy.setUsername("user2");
        editedShape.setCreatedBy(createdBy);

        Map<String, Double> oldProperties = new HashMap<>();
        oldProperties.put("length", 10.0);
        oldProperties.put("width", 5.0);

        User user = new User();
        user.setUsername("user1");
        Role role = new Role();
        role.setName("CREATOR");
        user.setRoles(Set.of(role));

        changeManager.save(1L, editedShape, "user1", oldProperties);

        verify(changeRepository, times(1)).saveAndFlush(any(ChangeEvent.class));

        ArgumentCaptor<ChangeEvent> changeCaptor = ArgumentCaptor.forClass(ChangeEvent.class);
        Mockito.verify(changeRepository).saveAndFlush(changeCaptor.capture());
        ChangeEvent savedChange = changeCaptor.getValue();

        assertEquals(editedShape.getId(), savedChange.getShapeId());
        assertEquals(editedShape.getLastModifiedBy(), savedChange.getLastModifiedBy());
        assertEquals(editedShape.getLastModifiedAt(), savedChange.getLastModifiedAt());
        assertEquals("user2", savedChange.getAuthor());
        assertEquals(oldProperties, savedChange.getChangedValues());
    }

    @Test
    void should_get_list_of_changes() {

        long shapeId = 1L;
        List<ChangeEvent> changes = new ArrayList<>();
        ChangeEvent change1 = new ChangeEvent();
        change1.setId(1L);
        change1.setShapeId(shapeId);
        ChangeEvent change2 = new ChangeEvent();
        change2.setId(2L);
        change2.setShapeId(shapeId);
        changes.add(change1);
        changes.add(change2);

        ShapeChangeDto dto1 = new ShapeChangeDto();
        ShapeChangeDto dto2 = new ShapeChangeDto();

        Mockito.when(modelMapper.map(change1, ShapeChangeDto.class)).thenReturn(dto1);
        Mockito.when(modelMapper.map(change2, ShapeChangeDto.class)).thenReturn(dto2);

        Mockito.when(changeRepository.findAllByShapeId(shapeId)).thenReturn(changes);

        List<ShapeChangeDto> result = changeManager.getChanges(shapeId);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

}
