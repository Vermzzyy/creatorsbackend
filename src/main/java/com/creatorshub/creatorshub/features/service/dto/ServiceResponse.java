package com.creatorshub.creatorshub.features.service.dto;

import java.util.Arrays;
import java.util.List;

import com.creatorshub.creatorshub.features.service.model.ServiceEntity;

public class ServiceResponse {

    public Long id;
    public String title;
    public String category;
    public String price;
    public String description;
    public List<String> tags;
    public String thumbnail;

    public ServiceResponse(ServiceEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.category = entity.getCategory();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.thumbnail = entity.getThumbnail();

        // Convert comma-separated tags string into a List
        if (entity.getTags() != null && !entity.getTags().isEmpty()) {
            this.tags = Arrays.asList(entity.getTags().split(","));
        } else {
            this.tags = List.of();
        }
    }
}
