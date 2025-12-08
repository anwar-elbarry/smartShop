package org.example.smartshop.service.product;

import org.example.smartshop.entity.OrderItem;
import org.springframework.data.repository.Repository;

interface OrderItemRepository extends Repository<OrderItem, String> {
    boolean existsByProduitId(String produitId);
}
