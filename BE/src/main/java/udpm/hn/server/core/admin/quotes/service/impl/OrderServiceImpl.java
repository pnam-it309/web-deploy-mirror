package udpm.hn.server.core.admin.quotes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.quotes.model.request.OrderCreateRequest;
import udpm.hn.server.core.admin.quotes.model.request.OrderItemRequest;
import udpm.hn.server.core.admin.quotes.model.request.OrderStatusUpdateRequest;
import udpm.hn.server.core.admin.quotes.model.request.OrderUpdateRequest;
import udpm.hn.server.core.admin.quotes.model.response.OrderItemResponse;
import udpm.hn.server.core.admin.quotes.model.response.OrderResponse;
import udpm.hn.server.core.admin.quotes.model.response.OrderSummaryResponse;
import udpm.hn.server.core.admin.quotes.repository.OrderManageRepository;
import udpm.hn.server.core.admin.quotes.service.OrderService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Product;
import udpm.hn.server.entity.QuoteItem;
import udpm.hn.server.entity.QuoteRequest;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.core.constant.EntityUnit;
import udpm.hn.server.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderManageRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseObject<?> createOrder(OrderCreateRequest request) {
        try {
            log.info("Creating new order for customer: {}", request.getCustomerName());

            QuoteRequest order = new QuoteRequest();
            order.setCustomerName(request.getCustomerName());
            order.setCustomerEmail(request.getCustomerEmail());
            order.setCustomerPhone(request.getCustomerPhone());
            order.setCustomerCompany(request.getCustomerCompany());
            order.setNotes(request.getNotes());
            order.setStatus(EntityStatus.ACTIVE);

            // Process items
            List<QuoteItem> items = new ArrayList<>();
            BigDecimal totalEstimated = BigDecimal.ZERO;

            for (OrderItemRequest itemRequest : request.getItems()) {
                QuoteItem item = createQuoteItem(itemRequest, order);
                items.add(item);
                
                BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totalEstimated = totalEstimated.add(itemTotal);
            }

            order.setItems(items);
            order.setTotalEstimated(totalEstimated);

            QuoteRequest savedOrder = orderRepository.save(order);
            log.info("Order created successfully with ID: {}", savedOrder.getId());

            OrderResponse response = mapToOrderResponse(savedOrder);
            return ResponseObject.successForward(response, "Tạo đơn hàng thành công");
        } catch (Exception e) {
            log.error("Error creating order", e);
            return ResponseObject.errorForward("Lỗi khi tạo đơn hàng: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<?> updateOrder(String id, OrderUpdateRequest request) {
        try {
        log.info("Updating order with ID: {}", id);

        QuoteRequest order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerCompany(request.getCustomerCompany());
        order.setNotes(request.getNotes());

        // Clear existing items
        order.getItems().clear();

        // Add new items
        BigDecimal totalEstimated = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            QuoteItem item = createQuoteItem(itemRequest, order);
            order.getItems().add(item);
            
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalEstimated = totalEstimated.add(itemTotal);
        }

        order.setTotalEstimated(totalEstimated);

        QuoteRequest updatedOrder = orderRepository.save(order);
        log.info("Order updated successfully with ID: {}", updatedOrder.getId());

            OrderResponse response = mapToOrderResponse(updatedOrder);
            return ResponseObject.successForward(response, "Cập nhật đơn hàng thành công");
        } catch (Exception e) {
            log.error("Error updating order", e);
            return ResponseObject.errorForward("Lỗi khi cập nhật đơn hàng: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseObject<?> getOrderById(String id) {
        try {
            log.info("Fetching order with ID: {}", id);

            QuoteRequest order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

            OrderResponse response = mapToOrderResponse(order);
            return ResponseObject.successForward(response, "Lấy thông tin đơn hàng thành công");
        } catch (Exception e) {
            log.error("Error fetching order", e);
            return ResponseObject.errorForward("Lỗi khi lấy thông tin đơn hàng: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseObject<?> getAllOrders(String search, EntityStatus status, int page, int size) {
        try {
            log.info("Fetching orders with search: {}, status: {}", search, status);

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdDate"));
            Page<QuoteRequest> orders = orderRepository.findAllWithFilters(search, status, pageable);
            Page<OrderSummaryResponse> response = orders.map(this::mapToOrderSummaryResponse);

            return ResponseObject.successForward(response, "Lấy danh sách đơn hàng thành công");
        } catch (Exception e) {
            log.error("Error fetching orders", e);
            return ResponseObject.errorForward("Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<?> updateOrderStatus(String id, OrderStatusUpdateRequest request) {
        try {
            log.info("Updating order status for ID: {} to {}", id, request.getStatus());

            QuoteRequest order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

            order.setStatus(request.getStatus());

            QuoteRequest updatedOrder = orderRepository.save(order);
            log.info("Order status updated successfully");

            OrderResponse response = mapToOrderResponse(updatedOrder);
            return ResponseObject.successForward(response, "Cập nhật trạng thái đơn hàng thành công");
        } catch (Exception e) {
            log.error("Error updating order status", e);
            return ResponseObject.errorForward("Lỗi khi cập nhật trạng thái: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<?> deleteOrder(String id) {
        try {
            log.info("Deleting order with ID: {}", id);

            QuoteRequest order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

            orderRepository.delete(order);
            log.info("Order deleted successfully");

            return ResponseObject.successForward(null, "Xóa đơn hàng thành công");
        } catch (Exception e) {
            log.error("Error deleting order", e);
            return ResponseObject.errorForward("Lỗi khi xóa đơn hàng: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private QuoteItem createQuoteItem(OrderItemRequest request, QuoteRequest order) {
        QuoteItem item = new QuoteItem();
        item.setQuoteRequest(order);
        item.setProductNameSnapshot(request.getProductNameSnapshot());
        item.setUnitPrice(request.getUnitPrice());
        item.setQuantity(request.getQuantity());
        item.setNotes(request.getNotes());

        // Set unit
        if (request.getUnitSnapshot() != null && !request.getUnitSnapshot().isEmpty()) {
            try {
                item.setUnitSnapshot(EntityUnit.valueOf(request.getUnitSnapshot()));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid unit: {}, using default", request.getUnitSnapshot());
                item.setUnitSnapshot(EntityUnit.Cai);
            }
        }

        // Link product if provided
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId().toString())
                    .orElse(null);
            item.setProduct(product);
        }

        return item;
    }

    private OrderResponse mapToOrderResponse(QuoteRequest order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(generateOrderNumber(order))
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .customerPhone(order.getCustomerPhone())
                .customerCompany(order.getCustomerCompany())
                .notes(order.getNotes())
                .status(order.getStatus())
                .statusText(getStatusText(order.getStatus()))
                .totalEstimated(order.getTotalEstimated())
                .items(itemResponses)
                .createdAt(order.getCreatedDate())
                .updatedAt(order.getLastModifiedDate())
                .build();
    }

    private OrderItemResponse mapToOrderItemResponse(QuoteItem item) {
        BigDecimal total = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .productNameSnapshot(item.getProductNameSnapshot())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .unitSnapshot(item.getUnitSnapshot() != null ? item.getUnitSnapshot().name() : null)
                .notes(item.getNotes())
                .total(total)
                .createdAt(item.getCreatedDate())
                .updatedAt(item.getLastModifiedDate())
                .build();
    }

    private OrderSummaryResponse mapToOrderSummaryResponse(QuoteRequest order) {
        return OrderSummaryResponse.builder()
                .id(order.getId())
                .orderNumber(generateOrderNumber(order))
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .customerPhone(order.getCustomerPhone())
                .status(order.getStatus())
                .statusText(getStatusText(order.getStatus()))
                .totalEstimated(order.getTotalEstimated())
                .itemCount(order.getItems() != null ? order.getItems().size() : 0)
                .createdAt(order.getCreatedDate())
                .updatedAt(order.getLastModifiedDate())
                .build();
    }

    private String generateOrderNumber(QuoteRequest order) {
        if (order.getCreatedDate() != null) {
            return String.format("ORD-%d-%s", 
                order.getCreatedDate() / 1000,
                order.getId().substring(0, 8).toUpperCase());
        }
        return "ORD-" + order.getId().substring(0, 8).toUpperCase();
    }

    private String getStatusText(EntityStatus status) {
        return switch (status) {
            case ACTIVE -> "Đang hoạt động";
            case INACTIVE -> "Đã đóng";
        };
    }
}
