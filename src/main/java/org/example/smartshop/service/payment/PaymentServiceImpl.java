package org.example.smartshop.service.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.dto.payment.PaymentResponse;
import org.example.smartshop.dto.payment.UpdatePaymentStatusRequest;
import org.example.smartshop.entity.Order;
import org.example.smartshop.entity.Payment;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.enums.PaymentStatus;
import org.example.smartshop.enums.TypePaiement;
import org.example.smartshop.exception.InvalidOperationException;
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


    @Transactional
    @Override
    public PaymentResponse processPayment(PaymentRequest request){
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.getOrderId()));

        validateOrderForPayment(order);
        validatePaymentAmount(order, request.getMontant());
        validatePaymentType(request);

        Payment payment = paymentMapper.toEntity(request);

        // Définir le statut initial en fonction du type de paiement
        payment.setStatus(getInitialStatusForType(request.getTypePaiement()));
        payment.setNumeroPaiement(generatePaymentNumber(order));
        payment.setDatePaiement(LocalDate.now());

        // Pour les espèces, la date d'encaissement est la même que la date de paiement
        if (request.getTypePaiement() == TypePaiement.ESPECES) {
            payment.setDateEncaissement(LocalDate.now());
        }

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
        return paymentRepository.findAll(pageable).map(paymentMapper::toResponse);
    }

    @Override
    public Page<PaymentResponse> getOrderPayments(String orderId,Pageable pageable) {
        return paymentRepository.findByOrderId(orderId,pageable).map(paymentMapper::toResponse);
    }

    private int generatePaymentNumber(Order order) {
        return paymentRepository.countByOrder(order) + 1;
    }

    @Transactional
    protected void updateOrderPaymentStatus(Order order, BigDecimal paymentAmount) {
        // Pour les paiements encaissés
        BigDecimal newRemainingAmount = order.getMontantRestant().subtract(paymentAmount)
                .setScale(2, RoundingMode.HALF_UP);

        // S'assurer que le montant restant ne devient pas négatif
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOperationException("Le montant du paiement dépasse le montant restant à payer");
        }

        order.setMontantRestant(newRemainingAmount);

        // Mettre à jour le statut de la commande si le montant restant est zéro
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            order.setStatut(OrderStatus.CONFIRMED);
            clientService.updateClientLoyalty(order.getClient().getId(), order.getTotalTTC());
        }

        orderRepository.save(order);
    }

    private PaymentStatus getInitialStatusForType(TypePaiement typePaiement) {
        return switch (typePaiement) {
            case ESPECES -> PaymentStatus.ENCAISSE;
            case CHEQUE, VIREMENT -> PaymentStatus.EN_ATTENTE;
            default -> throw new InvalidOperationException("Type de paiement non supporté: " + typePaiement);
        };
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(String paymentId, PaymentStatus newStatus, LocalDate encaissementDate) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        validateStatusTransition(payment.getStatus(), newStatus, payment.getTypePaiement());

        payment.setStatus(newStatus);

        // Mettre à jour la date d'encaissement si nécessaire
        if (newStatus == PaymentStatus.ENCAISSE) {
            payment.setDateEncaissement(encaissementDate != null ?
                    encaissementDate : LocalDate.now());
        }

        Payment updatedPayment = paymentRepository.save(payment);

        // Mettre à jour le statut de la commande si le paiement est maintenant encaissé
        if (newStatus == PaymentStatus.ENCAISSE) {
            updateOrderPaymentStatus(payment.getOrder(), payment.getMontant());
        }

        return paymentMapper.toResponse(updatedPayment);
    }

    private void validateStatusTransition(PaymentStatus currentStatus, PaymentStatus newStatus, TypePaiement typePaiement) {
        if (typePaiement == TypePaiement.ESPECES && newStatus != PaymentStatus.ENCAISSE) {
            throw new InvalidOperationException("Les paiements en espèces doivent toujours être à l'état ENCAISSE");
        }

        // Validation des transitions autorisées
        if (currentStatus == PaymentStatus.ENCAISSE) {
            throw new InvalidOperationException("Un paiement déjà encaissé ne peut pas être modifié");
        }

        if (newStatus == PaymentStatus.ENCAISSE && currentStatus == PaymentStatus.REJETE) {
            throw new InvalidOperationException("Un paiement rejeté ne peut pas être encaissé");
        }
    }

    private void validatePaymentType(PaymentRequest request) {
        if (request.getTypePaiement() == null) {
            throw new InvalidOperationException("Le type de paiement est obligatoire");
        }

        switch (request.getTypePaiement()) {
            case CHEQUE:
                if (request.getMontant().compareTo(new BigDecimal("20000")) > 0) {
                    throw new InvalidOperationException("Le montant maximum pour un chèque est de 20 000 DH");
                }
                if (request.getNumeroCheque() == null || request.getBank() == null) {
                    throw new InvalidOperationException("Le numéro de chèque et la banque sont obligatoires pour un paiement par chèque");
                }
                break;

            case VIREMENT:
                if (request.getReference() == null || request.getBank() == null) {
                    throw new InvalidOperationException("La référence et la banque sont obligatoires pour un virement");
                }
                break;

            case ESPECES:
                if (request.getMontant().compareTo(new BigDecimal("20000")) > 0) {
                    throw new InvalidOperationException("Le montant maximum pour un paiement en espèces est de 20 000 DH");
                }
                if (request.getRecu() == null) {
                    throw new InvalidOperationException("Un reçu est obligatoire pour un paiement en espèces");
                }
                break;
        }
    }

    @Override
    public boolean isOrderFullyPaid(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        return order.getMontantRestant().compareTo(BigDecimal.ZERO) <= 0;
    }

    public void validateStatusUpdateRequest(UpdatePaymentStatusRequest request) {
        if (request.getNewStatus() == null) {
            throw new InvalidOperationException("Le nouveau statut est obligatoire");
        }

        if (request.getNewStatus() == PaymentStatus.ENCAISSE &&
                request.getEncaissementDate() == null) {
            throw new InvalidOperationException("La date d'encaissement est obligatoire");
        }
    }
}
