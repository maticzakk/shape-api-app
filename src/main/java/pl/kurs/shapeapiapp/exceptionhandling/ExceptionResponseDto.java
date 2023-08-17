package pl.kurs.shapeapiapp.exceptionhandling;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ExceptionResponseDto {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ExceptionResponseDto(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ExceptionResponseDto{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}
