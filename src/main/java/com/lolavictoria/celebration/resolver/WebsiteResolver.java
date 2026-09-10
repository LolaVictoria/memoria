package com.lolavictoria.celebration.resolver;

import com.lolavictoria.celebration.dto.CreateWebsiteRequest;
import com.lolavictoria.celebration.entity.User;
import com.lolavictoria.celebration.entity.Website;
import com.lolavictoria.celebration.service.WebsiteService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class WebsiteResolver {

    private final WebsiteService websiteService;

    public WebsiteResolver(WebsiteService websiteService) {
        this.websiteService = websiteService;
    }

    @MutationMapping
    public Website createWebsite(
            @Argument CreateWebsiteRequest input,
            Authentication authentication
    ) {

        User creator = (User) authentication.getPrincipal();

        return websiteService.createWebsite(input, creator);
    }
}