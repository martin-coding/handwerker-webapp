package de.othr.hwwa.repository;

import java.util.Optional;
import de.othr.hwwa.model.User;

public interface UserRepositoryI extends MyBaseRepositoryI<User, Long> {

    Optional<User> findByLoginIgnoreCase(String login);

}