package pl.kurs.shapeapiapp.strategy;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.CircleDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;

@Service("CIRCLEDTO")
public class CircleDtoStrategy implements IDtoStrategy{

    @Override
    public ShapeDto createDto() {
        return new CircleDto();
    }
}
