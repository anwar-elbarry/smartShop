package org.example.smartshop.service.client;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.client.ClientRequest;
import org.example.smartshop.dto.client.ClientResponse;
import org.example.smartshop.dto.client.ClientUpdate;
import org.example.smartshop.entity.Client;
import org.example.smartshop.enums.CustomerTier;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.exception.ResourceNotFoundException;
import org.example.smartshop.mapper.ClientMapper;
import org.example.smartshop.mapper.OrderMapper;
import org.example.smartshop.repository.ClientRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final OrderMapper orderMapper;

    @Override
    public ClientResponse create(ClientRequest request) {

        Client client = clientMapper.toEntity(request);

        client.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        client.setEmail(client.getEmail().toLowerCase());
        client.setUsername(client.getUsername().toLowerCase());

        client.setCustomerTier(CustomerTier.BASIC);
        client.setRole(UserRole.CLIENT);
        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.valueOf(0));

        Client saved = clientRepository.save(client);
        return clientMapper.toResponse(saved);
    }

    @Override
    public ClientResponse update(String clientId, ClientUpdate request) {
        Client client = checkIfClientExist(clientId);
        client.setName(request.getName());
        client.setEmail(request.getEmail().toLowerCase());
        Client updated = clientRepository.save(client);
        return   clientMapper.toResponse(updated);
    }

    @Override
    public void remove(String clientId) {
          Client client =  checkIfClientExist(clientId);
          client.delete();
          clientRepository.save(client);
    }

    @Override
    public Page<ClientResponse> getAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toResponse);
    }

    @Override
    public Optional<ClientResponse> getById(String clientId) {
       Client client = checkIfClientExist(clientId);
        return Optional.of(clientMapper.toResponse(client));
    }

    public Client checkIfClientExist(String clientId){
        return clientRepository.findById(clientId).orElseThrow(
                () -> new ResourceNotFoundException("Client",clientId));
    }

    @Override
    @Transactional
    public void updateClientLoyalty(String clientId, BigDecimal orderAmount) {
        Client client = checkIfClientExist(clientId);

        client.setTotalOrders(client.getTotalOrders() + 1);
        client.setTotalSpent(client.getTotalSpent().add(orderAmount));

        updateCustomerTier(client);

        clientRepository.save(client);
    }

    private void updateCustomerTier(Client client) {
        BigDecimal totalSpent = client.getTotalSpent();

        if (totalSpent.compareTo(new BigDecimal("5000")) >= 0) {
            client.setCustomerTier(CustomerTier.PLATINUM);
        } else if (totalSpent.compareTo(new BigDecimal("2000")) >= 0) {
            client.setCustomerTier(CustomerTier.GOLD);
        } else if (totalSpent.compareTo(new BigDecimal("500")) >= 0) {
            client.setCustomerTier(CustomerTier.SILVER);
        } else {
            client.setCustomerTier(CustomerTier.BASIC);
        }
    }

}
