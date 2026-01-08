package de.othr.hwwa.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import de.othr.hwwa.model.Client;
import de.othr.hwwa.service.ClientServiceI;
import de.othr.hwwa.repository.ClientRepository;

@Service
@Transactional
public class ClientServiceImpl implements ClientServiceI {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Page<Client> search(String keyword, Pageable pageable) {
        return clientRepository.search(keyword, pageable);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client save(Client client) {
        if (client.getCreatedAt() == null) {
            client.setCreatedAt(LocalDateTime.now());
        }
        return clientRepository.save(client);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
