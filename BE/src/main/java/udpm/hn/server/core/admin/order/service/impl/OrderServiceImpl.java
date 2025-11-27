package udpm.hn.server.core.admin.order.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.order.dto.request.OrderCreateRequest;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.admin.order.repository.OrderManageRepository;
import udpm.hn.server.core.admin.order.service.OrderService;
import udpm.hn.server.core.admin.product.repository.ProductManageRepository;
import udpm.hn.server.entity.Order;
import udpm.hn.server.entity.OrderItem;
import udpm.hn.server.entity.Product;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderManageRepository orderRepository;
    private final ProductManageRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findByStatus(EntityStatus.ACTIVE, pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
        return convertToResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        Order order = new Order();
        modelMapper.map(request, order);

        // 1. Tạo mã đơn hàng tự động (VD: ORD-123456)
        order.setOrderCode("ORD-" + System.currentTimeMillis() % 1000000);
        order.setStatus(EntityStatus.ACTIVE);
        order.setOrderStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);

        // 2. Xử lý danh sách sản phẩm & Tính tổng tiền
        BigDecimal grandTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderCreateRequest.OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemReq.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setUnitPrice(product.getPrice()); // Lấy giá hiện tại của SP

            BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            orderItem.setTotalPrice(total);

            grandTotal = grandTotal.add(total);

            // Link 2 chiều
            order.addItem(orderItem);
        }

        order.setTotalAmount(grandTotal);

        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    // (Bạn có thể thêm hàm updateStatus, cancelOrder sau này)

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = modelMapper.map(order, OrderResponse.class);
        // Map danh sách items thủ công để lấy tên SP
        List<OrderResponse.OrderItemResponse> itemResponses = order.getItems().stream().map(item -> {
            OrderResponse.OrderItemResponse itemResp = new OrderResponse.OrderItemResponse();
            itemResp.setProductId(item.getProduct().getId());
            itemResp.setProductName(item.getProduct().getName());
            itemResp.setProductSku(item.getProduct().getSku());
            itemResp.setQuantity(item.getQuantity());
            itemResp.setUnitPrice(item.getUnitPrice());
            itemResp.setTotalPrice(item.getTotalPrice());
            return itemResp;
        }).collect(Collectors.toList());
        response.setItems(itemResponses);
        return response;
    }
}