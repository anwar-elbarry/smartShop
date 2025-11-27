package org.example.smartshop.mapper;

import org.example.smartshop.dto.client.ClientRequest;
import org.example.smartshop.dto.client.ClientResponse;
import org.example.smartshop.entity.Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username" ,source = "username")
    @Mapping(target = "email" ,source = "email")
    @Mapping(target = "name" ,source = "name")
    Client toEntity(ClientRequest dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "customerTier", source = "customerTier")
    @Mapping(target = "totalOrders", source = "totalOrders")
    @Mapping(target = "totalSpent", source = "totalSpent")
    ClientResponse toResponse(Client entity);
}
