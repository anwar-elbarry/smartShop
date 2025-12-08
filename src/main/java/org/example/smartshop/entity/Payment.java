package org.example.smartshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.smartshop.enums.PaymentStatus;
import org.example.smartshop.enums.TypePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    @JsonBackReference("order-payments")
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
    private PaymentStatus status;

    private LocalDate datePaiement;
    private LocalDate dateEncaissement;

    private String recu;
    private String numeroCheque;
    private String bank;
    private LocalDate chequeEcheance;
    private String reference;
}
