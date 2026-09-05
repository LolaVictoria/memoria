package com.lolavictoria.celebration.resolver;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.Template;
import com.lolavictoria.celebration.repository.TemplateRepository;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TemplateResolver {

    private final TemplateRepository templateRepository;

    public TemplateResolver(
            TemplateRepository templateRepository
    ) {
        this.templateRepository = templateRepository;
    }

    @QueryMapping
    public List<Template> templates(
            @Argument Occasion occasion
    ) {

        if (occasion == null) {
            return templateRepository.findAll();
        }

        return templateRepository
                .findByOccasionAndActiveTrue(occasion);
    }
}