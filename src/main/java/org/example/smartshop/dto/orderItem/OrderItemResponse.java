package org.example.smartshop.dto.orderItem;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class OrderItemResponse {
    private String productName;
    private Integer quantity;
    private BigDecimal prixUnitaire; // Prix figé au moment de la commande
    private BigDecimal totalLigne;
}
