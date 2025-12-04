package org.example.smartshop.service.order;

import org.example.smartshop.dto.order.OrderRequest;
import org.example.smartshop.dto.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    Page<OrderResponse> getAll(Pageable pageable);
    OrderResponse getById(String orderId);
}