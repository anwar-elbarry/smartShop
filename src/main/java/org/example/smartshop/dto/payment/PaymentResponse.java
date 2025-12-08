package org.example.smartshop.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smartshop.enums.TypePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponse {
    private String id;
    private String orderId;
    private Integer numeroPaiement;
    private BigDecimal montant;
    private String datePaiement;
    private LocalDate dateEncaissement;
    private TypePaiement typePaiement;
    private String recu;
    private String reference;
    private String bank;
    private String numeroCheque;
    private LocalDate chequeEcheance;

}
