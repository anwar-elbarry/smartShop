package org.example.smartshop.dto.payment;

import lombok.Data;
import org.example.smartshop.enums.PaymentStatus;

import java.time.LocalDate;

@Data
public class UpdatePaymentStatusRequest {
    private PaymentStatus newStatus;
    private LocalDate encaissementDate;
}
