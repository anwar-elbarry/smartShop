package org.example.smartshop.controller.payment;

import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.dto.payment.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PaymentControlller {
    ResponseEntity<?> processPayment(PaymentRequest request);
    ResponseEntity<Page<PaymentResponse>> getAllPayments(Pageable pageable, String clientId);
    ResponseEntity<?> getOrderPayments(String orderId);
    ResponseEntity<Boolean> isOrderFullyPaid(String orderId);
}
