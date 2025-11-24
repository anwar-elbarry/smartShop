package org.example.smartshop.dto.payment.especes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.enums.TypePaiement;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class EspecesRequest extends PaymentRequest {
    private String recu;
    private TypePaiement typePaiement= TypePaiement.ESPECES;
}
