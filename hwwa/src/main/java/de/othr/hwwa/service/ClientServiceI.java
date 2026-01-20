package de.othr.hwwa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.othr.hwwa.model.Client;
import de.othr.hwwa.model.dto.ClientTaskCountView;

public interface ClientServiceI {

    Page<Client> findAll(Pageable pageable);

    Page<Client> search(String keyword, Pageable pageable);

    Page<ClientTaskCountView> searchWithTaskCounts(String keyword, Pageable pageable);

    Client findById(Long id);

    Client save(Client client);

    void softDeleteById(Long id);

    void deleteById(Long id);
}
