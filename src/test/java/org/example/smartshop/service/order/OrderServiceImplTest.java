package org.example.smartshop.service.order;

import org.example.smartshop.dto.order.OrderRequest;
import org.example.smartshop.dto.order.OrderResponse;
import org.example.smartshop.dto.orderItem.OrderItemRequest;
import org.example.smartshop.entity.Client;
import org.example.smartshop.entity.Order;
import org.example.smartshop.entity.OrderItem;
import org.example.smartshop.entity.Product;
import org.example.smartshop.enums.CustomerTier;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.exception.ResourceNotFoundException;
import org.example.smartshop.mapper.OrderItemMapper;
import org.example.smartshop.mapper.OrderMapper;
import org.example.smartshop.repository.ClientRepository;
import org.example.smartshop.repository.OrderItemRepository;
import org.example.smartshop.repository.OrderRepository;
import org.example.smartshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequest orderRequest;
    private Order order;
    private Product product;
    private Client client;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId("client1");
        client.setCustomerTier(CustomerTier.GOLD);

        product = new Product();
        product.setId("product1");
        product.setPrixUnitaire(BigDecimal.valueOf(100));
        product.setStock(10);

        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId("product1");
        orderItemRequest.setQuantity(2);

        orderRequest = new OrderRequest();
        orderRequest.setClientId("client1");
        orderRequest.setOrderItems(Collections.singletonList(orderItemRequest));

        order = new Order();
        order.setId("order1");
        order.setClient(client);
        order.setDate(LocalDateTime.now());

        orderResponse = OrderResponse.builder()
                .id("order1")
                .build();

    }

    @Test
    void create_success() {
        when(orderMapper.toEntity(any(OrderRequest.class))).thenReturn(order);
        when(clientRepository.findById(anyString())).thenReturn(Optional.of(client));
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemMapper.toEntity(any(OrderItemRequest.class))).thenReturn(new OrderItem());
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);

        OrderResponse result = orderService.create(orderRequest);

        assertNotNull(result);
        assertEquals("order1", result.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void create_insufficientStock() {
        product.setStock(1);
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId("product1");
        orderItemRequest.setQuantity(2);
        orderRequest.setOrderItems(Collections.singletonList(orderItemRequest));

        when(orderMapper.toEntity(any(OrderRequest.class))).thenReturn(order);
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);


        orderService.create(orderRequest);

        assertEquals(OrderStatus.REJECTED, order.getStatut());
        verify(orderRepository, times(1)).save(order);
        verify(orderItemRepository, never()).save(any(OrderItem.class));
    }

    @Test
    void getAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orders = Collections.singletonList(order);
        Page<Order> orderPage = new PageImpl<>(orders, pageable, orders.size());

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);

        Page<OrderResponse> result = orderService.getAll(pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findAll(pageable);
    }

    @Test
    void getById_found() {
        when(orderRepository.findById("order1")).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.getById("order1");

        assertNotNull(result);
        assertEquals("order1", result.getId());
        verify(orderRepository, times(1)).findById("order1");
    }

    @Test
    void getById_notFound() {
        when(orderRepository.findById("order1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getById("order1"));
        verify(orderRepository, times(1)).findById("order1");
    }

    @Test
    void cancele() {
        when(orderRepository.findById("order1")).thenReturn(Optional.of(order));

        orderService.cancele("order1");

        assertEquals(OrderStatus.CANCELED, order.getStatut());
        verify(orderRepository, times(1)).save(order);
    }
}

