package org.example.smartshop.dto.client;

import lombok.Builder;
import lombok.Data;
import org.example.smartshop.dto.order.OrderResponse;
import org.example.smartshop.entity.Order;
import org.example.smartshop.enums.CustomerTier;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class ClientResponse {
    private String id;
    private String name;
    private String username;
    private String email;
    private CustomerTier customerTier;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private List<OrderResponse> commandHistory;
}
