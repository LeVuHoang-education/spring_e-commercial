package dev.backend.ecommercial.repository;

import dev.backend.ecommercial.model.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByUserName(String userName);
    Optional<User> findById(Long userId);
    boolean existsByEmail(String email);


}
