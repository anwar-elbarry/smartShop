package org.example.smartshop.mapper;

import org.example.smartshop.dto.order.OrderRequest;
import org.example.smartshop.dto.order.OrderResponse;
import org.example.smartshop.entity.Order;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR,uses = OrderItemMapper.class)
public interface OrderMapper {

        @BeanMapping(ignoreByDefault = true)
        @Mapping(target = "client.id", source = "clientId")
        Order toEntity(OrderRequest orderRequest);

        @Mapping(target = "items", source = "orderItems")
        @Mapping(target = "clientName", source = "client.name")
        @Mapping(target = "clientId", source = "client.id")
        OrderResponse toResponse(Order entity);
    }