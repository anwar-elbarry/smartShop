package org.example.smartshop.dto.client;

import lombok.Builder;
import lombok.Data;
import org.example.smartshop.enums.CustomerTier;

import java.math.BigDecimal;

@Builder
@Data
public class ClientResponse {
    private String id;
    private String nom;
    private String email;
    private CustomerTier tier;
    private Integer totalOrders;
    private BigDecimal totalSpent;
}
