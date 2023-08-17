package pl.kurs.shapeapiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.shapeapiapp.model.Rectangle;

public interface RectangleRepository extends JpaRepository<Rectangle, Long> {
}
