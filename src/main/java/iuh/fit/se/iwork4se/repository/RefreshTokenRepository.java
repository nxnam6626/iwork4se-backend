package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {}
