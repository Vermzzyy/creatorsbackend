package com.creatorshub.creatorshub.features.order.dto;

import com.creatorshub.creatorshub.features.order.model.OrderEntity;
import java.time.LocalDateTime;

public class OrderResponse {
    public Long id;
    public Long userId;
    public Long serviceId;
    public String serviceTitle;
    public String price;
    public String instructions;
    public String status;
    public LocalDateTime createdAt;

    public OrderResponse(OrderEntity entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.serviceId = entity.getServiceId();
        this.serviceTitle = entity.getServiceTitle();
        this.price = entity.getPrice();
        this.instructions = entity.getInstructions();
        this.status = entity.getStatus();
        this.createdAt = entity.getCreatedAt();
    }
}
