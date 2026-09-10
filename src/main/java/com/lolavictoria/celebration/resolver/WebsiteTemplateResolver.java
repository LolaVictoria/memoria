package com.lolavictoria.celebration.resolver;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.WebsiteTemplate;
import com.lolavictoria.celebration.service.WebsiteTemplateService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.UUID;

@Controller
public class WebsiteTemplateResolver {

    private final WebsiteTemplateService templateService;

    public WebsiteTemplateResolver(WebsiteTemplateService templateService) {
        this.templateService = templateService;
    }

    @QueryMapping
    public List<WebsiteTemplate> websiteTemplates(
            @Argument Occasion occasion
    ) {
        if (occasion != null) {
            return templateService.getTemplatesByOccasion(occasion);
        }

        return templateService.getActiveTemplates();
    }

    @QueryMapping
    public WebsiteTemplate websiteTemplateById(
            @Argument UUID id
    ) {
        return templateService.getTemplateById(id);
    }
}