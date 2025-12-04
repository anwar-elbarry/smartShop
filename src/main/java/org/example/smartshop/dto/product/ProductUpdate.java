package org.example.smartshop.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class ProductUpdate {
    @Positive(message = "Le prix doit être positif")
    private BigDecimal prixUnitaire;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stock;
}
