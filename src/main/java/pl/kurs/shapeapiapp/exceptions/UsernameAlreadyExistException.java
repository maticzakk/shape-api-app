package pl.kurs.shapeapiapp.exceptions;

public class UsernameAlreadyExistException extends RuntimeException {

    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
