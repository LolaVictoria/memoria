package com.lolavictoria.celebration.service;

import com.lolavictoria.celebration.dto.CreateWebsiteRequest;
import com.lolavictoria.celebration.dto.UpdateWebsiteRequest;
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

    // =========================
    // CREATE
    // =========================

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


    // =========================
    // UPDATE
    // =========================

    public Website updateWebsite(
            UUID websiteId,
            UpdateWebsiteRequest request,
            User creator
    ) {

        Website website = getWebsiteForCreator(
                websiteId,
                creator
        );

        if (website.getStatus() == Status.ARCHIVED) {
            throw new RuntimeException(
                    "Archived websites cannot be edited"
            );
        }

        if (request.getTitle() != null) {
            website.setTitle(request.getTitle());
        }

        if (request.getRecipientName() != null) {
            website.setRecipientName(
                    request.getRecipientName()
            );
        }

        if (request.getRecipientEmail() != null) {
            website.setRecipientEmail(
                    request.getRecipientEmail()
            );
        }

        if (request.getRecipientPhone() != null) {
            website.setRecipientPhone(
                    request.getRecipientPhone()
            );
        }

        if (request.getOccasion() != null) {
            website.setOccasion(
                    request.getOccasion()
            );
        }

        if (request.getCustomOccasion() != null) {
            website.setCustomOccasion(
                    request.getCustomOccasion()
            );
        }

        if (request.getTemplateId() != null) {

            WebsiteTemplate template =
                    templateRepository
                            .findById(request.getTemplateId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Website template not found"
                                    )
                            );

            website.setTemplate(template);
        }

        if (request.getContent() != null) {
            website.setContent(
                    request.getContent()
            );
        }

        return websiteRepository.save(website);
    }


    // =========================
    // PUBLISH
    // =========================

    public Website publishWebsite(
            UUID websiteId,
            User creator
    ) {

        Website website = getWebsiteForCreator(
                websiteId,
                creator
        );

        if (website.getStatus() == Status.ARCHIVED) {
            throw new RuntimeException(
                    "Archived websites cannot be published"
            );
        }

        website.setStatus(Status.PUBLISHED);

        return websiteRepository.save(website);
    }


    // =========================
    // ARCHIVE
    // =========================

    public Website archiveWebsite(
            UUID websiteId,
            User creator
    ) {

        Website website = getWebsiteForCreator(
                websiteId,
                creator
        );

        website.setStatus(Status.ARCHIVED);

        return websiteRepository.save(website);
    }


    // =========================
    // PUBLIC WEBSITE
    // =========================

    public Website getWebsiteBySlug(
            String slug
    ) {

        return websiteRepository
                .findByPublicSlugAndStatus(
                        slug,
                        Status.PUBLISHED
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Website not found"
                        )
                );
    }


    // =========================
    // CREATOR OWNERSHIP
    // =========================

    private Website getWebsiteForCreator(
            UUID websiteId,
            User creator
    ) {

        Website website = websiteRepository
                .findById(websiteId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Website not found"
                        )
                );

        if (
                website.getCreator() == null ||
                creator == null ||
                !website.getCreator()
                        .getId()
                        .equals(creator.getId())
        ) {

            throw new RuntimeException(
                    "You do not have permission to modify this website"
            );
        }

        return website;
    }


    // =========================
    // SLUG GENERATION
    // =========================

    private String generateSlug() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);
    }
}