package org.example.smartshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem  extends IsDeleted{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produit_id")
    private Product produit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal prixUnitaire;

    @Column(nullable = false)
    private BigDecimal totalLigne;
}
