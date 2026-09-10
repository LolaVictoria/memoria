package com.lolavictoria.celebration.service;

import com.lolavictoria.celebration.dto.CreateWebsiteRequest;
import com.lolavictoria.celebration.entity.Status;
import com.lolavictoria.celebration.entity.User;
import com.lolavictoria.celebration.entity.Website;
import com.lolavictoria.celebration.entity.WebsiteTemplate;
import com.lolavictoria.celebration.repository.WebsiteRepository;
import com.lolavictoria.celebration.repository.WebsiteTemplateRepository;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebsiteService {

    private final WebsiteRepository websiteRepository;
    private final WebsiteTemplateRepository templateRepository;

    public WebsiteService(
            WebsiteRepository websiteRepository,
            WebsiteTemplateRepository templateRepository
    ) {
        this.websiteRepository = websiteRepository;
        this.templateRepository = templateRepository;
    }

    public Website createWebsite(
            CreateWebsiteRequest request,
            User creator
    ) {

        WebsiteTemplate template = templateRepository
                .findById(request.getTemplateId())
                .orElseThrow(() ->
                        new RuntimeException("Website template not found")
                );

        Website website = new Website();

        website.setTitle(request.getTitle());
        website.setRecipientName(request.getRecipientName());
        website.setRecipientEmail(request.getRecipientEmail());
        website.setRecipientPhone(request.getRecipientPhone());

        website.setOccasion(request.getOccasion());
        website.setCustomOccasion(request.getCustomOccasion());

        website.setTemplate(template);
        website.setCreator(creator);

        website.setContent(request.getContent());

        website.setStatus(Status.DRAFT);

        website.setPublicSlug(generateSlug());

        return websiteRepository.save(website);
    }

    private String generateSlug() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);
    }
}