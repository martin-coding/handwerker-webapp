package de.othr.hwwa.repository.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.othr.hwwa.model.User;
import de.othr.hwwa.repository.UserRepositoryI;

@Repository
@Primary
public interface UserRepositoryImpl extends UserRepositoryI{
}
