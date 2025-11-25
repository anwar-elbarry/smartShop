package org.example.smartshop.mapper;

import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.dto.payment.PaymentResponse;
import org.example.smartshop.entity.Payment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "order.id", source = "orderId")
    Payment toEntity(PaymentRequest paymentRequest);

    PaymentResponse toResponse(Payment entity);
}

