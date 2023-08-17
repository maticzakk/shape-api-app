package pl.kurs.shapeapiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.shapeapiapp.model.Circle;

public interface CircleRepository extends JpaRepository<Circle, Long> {
}
