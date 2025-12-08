package org.example.smartshop.mapper;

import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.dto.payment.PaymentResponse;

import org.example.smartshop.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMapper {
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "datePaiement", ignore = true)
    @Mapping(target = "order.id", source = "orderId")
    Payment toEntity(PaymentRequest paymentRequest);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "status", source = "status")
    PaymentResponse toResponse(Payment entity);
}
