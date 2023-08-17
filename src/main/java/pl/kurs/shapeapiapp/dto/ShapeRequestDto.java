package pl.kurs.shapeapiapp.dto;

import pl.kurs.shapeapiapp.validation.ShapeType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ShapeRequestDto {
    @NotBlank
    @ShapeType
    private String type;
    @NotEmpty
    private List<Double> parameters;

    public ShapeRequestDto() {
    }

    public ShapeRequestDto(String type, List<Double> parameters) {
        this.type = type;
        this.parameters = parameters;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getParameters() {
        return parameters;
    }

    public void setParameters(List<Double> parameters) {
        this.parameters = parameters;
    }
}
