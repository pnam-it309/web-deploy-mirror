package udpm.hn.server.core.admin.order.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.order.dto.request.OrderFilterRequest;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.admin.order.repository.OrderManageRepository;
import udpm.hn.server.core.admin.order.repository.OrderSpecification;
import udpm.hn.server.core.admin.order.service.OrderADService;
import udpm.hn.server.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderADServiceImpl implements OrderADService {

    private final OrderManageRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(OrderFilterRequest request, Pageable pageable) {
        Specification<Order> spec = OrderSpecification.getFilter(request);
        return orderRepository.findAll(spec, pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
        return convertToResponse(order);
    }

    // Logic quan trọng nhất: Phê duyệt đơn
    @Override
    @Transactional
    public void updateOrderStatus(String id, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));

        // Tại đây có thể thêm logic kiểm tra (VD: Không thể huỷ đơn đã giao thành công)
        // if (order.getOrderStatus() == Order.OrderStatus.DELIVERED) {
        //     throw new IllegalStateException("Cannot change status of delivered order");
        // }

        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = modelMapper.map(order, OrderResponse.class);

        // Map chi tiết sản phẩm
        List<OrderResponse.OrderItemResponse> itemResponses = order.getItems().stream().map(item -> {
            OrderResponse.OrderItemResponse itemResp = new OrderResponse.OrderItemResponse();
            // Kiểm tra null để tránh lỗi nếu sản phẩm bị xoá
            if (item.getProduct() != null) {
                itemResp.setProductId(item.getProduct().getId());
                itemResp.setProductName(item.getProduct().getName());
                itemResp.setProductSku(item.getProduct().getSku());
            }
            itemResp.setQuantity(item.getQuantity());
            itemResp.setUnitPrice(item.getUnitPrice());
            itemResp.setTotalPrice(item.getTotalPrice());
            return itemResp;
        }).collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }
}