package org.example.smartshop.controller.order;

import org.example.smartshop.dto.order.OrderRequest;
import org.example.smartshop.dto.order.OrderResponse;
import org.example.smartshop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface OrderController {
    ResponseEntity<?> create(OrderRequest request);
    ResponseEntity<?> getById(String orderId);
    ResponseEntity<Page<OrderResponse>> getAll(Pageable pageable);
    ResponseEntity<Page<OrderResponse>> getByClientId(String clientId,Pageable pageable);
    ResponseEntity<Page<OrderResponse>> findByStatus(OrderStatus status, Pageable pageable);

}
