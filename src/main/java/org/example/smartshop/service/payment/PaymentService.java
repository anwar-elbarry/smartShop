package org.example.smartshop.service.payment;

import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.dto.payment.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService{
    Page<PaymentResponse> getAll(Pageable pageable,String clientId);
    PaymentResponse getOrderPayments(String orderId);
    boolean isOrderFullyPaid(String orderId);
    PaymentResponse processPayment(PaymentRequest request);
}
