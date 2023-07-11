package pl.kurs.shapeapiapp.model;
import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Shape> shapes = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(Set<Shape> shapes) {
        this.shapes = shapes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles) && Objects.equals(shapes, user.shapes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, roles, shapes);
    }

    //metoda dodająca rolę użytkownika
    public void addRole(Role role) {
        roles.add(role);
    }

    //metoda usuwająca rolę użytkownika
    public void removeRoles(Role role) {
        roles.remove(role);
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }


}
