package pl.kurs.shapeapiapp.dto;

import java.util.List;

public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private List<String> role;
    private long shapesCreated;

    public UserDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public long getShapesCreated() {
        return shapesCreated;
    }

    public void setShapesCreated(long shapesCreated) {
        this.shapesCreated = shapesCreated;
    }
}
