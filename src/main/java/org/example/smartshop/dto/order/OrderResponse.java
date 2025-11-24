package org.example.smartshop.dto.order;

import org.example.smartshop.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private Long clientId;
    private String clientName; // Utile pour l'affichage
    private LocalDateTime date;
    private OrderStatus status;

    // Détails financiers calculés
    private BigDecimal sousTotalHT;
    private BigDecimal montantRemise; // Fidélité + Promo
    private BigDecimal montantTVA;
    private BigDecimal totalTTC;
    private BigDecimal montantRestant; // Important pour savoir si on peut valider

    private List<OrderResponse> items;
}
