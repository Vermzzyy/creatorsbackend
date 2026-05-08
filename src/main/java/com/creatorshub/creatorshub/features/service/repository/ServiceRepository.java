package com.creatorshub.creatorshub.features.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.creatorshub.creatorshub.features.service.model.ServiceEntity;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findByCategoryIgnoreCase(String category);

    List<ServiceEntity> findByTitleContainingIgnoreCase(String keyword);

    @Query("SELECT s FROM ServiceEntity s WHERE " +
           "(:category IS NULL OR LOWER(s.category) = LOWER(:category)) AND " +
           "(:keyword IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR :keyword IS NULL OR LOWER(s.category) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<ServiceEntity> searchServices(@Param("category") String category,
                                       @Param("keyword") String keyword);
}
