package pl.kurs.shapeapiapp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHANGES")
public class ChangeEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long shapeId;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    private String author;
    @ElementCollection
    @CollectionTable(name = "change_properties", joinColumns = {@JoinColumn(name = "change_id")})
    @MapKeyColumn(name = "name")
    @Column(name = "num_value")
    private Map<String, Double> changedValues = new HashMap<>();
}
