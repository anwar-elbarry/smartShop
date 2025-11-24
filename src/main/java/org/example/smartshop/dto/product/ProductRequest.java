package org.example.smartshop.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank(message = "Le nom du produit est requis")
    private String nom;

    @NotNull(message = "Le prix est requis")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal prixUnitaire;

    @NotNull(message = "Le stock est requis")
    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stock;
}
