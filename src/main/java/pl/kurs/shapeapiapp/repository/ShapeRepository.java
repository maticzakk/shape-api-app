package pl.kurs.shapeapiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.shapeapiapp.model.Shape;

public interface ShapeRepository extends JpaRepository<Shape, Long> {

}
