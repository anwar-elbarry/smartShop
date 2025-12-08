package org.example.smartshop.repository;

import org.example.smartshop.entity.Order;
import org.example.smartshop.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Page<Payment> findByOrderId(String orderId,Pageable pageable);

    int countByOrder(Order order);
}

