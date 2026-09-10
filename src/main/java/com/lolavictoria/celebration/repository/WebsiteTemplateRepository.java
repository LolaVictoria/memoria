package com.lolavictoria.celebration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.WebsiteTemplate;

public interface WebsiteTemplateRepository
        extends JpaRepository<WebsiteTemplate, UUID> {

    List<WebsiteTemplate> findByActiveTrue();

    List<WebsiteTemplate> findByOccasionAndActiveTrue(Occasion occasion);
}