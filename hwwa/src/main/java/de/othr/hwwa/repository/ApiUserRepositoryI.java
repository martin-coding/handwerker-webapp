package de.othr.hwwa.repository;

import de.othr.hwwa.model.jwt.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUserRepositoryI extends JpaRepository<ApiUser, Long> {
    public Optional<ApiUser> findByEmail(String email);
}
