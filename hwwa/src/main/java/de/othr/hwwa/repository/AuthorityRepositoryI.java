package de.othr.hwwa.repository;

import de.othr.hwwa.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepositoryI extends JpaRepository <Authority,Long>{
    Optional<Authority> findByName(String name);
}
