package de.othr.hwwa.repository;

import java.util.Optional;
import de.othr.hwwa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;

public interface UserRepositoryI extends JpaRepository<User, Long> {

    Optional<User> findUserByUserName(String userName);
    Optional<User> findUserByEmailIgnoreCase(String email);

}