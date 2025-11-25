package org.example.smartshop.repository;

import org.example.smartshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByIsDeletedFalse(Pageable pageable);
    Optional<Product> findByIdAndIsDeletedFalse(String id);
}

