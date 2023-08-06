package pl.kurs.shapeapiapp.strategy;

import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.dto.ShapeDto;
import pl.kurs.shapeapiapp.dto.SquareDto;

@Service("SQUAREDTO")
public class SquareDtoStrategy implements IDtoStrategy {

    @Override
    public ShapeDto createDto() {
        return new SquareDto();
    }
}
