package udpm.hn.server.core.customer.order.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.customer.order.model.response.CustomerOrderDetailResponse;
import udpm.hn.server.core.customer.order.model.response.CustomerOrderItemResponse;
import udpm.hn.server.core.customer.order.model.response.CustomerOrderResponse;
import udpm.hn.server.core.customer.order.repository.CustomerOrderRepository;
import udpm.hn.server.core.customer.order.service.CustomerOrderService;
import udpm.hn.server.core.customer.order.model.request.OrderItemRequest;
import udpm.hn.server.core.customer.order.model.request.OrderRequest;
import udpm.hn.server.entity.*;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.CustomerRepository;
import udpm.hn.server.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService {
    private final CustomerRepository customerRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final ProductRepository productRepository;

    @Override
    public PageableObject<CustomerOrderResponse> getOrders(String email, String search, String status, Pageable pageable) {
        Page<CustomerOrderResponse> page = customerOrderRepository.findAllByCustomerEmail(email, search, status, pageable);
        return new PageableObject<>(page);
    }

    @Override
    public CustomerOrderDetailResponse getOrderDetail(String id, String email) {
        Order order = customerOrderRepository.findByIdAndCustomerEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        List<CustomerOrderItemResponse> items = order.getItems().stream().map(item -> {
            String imageUrl = null;
            if (item.getProduct().getProductDetail() != null && !item.getProduct().getProductDetail().getImages().isEmpty()) {
                imageUrl = item.getProduct().getProductDetail().getImages().get(0).getUrl();
            }
            
            return new CustomerOrderItemResponse(
                item.getId(),
                item.getProduct().getName(),
                imageUrl,
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
            );
        }).collect(Collectors.toList());
        
        return new CustomerOrderDetailResponse(order, items);
    }

    @Override
    public Boolean cancelOrder(String id, String email, String reason) {
        Order order = customerOrderRepository.findByIdAndCustomerEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getOrderStatus() == Order.OrderStatus.PENDING || order.getOrderStatus() == Order.OrderStatus.CONFIRMED) {
            order.setOrderStatus(Order.OrderStatus.CANCELLED);
            order.setCancellationReason(reason);
            customerOrderRepository.save(order);
            return true;
        }
        
        throw new RuntimeException("Cannot cancel order in current status");
    }
    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // 1. Validate customer exists
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseGet(() -> {
                    // Fallback: Try to find by email if ID not found (e.g. Admin acting as Customer)
                    return customerRepository.findByEmail(orderRequest.getEmail())
                            .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + orderRequest.getCustomerId() + " or Email: " + orderRequest.getEmail()));
                });

        // 2. Create order
        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setCustomerName(customer.getName());
        order.setCustomerPhone(orderRequest.getPhoneNumber());
        order.setCustomerEmail(orderRequest.getEmail());
        order.setCustomerAddress(orderRequest.getShippingAddress());
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        order.setNotes(orderRequest.getNote());
        order.setStatus(EntityStatus.ACTIVE);

        // 3. Process order items
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemRequest.getProductId()));

            // Check stock
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            // Total price will be calculated by @PrePersist in OrderItem

            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            order.addItem(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = customerOrderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    private String generateOrderCode() {
        return "OD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderCode(order.getOrderCode());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setCustomerAddress(order.getCustomerAddress());
        response.setOrderStatus(order.getOrderStatus());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        if (order.getCreatedDate() != null) {
            response.setCreatedDate(LocalDateTime.ofEpochSecond(
                    order.getCreatedDate() / 1000, 0, java.time.ZoneOffset.UTC).toLocalDate());
        }

        List<OrderResponse.OrderItemResponse> itemResponses = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());
        response.setItems(itemResponses);

        return response;
    }

    private OrderResponse.OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        OrderResponse.OrderItemResponse response = new OrderResponse.OrderItemResponse();
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setTotalPrice(item.getTotalPrice());
        if (item.getProduct().getSku() != null) {
            response.setProductSku(item.getProduct().getSku());
        }
        return response;
    }
}
