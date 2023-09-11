package pl.kurs.shapeapiapp.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shapeapiapp.dto.ShapeChangeDto;
import pl.kurs.shapeapiapp.model.ChangeEvent;
import pl.kurs.shapeapiapp.model.Shape;
import pl.kurs.shapeapiapp.repository.ChangeEventRepository;
import pl.kurs.shapeapiapp.service.IChangeEventService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChangeEventService implements IChangeEventService {

    private final ChangeEventRepository changeEventRepository;
    private final ModelMapper modelMapper;

    public ChangeEventService(ChangeEventRepository changeEventRepository, ModelMapper modelMapper) {
        this.changeEventRepository = changeEventRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void save(Long id, Shape editedShape, String username, Map<String, Double> oldProperties) {
        ChangeEvent change = new ChangeEvent();
        change.setShapeId(id);
        change.setLastModifiedBy(editedShape.getLastModifiedBy());
        change.setLastModifiedAt(editedShape.getLastModifiedAt());
        change.setAuthor(editedShape.getCreatedBy().getUsername());
        change.setChangedValues(oldProperties);
        changeEventRepository.saveAndFlush(change);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ShapeChangeDto> getChanges(long shapeId) {
        return changeEventRepository.findAllByShapeId(shapeId).stream()
                .map(x -> modelMapper.map(x, ShapeChangeDto.class)).collect(Collectors.toList());
    }
}
