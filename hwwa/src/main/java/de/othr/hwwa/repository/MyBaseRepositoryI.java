package de.othr.hwwa.repository;

import java.io.Serializable;
import java.util.Optional;

public interface MyBaseRepositoryI <T, ID extends Serializable> {

    <S extends T> S save(S entity);

    Optional<T> findById(ID id);

    Iterable<T> findAll();

    void delete(T entity);

}