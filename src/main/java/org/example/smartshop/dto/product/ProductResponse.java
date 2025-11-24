package org.example.smartshop.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ProductResponse {
    private String id;
    private String nom;
    private BigDecimal prixUnitaire;
    private Integer stock;
}
