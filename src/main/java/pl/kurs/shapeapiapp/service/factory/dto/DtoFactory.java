package pl.kurs.shapeapiapp.service.factory.dto;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.strategy.IDtoStrategy;

import java.util.Map;

@Service
public class DtoFactory {
    private final Map<String, IDtoStrategy> dtoStrategyMap;

    public DtoFactory(Map<String, IDtoStrategy> dtoStrategyMap) {
        this.dtoStrategyMap = dtoStrategyMap;
    }

    public ShapeDto createDto(String shape) {
        return this.dtoStrategyMap.get(shape+"DTO").createDto();
    }
}
