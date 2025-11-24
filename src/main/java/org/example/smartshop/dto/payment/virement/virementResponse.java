package org.example.smartshop.dto.payment.virement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.smartshop.dto.payment.PaymentResponse;
import org.example.smartshop.enums.TypePaiement;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class virementResponse extends PaymentResponse {
    private String reference;
    private String bank;
}
