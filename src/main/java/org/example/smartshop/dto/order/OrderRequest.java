package org.example.smartshop.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "L'ID du client est requis")
    private String clientId;

    @NotEmpty(message = "La commande doit contenir au moins un article")
    private List<OrderRequest> items;

    // Validation Regex stricte exigée par le brief
    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Format code promo invalide (ex: PROMO-1234)")
    private String codePromo;
}
