package org.example.smartshop.controller.payment;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.payment.PaymentRequest;
import org.example.smartshop.dto.payment.PaymentResponse;
import org.example.smartshop.service.payment.PaymentService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Payments", description = "Payment management APIs")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentControlller{

    private final PaymentService paymentService;

    @Operation(summary = "Process a payment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment request"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping
    @Override
    public ResponseEntity<?> processPayment(@Validated @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Payment processed successfully",
                        "payment", response
                )
        );
    }

    @Operation(summary = "Get all payments with optional client filter")
    @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    @GetMapping
    @Override
    public ResponseEntity<Page<PaymentResponse>> getAllPayments(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String clientId) {
        return ResponseEntity.ok(paymentService.getAll(pageable, clientId));
    }

    @Operation(summary = "Get all payments for an order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/order/{orderId}")
    @Override
    public ResponseEntity<?> getOrderPayments(@PathVariable String orderId) {
        PaymentResponse response = paymentService.getOrderPayments(orderId);
        return ResponseEntity.ok(
                Map.of(
                        "message", "Order payments retrieved successfully",
                        "data", response
                )
        );
    }

    @Operation(summary = "Check if an order is fully paid")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment status retrieved"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/order/{orderId}/status")
    @Override
    public ResponseEntity<Boolean> isOrderFullyPaid(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.isOrderFullyPaid(orderId));
    }
}
