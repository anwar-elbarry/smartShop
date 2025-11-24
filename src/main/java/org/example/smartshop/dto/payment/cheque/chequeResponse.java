package org.example.smartshop.dto.payment.cheque;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.smartshop.dto.payment.PaymentResponse;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class chequeResponse extends PaymentResponse {
    private String numeroCheque;
    private String bank;
    private LocalDateTime chequeEcheance;
}
