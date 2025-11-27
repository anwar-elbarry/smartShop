package org.example.smartshop.mapper;

import org.example.smartshop.dto.product.ProductRequest;
import org.example.smartshop.dto.product.ProductResponse;
import org.example.smartshop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product entity);
}
