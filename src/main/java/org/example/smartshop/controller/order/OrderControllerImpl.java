package org.example.smartshop.controller.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.order.OrderRequest;
import org.example.smartshop.dto.order.OrderResponse;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.service.order.OrderService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Order API's managment")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;


    @Operation(summary = "Create Order")
    @ApiResponses({
        @ApiResponse(responseCode = "201" ,description = "order created successfully"),
        @ApiResponse(responseCode = "400" ,description = "Validation error")
    })
    @PostMapping
    @Override
    public ResponseEntity<?> create(@RequestBody @Validated OrderRequest request) {
        OrderResponse response = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "order created successfully",
                        "order", response
                )
        );
    }


    @Operation(summary = "Get order by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200" ,description = "order fetched successfully"),
            @ApiResponse(responseCode = "404" ,description = "order not found")
    })
    @GetMapping("{orderId}")
    @Override
    public ResponseEntity<?> getById(@PathVariable String orderId) {
        OrderResponse response = orderService.getById(orderId);
        return ResponseEntity.ok(
                Map.of(
                        "message" , "order feched successfully",
                        "Order", response
                )
        );
    }

    @Operation(summary = "Get All Orders")
            @ApiResponse(responseCode = "201" ,description = "orders fetched successfully")
    @GetMapping
    @Override
    public ResponseEntity<Page<OrderResponse>> getAll(@ParameterObject Pageable pageable) {
        Page<OrderResponse> response = orderService.getAll(pageable);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get orders by client ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved orders for the specified client")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/clinet/{clientId}")
    @Override
    public ResponseEntity<Page<OrderResponse>> getByClientId(@PathVariable String clientId,@ParameterObject Pageable pageable){
        return ResponseEntity.ok(orderService.findByClientId(clientId,pageable));
    }

    @Operation(summary = "Get orders by status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved orders By the specified Status")
    @GetMapping("/status")
    @Override
    public ResponseEntity<Page<OrderResponse>> findByStatus(@RequestBody @Validated OrderStatus status, @ParameterObject Pageable pageable){
        return ResponseEntity.ok(orderService.findByStatus(status,pageable));
    }
}
