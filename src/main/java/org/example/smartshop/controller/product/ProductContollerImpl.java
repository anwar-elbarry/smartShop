package org.example.smartshop.controller.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.product.ProductRequest;
import org.example.smartshop.dto.product.ProductResponse;
import org.example.smartshop.dto.product.ProductUpdate;
import org.example.smartshop.service.product.ProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Tag(name = "products" , description = "Product management APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductContollerImpl implements ProductController{

    private final ProductService productService;


    @Operation(summary = "Create product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product Created Successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    @Override
    public ResponseEntity<?> create(@Validated @RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message","Product Created Successfully",
                        "product" , productResponse
                )
        );
    }

    @Operation(summary = "Update product")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Product Updated Successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("{productId}")
    @Override
    public ResponseEntity<?> update(@Validated @RequestBody ProductUpdate request,@PathVariable String productId) {
        ProductResponse productResponse = productService.update(productId,request);
        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of(
                        "message","Product Updated Successfully",
                        "product" , productResponse
                )
        );
    }

    @Operation(summary = "delete product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted Successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @DeleteMapping("{productId}")
    @Override
    public ResponseEntity<?> delete(String productId) {
        productService.delete(productId);
        return ResponseEntity.ok(
                Map.of(
                        "message", "Product deleted Successfully"
                )
        );
    }

    @Operation(summary = "Get All products")
            @ApiResponse(responseCode = "200", description = "Products feched Successfully")
    @GetMapping
    @Override
    public ResponseEntity<Page<?>> getAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll(pageable));
    }


    @Operation(summary = "Get product By Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "product feched Successfully"),
            @ApiResponse(responseCode = "404", description = "product not found")
    })
    @GetMapping("{productId}")
    @Override
    public ResponseEntity<?> getById(@PathVariable String productId) {
        Optional<ProductResponse> productResponse = productService.getById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of(
                        "message","Product feched Successfully",
                        "product" , productResponse
                )
        );
    }
}
