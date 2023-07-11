package pl.kurs.shapeapiapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/*Klasa reprezentująca role, które mogą być przypisane do użytkownika. Rola określa zestaw uprawnień i funkcji, które są
* przyznawane użytkownikowi w kontekście aplikacji. */
@Entity
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name; //nazwa roli np. "CREATOR"

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
