package org.example.smartshop.dto.orderItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull(message = "L'ID du produit est requis")
    private String productId;

    @NotNull(message = "La quantité est requise")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantity;
}
