package pl.kurs.shapeapiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.shapeapiapp.model.ChangeEvent;

import java.util.List;

public interface ChangeEventRepository extends JpaRepository<ChangeEvent, Long> {
    List<ChangeEvent> findAllByShapeId(Long shapeId);
}
