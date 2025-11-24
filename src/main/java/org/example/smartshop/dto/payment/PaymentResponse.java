package org.example.smartshop.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.smartshop.entity.Order;
import org.example.smartshop.enums.TypePaiement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponse {
    private String id;
    private Order order;
    private Integer numeroPaiement;
    private BigDecimal montant;
    private String datePaiement;
    private LocalDateTime dateEncaissement;
    private TypePaiement typePaiement;
}
