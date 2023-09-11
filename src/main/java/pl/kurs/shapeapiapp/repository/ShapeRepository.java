package pl.kurs.shapeapiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.kurs.shapeapiapp.model.Shape;

import java.util.Optional;


public interface ShapeRepository extends JpaRepository<Shape, Long>, QuerydslPredicateExecutor<Shape> {

    @Query("SELECT s.type FROM Shape s WHERE s.id = ?1")
    Optional<String> getTypeById(long id);


    @Query("SELECT s.createdBy.username FROM Shape s WHERE s.id = ?1")
    Optional<String> getCreatedByUsernameById(long id);

}
