package edu.njit.cs631.repositories;

import edu.njit.cs631.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long>{
    Optional<CustomUser> findByEmail(String email);
}
