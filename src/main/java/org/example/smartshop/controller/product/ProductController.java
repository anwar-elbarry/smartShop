package org.example.smartshop.controller.product;

import org.example.smartshop.dto.client.ClientUpdate;
import org.example.smartshop.dto.product.ProductRequest;
import org.example.smartshop.dto.product.ProductUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductController {
    ResponseEntity<?> create(ProductRequest request);
    ResponseEntity<?> update(ProductUpdate request, String productId);
    ResponseEntity<?> delete(String productId);
    ResponseEntity<Page<?>> getAll(Pageable pageable);
    ResponseEntity<?> getById(String productId);
}
