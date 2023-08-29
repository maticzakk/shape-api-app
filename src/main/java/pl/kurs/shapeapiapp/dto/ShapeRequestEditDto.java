package pl.kurs.shapeapiapp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ShapeRequestEditDto {
    private List<Double> parameters;
    private int version;

    @JsonCreator
    public ShapeRequestEditDto(@JsonProperty("parameters") List<Double> parameters) {
        this.parameters = parameters;
    }

    public List<Double> getParameters() {
        return parameters;
    }

    public void setParameters(List<Double> parameters) {
        this.parameters = parameters;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
