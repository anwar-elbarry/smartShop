package org.example.smartshop.service.order;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.order.CostBreakdownDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    @Transactional
    @Override
    public OrderResponse create(OrderRequest request) {
        Order order = orderMapper.toEntity(request);
        CostBreakdownDTO costBreakdown= calculBreakDown(request);
        order.setMontantRemise(costBreakdown.getMontantRemise());
        order.setMontantRestant(costBreakdown.getMontantRestant());
        order.setSousTotalHt(costBreakdown.getSousTotalHt());
        order.setTva(costBreakdown.getTva());
        order.setStatut(costBreakdown.getStatus());
        order.setDate(LocalDateTime.now());
        order.setTotalTTC(costBreakdown.getTotalTTC());
        Order saved = orderRepository.save(order);

        if (saved.getStatut() != OrderStatus.REJECTED) {
            createOrderItems(request.getOrderItems(), saved);
        }
        return orderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponse> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getById(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Order",orderId)
        );
        return orderMapper.toResponse(order);
    }

    @Transactional
    public CostBreakdownDTO calculBreakDown(OrderRequest request) {
        // First, validate stock availability
        List<OrderItemRequest> orderItems = request.getOrderItems();
        for (OrderItemRequest item : orderItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", item.getProductId()));
            if (product.getStock() < item.getQuantity()) {
                return CostBreakdownDTO.builder()
                        .status(OrderStatus.REJECTED)
                        .sousTotalHt(BigDecimal.ZERO)
                        .montantRemise(BigDecimal.ZERO)
                        .tva(BigDecimal.ZERO)
                        .totalTTC(BigDecimal.ZERO)
                        .montantRestant(BigDecimal.ZERO)
                        .build();
            }
        }

        // Calculate subtotal
        BigDecimal sousTotalHt = orderItems.stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product", item.getProductId()));
                    return product.getPrixUnitaire().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate discount
        BigDecimal montantRemise = calculerMontantRemise(request, sousTotalHt);
        BigDecimal montantHtApresRemise = sousTotalHt.subtract(montantRemise);
        BigDecimal tva = montantHtApresRemise.multiply(BigDecimal.valueOf(0.2));
        BigDecimal totalTTC = montantHtApresRemise.add(tva);

        return CostBreakdownDTO.builder()
                .sousTotalHt(sousTotalHt)
                .montantRemise(montantRemise)
                .tva(tva)
                .totalTTC(totalTTC)
                .status(OrderStatus.PENDING)
                .montantRestant(totalTTC)
                .build();
    }


    public BigDecimal calculerMontantRemise(OrderRequest request, BigDecimal sousTotalHt) {
        Client client = clientRepository.findById(request.getClientId()).orElseThrow(
                () -> new ResourceNotFoundException("Client", request.getClientId()));

        CustomerTier tier = client.getCustomerTier();
        BigDecimal montantHtRemise = BigDecimal.ZERO;

            if (tier.equals(CustomerTier.SILVER)  && sousTotalHt.compareTo(BigDecimal.valueOf(500)) >= 0) {
                montantHtRemise = sousTotalHt.multiply(BigDecimal.valueOf(0.05));
          }
           else if (tier.equals(CustomerTier.GOLD)  && sousTotalHt.compareTo(BigDecimal.valueOf(800)) >= 0) {
                montantHtRemise = sousTotalHt.multiply(BigDecimal.valueOf(0.10));
            }
              else if (tier.equals(CustomerTier.PLATINUM)  && sousTotalHt.compareTo(BigDecimal.valueOf(1200)) >= 0) {
                montantHtRemise = sousTotalHt.multiply(BigDecimal.valueOf(0.15));
            }

              return montantHtRemise;
    }


    @Transactional
    public void createOrderItems(List<OrderItemRequest> requestList, Order order) {
        for (OrderItemRequest request : requestList) {
            OrderItem orderItem = orderItemMapper.toEntity(request);
            if (orderItem == null) {
                throw new IllegalStateException("Failed to map OrderItemRequest to OrderItem");
            }
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", request.getProductId()));

            // Update stock
            int newStock = product.getStock() - request.getQuantity();
            product.setStock(newStock);
            productRepository.save(product);

            // Set order item details
            orderItem.setOrder(order);
            orderItem.setProduit(product);
            orderItem.setPrixUnitaire(product.getPrixUnitaire());
            orderItem.setTotalLigne(product.getPrixUnitaire().multiply(BigDecimal.valueOf(request.getQuantity())));
            orderItemRepository.save(orderItem);
        }
    }

    @Override
    public void cancele(String orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Order",orderId)
        );
        order.setStatut(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Override
    public Page<OrderResponse> findByClientId(String clientId,Pageable pageable) {
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new  ResourceNotFoundException("Client",clientId)
        );
       return orderRepository.findByClientId(client.getId(),pageable).map(orderMapper::toResponse);
    }

    @Override
    public Page<OrderResponse> findByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatut(status,pageable).map(orderMapper::toResponse);
    }
}

