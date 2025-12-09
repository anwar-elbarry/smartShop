package org.example.smartshop.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.smartshop.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends IsDeleted{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    private BigDecimal sousTotalHt;
    private BigDecimal montantRestant;
    private BigDecimal tva = BigDecimal.valueOf(0.2);
    private BigDecimal totalTTC;
    private BigDecimal montantRemise;

    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Invalid promo code format")
    private String codePromo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus statut = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Payment> payments;

}
