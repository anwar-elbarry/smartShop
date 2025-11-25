package org.example.smartshop.dto.order;

import lombok.Builder;
import lombok.Data;
import org.example.smartshop.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String clientId;
    private String clientName; // Utile pour l'affichage
    private LocalDateTime date;
    private OrderStatus statut;

    // Détails financiers calculés
    private BigDecimal sousTotalHt;
    private BigDecimal montantRemise; // Fidélité + Promo
    private BigDecimal tva;
    private BigDecimal totalTTC;
    private BigDecimal montantRestant; // Important pour savoir si on peut valider

    private List<OrderResponse> items;
}
