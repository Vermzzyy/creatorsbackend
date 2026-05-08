package com.creatorshub.creatorshub.features.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creatorshub.creatorshub.features.service.dto.CreateServiceRequest;
import com.creatorshub.creatorshub.features.service.dto.ServiceResponse;
import com.creatorshub.creatorshub.features.service.dto.UpdateServiceRequest;
import com.creatorshub.creatorshub.features.service.model.ServiceEntity;
import com.creatorshub.creatorshub.features.service.repository.ServiceRepository;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(ServiceResponse::new)
                .collect(Collectors.toList());
    }

    public ServiceResponse getServiceById(Long id) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return new ServiceResponse(entity);
    }

    public List<ServiceResponse> searchServices(String keyword, String category) {
        String catFilter = (category == null || category.isEmpty() || category.equalsIgnoreCase("All"))
                ? null : category;
        String keyFilter = (keyword == null || keyword.isEmpty()) ? null : keyword;

        if (catFilter == null && keyFilter == null) {
            return getAllServices();
        }

        return serviceRepository.searchServices(catFilter, keyFilter)
                .stream()
                .map(ServiceResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceResponse createService(CreateServiceRequest request) {

        ServiceEntity entity = new ServiceEntity();
        entity.setTitle(request.title);
        entity.setCategory(request.category);
        entity.setPrice(request.price);
        entity.setDescription(request.description);
        entity.setThumbnail(request.thumbnail);

        if (request.tags != null && !request.tags.isEmpty()) {
            entity.setTags(String.join(",", request.tags));
        }

        serviceRepository.save(entity);

        return new ServiceResponse(entity);
    }

    @Transactional
    public ServiceResponse updateService(Long id, UpdateServiceRequest request) {

        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (request.title != null) entity.setTitle(request.title);
        if (request.category != null) entity.setCategory(request.category);
        if (request.price != null) entity.setPrice(request.price);
        if (request.description != null) entity.setDescription(request.description);
        if (request.thumbnail != null) entity.setThumbnail(request.thumbnail);
        if (request.tags != null) {
            entity.setTags(String.join(",", request.tags));
        }

        serviceRepository.save(entity);

        return new ServiceResponse(entity);
    }

    @Transactional
    public void deleteService(Long id) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        serviceRepository.delete(entity);
    }
}
