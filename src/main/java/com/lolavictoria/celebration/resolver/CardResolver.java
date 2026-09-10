package com.lolavictoria.celebration.resolver;

import com.lolavictoria.celebration.entity.Card;
import com.lolavictoria.celebration.dto.CreateCardRequest;
import com.lolavictoria.celebration.dto.UpdateCardRequest;
import com.lolavictoria.celebration.service.CardService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class CardResolver {

    private final CardService cardService;

    public CardResolver(CardService cardService) {
        this.cardService = cardService;
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Card createCard(
            @Argument CreateCardRequest input) {

        return cardService.createCard(input);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<Card> cards() {

        return cardService.getMyCards();
    }

    // Public — recipient does not need an account
    @QueryMapping
    public Card cardBySlug(@Argument String slug) {

        return cardService.getCardBySlug(slug);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Card updateCard(
            @Argument UUID id,
            @Argument UpdateCardRequest input) {

        return cardService.updateCard(id, input);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Card publishCard(@Argument UUID id) {

        return cardService.publishCard(id);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Card archiveCard(@Argument UUID id) {

        return cardService.archiveCard(id);
    }
}