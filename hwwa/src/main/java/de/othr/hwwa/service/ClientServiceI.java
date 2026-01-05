package de.othr.hwwa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.othr.hwwa.model.Client;

public interface ClientServiceI {

    Page<Client> findAll(Pageable pageable);

    Client findById(Long id);

    Client save(Client client);

    void deleteById(Long id);
}
