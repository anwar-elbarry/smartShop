package org.example.smartshop.dto.payment.cheque;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.enums.TypePaiement;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class chequeRequest extends PaymentRequest {
    @NotNull
    private String numeroCheque;

    @NotNull
    private String bank;

    @NotNull
    private LocalDateTime chequeEcheance;

    private TypePaiement typePaiement= TypePaiement.CHEQUE;
}
