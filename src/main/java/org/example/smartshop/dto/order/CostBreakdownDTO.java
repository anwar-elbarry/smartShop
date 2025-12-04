package org.example.smartshop.dto.order;

import lombok.Builder;
import lombok.Data;
import org.example.smartshop.enums.OrderStatus;

import java.math.BigDecimal;

@Data
@Builder
public class CostBreakdownDTO {
    private BigDecimal sousTotalHt;
    private BigDecimal montantRemise; // Fidélité + Promo
    private BigDecimal tva;
    private BigDecimal totalTTC;
    private BigDecimal montantRestant;
    private OrderStatus status;
}
