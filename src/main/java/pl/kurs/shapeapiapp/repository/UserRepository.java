package pl.kurs.shapeapiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.shapeapiapp.model.User;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = ?1")
    Optional<User> findByUsernameFetchRoles(String username);

    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    boolean existsWithLockingByEmail(String email);
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    boolean existsWithLockingByUsername(String username);

}
