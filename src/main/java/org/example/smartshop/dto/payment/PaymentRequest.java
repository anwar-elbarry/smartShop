package org.example.smartshop.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@SuperBuilder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequest {
    @NotNull
    private String OrderId;

    @NotNull
    private Integer numeroPaiement;

    @NotNull
    @Positive
    private BigDecimal montant;

    @NotNull
    private String datePaiement;

    private LocalDateTime dateEncaissement;
}
