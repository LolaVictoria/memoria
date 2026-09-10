package com.lolavictoria.celebration.resolver;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.CardTemplate;
import com.lolavictoria.celebration.repository.CardTemplateRepository;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class CardTemplateResolver {

    private final CardTemplateRepository cardTemplateRepository;

    public CardTemplateResolver(
            CardTemplateRepository cardTemplateRepository
    ) {
        this.cardTemplateRepository = cardTemplateRepository;
    }
    @QueryMapping
    public CardTemplate cardTemplateById(
            @Argument UUID id
    ) {
        return cardTemplateRepository.findById(id).orElse(null);
    }
    @QueryMapping
    public List<CardTemplate> cardTemplates(
            @Argument Occasion occasion
    ) {

        if (occasion == null) {
            return cardTemplateRepository.findAll();
        }

        return cardTemplateRepository
                .findByOccasionAndActiveTrue(occasion);
    }
}