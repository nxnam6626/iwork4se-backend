package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.Role;
import iuh.fit.se.iwork4se.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmailOrPhone(String email, String phone);
    
    // Statistics methods
    long countByRole(Role role);
}
