package org.example.smartshop.repository;

import org.example.smartshop.entity.Order;
import org.example.smartshop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByClientId(String clientId, Pageable pageable);
    Page<Order> findByStatut(OrderStatus statut, Pageable pageable);
}

