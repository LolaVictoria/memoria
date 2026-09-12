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

import java.util.List;
import java.util.UUID;
import com.lolavictoria.celebration.entity.WebsiteDuration;
import com.lolavictoria.celebration.entity.WebsiteVersion;
import com.lolavictoria.celebration.repository.WebsiteVersionRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WebsiteService {

    private final WebsiteRepository websiteRepository;
    private final WebsiteTemplateRepository templateRepository;
    private final WebsiteVersionRepository websiteVersionRepository;
    
    public WebsiteService(
        WebsiteRepository websiteRepository,
        WebsiteTemplateRepository templateRepository,
        WebsiteVersionRepository websiteVersionRepository
) {
    this.websiteRepository = websiteRepository;
    this.templateRepository = templateRepository;
    this.websiteVersionRepository = websiteVersionRepository;
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
        website.setDuration(request.getDuration());
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

    public Website getWebsiteById(UUID id, User creator) {
        return getWebsiteForCreator(id, creator);
     }

     public List<Website> getWebsitesForCreator(User creator) {
        return websiteRepository.findByCreatorId(creator.getId());
     }

    // =========================
    // PUBLISH
    // =========================

    @Transactional
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

        if (website.getContent() == null) {
                throw new RuntimeException(
                        "Website has no content to publish"
                );
        }

        WebsiteVersion previousVersion =
                websiteVersionRepository
                        .findTopByWebsiteIdOrderByVersionNumberDesc(
                                websiteId
                        )
                        .orElse(null);

        int nextVersionNumber =
                previousVersion == null
                        ? 1
                        : previousVersion.getVersionNumber() + 1;

        WebsiteVersion newVersion = new WebsiteVersion();

        newVersion.setWebsite(website);
        newVersion.setVersionNumber(nextVersionNumber);

        // Freeze the current draft into this published snapshot
        newVersion.setContent(website.getContent());

        newVersion.setTemplate(website.getTemplate());

        newVersion =
                websiteVersionRepository.save(newVersion);

        // This is now the version visitors should see
        website.setPublishedVersion(newVersion);

        website.setStatus(Status.PUBLISHED);

        /*
        * Only calculate the expiry when publishing
        * for the first time.
        *
        * Editing and saving changes later does NOT
        * extend the website's lifetime.
        */
        if (website.getPublishedAt() == null) {

                LocalDateTime publishedAt =
                        LocalDateTime.now();

                website.setPublishedAt(publishedAt);

                website.setExpiresAt(
                        calculateExpiry(
                                publishedAt,
                                website.getDuration()
                        )
                );
        }

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
                .findByPublicSlugAndStatusAndExpiresAtAfter(
                        slug,
                        Status.PUBLISHED,
                        LocalDateTime.now()
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
    // HELPER FUNCTIONS
    // =========================


    private LocalDateTime calculateExpiry(
        LocalDateTime publishedAt,
        WebsiteDuration duration
        ) {

        return switch (duration) {

                case HOURS_24 ->
                        publishedAt.plusHours(24);

                case HOURS_72 ->
                        publishedAt.plusHours(72);

                case DAYS_7 ->
                        publishedAt.plusDays(7);

                case DAYS_365 ->
                        publishedAt.plusDays(365);
        };
        }

    private String generateSlug() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);
    }
}