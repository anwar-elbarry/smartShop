package org.example.smartshop.mapper;

import org.example.smartshop.dto.orderItem.OrderItemRequest;
import org.example.smartshop.dto.orderItem.OrderItemResponse;
import org.example.smartshop.entity.OrderItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderItemMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "produit.id", source = "productId")
    OrderItem toEntity(OrderItemRequest orderItemRequest);

    @Mapping(target = "productName" , source = "produit.nom")
    OrderItemResponse toResponse(OrderItem entity);
}

