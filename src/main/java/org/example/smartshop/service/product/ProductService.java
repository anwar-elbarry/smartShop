package org.example.smartshop.service.product;

import org.example.smartshop.dto.product.ProductRequest;
import org.example.smartshop.dto.product.ProductResponse;
import org.example.smartshop.dto.product.ProductUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse update(String productId, ProductUpdate request);
    Optional<ProductResponse> getById(String productId);
    Page<ProductResponse> getAll(Pageable pageable);
    Page<ProductResponse> getDeleted(Pageable pageable);
    void delete(String productId);
}
