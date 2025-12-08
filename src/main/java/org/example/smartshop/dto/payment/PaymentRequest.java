package org.example.smartshop.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.example.smartshop.enums.TypePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentRequest {
    @NotNull
    private String orderId;
    private Integer numeroPaiement;
    @NotNull
    @Positive
    private BigDecimal montant;
    private LocalDate dateEncaissement;
    private TypePaiement typePaiement;
    private String numeroCheque;
    private String bank;
    private LocalDate chequeEcheance;
    private String reference;
    private String recu;
}
