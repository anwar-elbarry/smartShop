package org.example.smartshop.controller.client;

import org.example.smartshop.dto.client.ClientRequest;
import org.example.smartshop.dto.client.ClientUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ClientController {
    ResponseEntity<?> create(ClientRequest request);
    ResponseEntity<?> update(ClientUpdate request,String clientId);
    ResponseEntity<?> delete(String clientId);
    ResponseEntity<Page<?>> getAll(Pageable pageable);
    ResponseEntity<?> getById(String clientId);
}
