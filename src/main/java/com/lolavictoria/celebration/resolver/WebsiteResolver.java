package com.lolavictoria.celebration.resolver;

import com.lolavictoria.celebration.dto.CreateWebsiteRequest;
import com.lolavictoria.celebration.dto.UpdateWebsiteRequest;
import com.lolavictoria.celebration.entity.User;
import com.lolavictoria.celebration.entity.Website;
import com.lolavictoria.celebration.service.WebsiteService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class WebsiteResolver {

    private final WebsiteService websiteService;

    public WebsiteResolver(
            WebsiteService websiteService
    ) {
        this.websiteService = websiteService;
    }


    // =========================
    // CREATE WEBSITE
    // =========================

    @MutationMapping
    public Website createWebsite(
            @Argument CreateWebsiteRequest input,
            Authentication authentication
    ) {

        User creator =
                (User) authentication.getPrincipal();

        return websiteService.createWebsite(
                input,
                creator
        );
    }


    // =========================
    // UPDATE WEBSITE
    // =========================

    @MutationMapping
    public Website updateWebsite(
            @Argument UUID id,
            @Argument UpdateWebsiteRequest input,
            Authentication authentication
    ) {

        User creator =
                (User) authentication.getPrincipal();

        return websiteService.updateWebsite(
                id,
                input,
                creator
        );
    }

    @QueryMapping
public Website websiteById(
        @Argument UUID id,
        Authentication authentication
) {
    User creator = (User) authentication.getPrincipal();

    return websiteService.getWebsiteById(id, creator);
}
    // =========================
    // PUBLISH WEBSITE
    // =========================

    @MutationMapping
    public Website publishWebsite(
            @Argument UUID id,
            Authentication authentication
    ) {

        User creator =
                (User) authentication.getPrincipal();

        return websiteService.publishWebsite(
                id,
                creator
        );
    }


    // =========================
    // ARCHIVE WEBSITE
    // =========================

    @MutationMapping
    public Website archiveWebsite(
            @Argument UUID id,
            Authentication authentication
    ) {

        User creator =
                (User) authentication.getPrincipal();

        return websiteService.archiveWebsite(
                id,
                creator
        );
    }


    // =========================
    // PUBLIC WEBSITE
    // =========================

    @QueryMapping
    public Website websiteBySlug(
            @Argument String slug
    ) {

        return websiteService.getWebsiteBySlug(
                slug
        );
    }
}