package com.lolavictoria.celebration.service;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.WebsiteTemplate;
import com.lolavictoria.celebration.repository.WebsiteTemplateRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebsiteTemplateService {

    private final WebsiteTemplateRepository templateRepository;

    public WebsiteTemplateService(WebsiteTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public List<WebsiteTemplate> getActiveTemplates() {
        return templateRepository.findByActiveTrue();
    }

    public List<WebsiteTemplate> getTemplatesByOccasion(Occasion occasion) {
        return templateRepository.findByOccasionAndActiveTrue(occasion);
    }

    public WebsiteTemplate getTemplateById(UUID id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Website template not found"));
    }
}