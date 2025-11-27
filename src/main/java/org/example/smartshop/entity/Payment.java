package org.example.smartshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.smartshop.enums.PaymentStatus;
import org.example.smartshop.enums.TypePaiement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "payments")
public class Payment extends IsDeleted{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Integer numeroPaiement;

    @Column(nullable = false)
    private BigDecimal montant;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypePaiement typePaiement;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.EN_ATTENTE;

    private LocalDateTime datePaiement;
    private LocalDateTime dateEncaissement;

    private String recu;
    private String numeroCheque;
    private String banque;
    private LocalDateTime chequeEcheance;
    private String reference;
}
