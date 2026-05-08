package com.creatorshub.creatorshub.features.order.service;

import com.creatorshub.creatorshub.features.order.dto.CreateOrderRequest;
import com.creatorshub.creatorshub.features.order.dto.OrderResponse;
import com.creatorshub.creatorshub.features.order.model.OrderEntity;
import com.creatorshub.creatorshub.features.order.repository.OrderRepository;
import com.creatorshub.creatorshub.features.service.model.ServiceEntity;
import com.creatorshub.creatorshub.features.service.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        ServiceEntity service = serviceRepository.findById(request.serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        OrderEntity order = new OrderEntity();
        order.setUserId(userId);
        order.setServiceId(service.getId());
        order.setServiceTitle(service.getTitle());
        order.setPrice(service.getPrice());
        order.setInstructions(request.instructions);
        order.setStatus("PENDING");

        orderRepository.save(order);
        return new OrderResponse(order);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return new OrderResponse(order);
    }
}
