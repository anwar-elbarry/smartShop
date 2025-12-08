package org.example.smartshop.service.payment;

import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.dto.payment.PaymentResponse;
import org.example.smartshop.dto.payment.UpdatePaymentStatusRequest;
import org.example.smartshop.entity.Order;
import org.example.smartshop.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PaymentService{
    Page<PaymentResponse> getAll(Pageable pageable,String clientId);
    Page<PaymentResponse> getOrderPayments(String orderId,Pageable pageable);
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse updatePaymentStatus(String paymentId, PaymentStatus newStatus, LocalDate encaissementDate);
    boolean isOrderFullyPaid (String orderId);
    void validateStatusUpdateRequest(UpdatePaymentStatusRequest request);
}
