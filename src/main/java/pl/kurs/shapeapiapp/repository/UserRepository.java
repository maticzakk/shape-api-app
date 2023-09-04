package pl.kurs.shapeapiapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kurs.shapeapiapp.model.User;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = ?1")
    Optional<User> findByUsernameFetchRoles(String username);
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    boolean existsWithLockingByEmail(String email);
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    boolean existsWithLockingByUsername(String username);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :userId")
    User findUserWithRoles(@Param("userId") Long userId);


    @Query("SELECT u.id FROM User u")
    Page<Long> findIds(Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH u.shapes WHERE u.id IN :id")
    List<User> findAllWithRolesAndShapesByIds(@Param("id") List<Long> ids);
}
