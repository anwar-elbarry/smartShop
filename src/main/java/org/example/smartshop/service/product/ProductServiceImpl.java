package org.example.smartshop.service.product;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.product.ProductRequest;
import org.example.smartshop.dto.product.ProductResponse;
import org.example.smartshop.dto.product.ProductUpdate;
import org.example.smartshop.entity.Product;
import org.example.smartshop.exception.ResourceNotFoundException;
import org.example.smartshop.mapper.ProductMapper;
import org.example.smartshop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        if (existByname(request.getNom())){
            throw new IllegalArgumentException("A product with the name " + request.getNom() + " already exists");
        }
        Product product = productMapper.toEntity(request);
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    public ProductResponse update(String productId, ProductUpdate request) {
        Product product = checkIfProductExist(productId);
        product.setStock(request.getStock());
        product.setPrixUnitaire(request.getPrixUnitaire());
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    public Optional<ProductResponse> getById(String productId) {
        Product product = checkIfProductExist(productId);
        return Optional.of(productMapper.toResponse(product));
    }

    @Override
    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepository.findProductByDeleted(false,pageable).map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getDeleted(Pageable pageable) {
        return productRepository.findProductByDeleted(true,pageable).map(productMapper::toResponse);
    }

    @Override
    public void delete(String productId) {
       Product product = checkIfProductExist(productId);
       product.delete();
       productRepository.save(product);
    }

    public Product checkIfProductExist(String productId){
        return productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product",productId));
    }
    public boolean existByname(String name){
        return productRepository.existsProductByNom(name);
    }
}
