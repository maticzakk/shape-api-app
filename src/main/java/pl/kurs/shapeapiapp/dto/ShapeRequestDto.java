package pl.kurs.shapeapiapp.dto;

import java.util.List;

public class ShapeRequestDto {
    private String type;
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
