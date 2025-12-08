package org.example.smartshop.service.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.dto.payment.PaymentResponse;
import org.example.smartshop.entity.Order;
import org.example.smartshop.entity.Payment;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.enums.PaymentStatus;
import org.example.smartshop.exception.ResourceNotFoundException;
import org.example.smartshop.mapper.PaymentMapper;
import org.example.smartshop.repository.OrderRepository;
import org.example.smartshop.repository.PaymentRepository;

import org.example.smartshop.service.client.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ClientService clientService;


    @Override
    public PaymentResponse processPayment(PaymentRequest request){
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.getOrderId()));

        validateOrderForPayment(order);
        validatePaymentAmount(order, request.getMontant());

        Payment payment = paymentMapper.toEntity(request);

        payment.setNumeroPaiement(generatePaymentNumber(order));
        payment.setDatePaiement(LocalDate.now());
        payment.setStatus(PaymentStatus.EN_ATTENTE);
        payment.setTypePaiement(request.getTypePaiement());

        Payment savedPayment = paymentRepository.save(payment);
        updateOrderPaymentStatus(order, payment.getMontant());

        return paymentMapper.toResponse(savedPayment);
    }

    private void validateOrderForPayment(Order order) {
        if (order.getStatut() == OrderStatus.CANCELED) {
            throw new InvalidOperationException("Cannot process payment for a canceled order");
        }
        if (order.getStatut() == OrderStatus.CONFIRMED) {
            throw new InvalidOperationException("Order is already confirmed and paid");
        }
        if (order.getClient() == null) {
            throw new InvalidOperationException("Cannot process payment for an order without a client");
        }
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new InvalidOperationException("Cannot process payment for an order without items");
        }
    }

    private void validatePaymentAmount(Order order, BigDecimal paymentAmount) {
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Payment amount must be greater than zero");
        }

        BigDecimal remainingAmount = order.getMontantRestant();
        if (paymentAmount.compareTo(remainingAmount) > 0) {
            throw new InvalidOperationException(
                    String.format("Payment amount (%.2f) exceeds remaining amount (%.2f)",
                            paymentAmount, remainingAmount));
        }
    }
    @Override
    public Page<PaymentResponse> getAll(Pageable pageable, String clientId) {
        return null;
    }

    @Override
    public PaymentResponse getOrderPayments(String orderId) {
        return null;
    }

    private int generatePaymentNumber(Order order) {
        return paymentRepository.countByOrder(order) + 1;
    }

    @Override
    public boolean isOrderFullyPaid(String orderId) {
        return false;
    }

    @Transactional
    protected void updateOrderPaymentStatus(Order order, BigDecimal paymentAmount) {
        BigDecimal newRemainingAmount = order.getMontantRestant().subtract(paymentAmount)
                .setScale(2, RoundingMode.HALF_UP);
        order.setMontantRestant(newRemainingAmount);

        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            order.setStatut(OrderStatus.CONFIRMED);
            clientService.updateClientLoyalty(order.getClient().getId(), order.getTotalTTC());
        }
        orderRepository.save(order);
    }
}
