package pl.kurs.shapeapiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.shapeapiapp.model.Square;

public interface SquareRepository extends JpaRepository<Square, Long> {
}
