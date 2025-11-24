package de.othr.hwwa.repository.impl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.othr.hwwa.model.User;
import de.othr.hwwa.repository.UserRepositoryI;

@Repository
public interface UserRepositoryImpl extends UserRepositoryI, CrudRepository<User, Long>{
}
