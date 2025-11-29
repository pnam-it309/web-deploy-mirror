package udpm.hn.server.core.customer.register.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.customer.register.dto.request.OrderItemRequest;
import udpm.hn.server.core.customer.register.dto.request.OrderRequest;
import udpm.hn.server.core.customer.register.dto.response.OrderItemResponse;
import udpm.hn.server.core.customer.register.service.OrderService;
import udpm.hn.server.entity.Customer;
import udpm.hn.server.entity.Order;
import udpm.hn.server.entity.OrderItem;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.repository.CustomerRepository;
import udpm.hn.server.repository.OrderItemRepository;
import udpm.hn.server.repository.OrderRepository;
import udpm.hn.server.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCusServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // 1. Validate customer exists
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

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
        Order savedOrder = orderRepository.save(order);

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