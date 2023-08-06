package pl.kurs.shapeapiapp.strategy;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.RectangleDto;
import pl.kurs.shapeapiapp.dto.ShapeDto;

@Service("RECTANGLEDTO")
public class RectangleDtoStrategy implements IDtoStrategy {

    @Override
    public ShapeDto createDto() {
        return new RectangleDto();
    }
}
