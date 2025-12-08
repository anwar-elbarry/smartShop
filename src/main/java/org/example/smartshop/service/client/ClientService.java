package org.example.smartshop.service.client;

import org.example.smartshop.dto.client.ClientRequest;
import org.example.smartshop.dto.client.ClientResponse;
import org.example.smartshop.dto.client.ClientUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface ClientService {
    ClientResponse create(ClientRequest request);
    ClientResponse update(String clientId, ClientUpdate clientUpdate);
    void remove(String clientId);
    Page<ClientResponse> getAll(Pageable pageable);
    Optional<ClientResponse> getById(String clientId);
    void updateClientLoyalty(String clientId, BigDecimal orderAmount);
}