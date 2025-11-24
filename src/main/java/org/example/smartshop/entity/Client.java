package org.example.smartshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.smartshop.enums.CustomerTier;

import java.util.List;

@Data
@Entity
@Table(name = "clients")
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User{

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format de l'email invalide")
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerTier customerTier = CustomerTier.BASIC;

    private Integer totalOrders = 0;
    private Double totalSpent = 0.0;

    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY)
    private List<Order> commandHistory;
}
