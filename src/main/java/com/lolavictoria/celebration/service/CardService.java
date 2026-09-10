package com.lolavictoria.celebration.service;

import com.lolavictoria.celebration.dto.CreateCardRequest;
import com.lolavictoria.celebration.dto.UpdateCardRequest;
import com.lolavictoria.celebration.entity.Card;
import com.lolavictoria.celebration.entity.Status;
import com.lolavictoria.celebration.entity.CardTemplate;
import com.lolavictoria.celebration.entity.User;
import com.lolavictoria.celebration.exception.BadRequestException;
import com.lolavictoria.celebration.exception.ForbiddenException;
import com.lolavictoria.celebration.exception.ResourceNotFoundException;
import com.lolavictoria.celebration.exception.UnauthenticatedException;
import com.lolavictoria.celebration.repository.CardRepository;
import com.lolavictoria.celebration.repository.CardTemplateRepository;
import com.lolavictoria.celebration.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardTemplateRepository cardTemplateRepository;
    private final UserRepository userRepository;

    private static final String BASE62 =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final SecureRandom random = new SecureRandom();

    public CardService(
            CardRepository cardRepository,
            CardTemplateRepository cardTemplateRepository,
            UserRepository userRepository
    ) {
        this.cardRepository = cardRepository;
        this.cardTemplateRepository = cardTemplateRepository;
        this.userRepository = userRepository;
    }

    public Card createCard(
            CreateCardRequest request
    ) {

        User creator = getCurrentUser();

        validateCustomOccasion(request);

        CardTemplate template = null;

        if (request.getTemplateId() != null) {
            template = cardTemplateRepository
                    .findById(request.getTemplateId())
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Template not found")
                    );
        }

        String publicSlug = generatePublicSlug();

        if (request.getCustomSlug() != null
                && !request.getCustomSlug().isBlank()) {

            publicSlug = request.getCustomSlug().trim().toLowerCase();

            if (cardRepository.existsByPublicSlug(publicSlug)) {
                throw new BadRequestException(
                        "That custom slug is already in use"
                );
            }
        }

        Card card = new Card();

        card.setTitle(request.getTitle());
        card.setRecipientName(request.getRecipientName());
        card.setRecipientEmail(request.getRecipientEmail());
        card.setRecipientPhone(request.getRecipientPhone());
        card.setOccasion(request.getOccasion());
        card.setCustomOccasion(request.getCustomOccasion());
        card.setMessage(request.getMessage());
        card.setCoverImageUrl(request.getCoverImageUrl());
        card.setStatus(
                request.isSaveAsDraft()
                        ? Status.DRAFT
                        : Status.PUBLISHED
                );
        card.setCreator(creator);
        card.setTemplate(template);
        card.setPublicSlug(publicSlug);

        return cardRepository.save(card);
    }

    public List<Card> getMyCards() {

        User creator = getCurrentUser();

        return cardRepository.findByCreator(creator);
    }

    public Card getCardBySlug(String slug) {

        return cardRepository
                .findByPublicSlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found")
                );
    }

    public Card updateCard(
            UUID id,
            UpdateCardRequest request
    ) {

        User currentUser = getCurrentUser();

        Card card = cardRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found")
                );

        if (!card.getCreator()
                .getId()
                .equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You are not allowed to update this card"
            );
        }


        if (request.getTitle() != null) {
            card.setTitle(request.getTitle());
        }

        if (request.getRecipientName() != null) {
            card.setRecipientName(
                    request.getRecipientName()
            );
        }

        if (request.getRecipientEmail() != null) {
            card.setRecipientEmail(
                    request.getRecipientEmail()
            );
        }

        if (request.getRecipientPhone() != null) {
            card.setRecipientPhone(
                    request.getRecipientPhone()
            );
        }

        if (request.getOccasion() != null) {
            card.setOccasion(
                    request.getOccasion()
            );
        }

        if (request.getCustomOccasion() != null) {
            card.setCustomOccasion(
                    request.getCustomOccasion()
            );
        }

        if (request.getMessage() != null) {
            card.setMessage(request.getMessage());
        }

        if (request.getCoverImageUrl() != null) {
            card.setCoverImageUrl(
                request.getCoverImageUrl()
            );
        }

        if (request.getTemplateId() != null) {

            CardTemplate template = cardTemplateRepository
                    .findById(request.getTemplateId())
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Template not found")
                    );

            card.setTemplate(template);
        }

        if (request.getCustomSlug() != null
                && !request.getCustomSlug().isBlank()) {

            String newSlug = request
                    .getCustomSlug()
                    .trim()
                    .toLowerCase();

            if (!newSlug.equals(
                    card.getPublicSlug()
            ) && cardRepository
                    .existsByPublicSlug(newSlug)) {

                throw new BadRequestException(
                        "That custom slug is already in use"
                );
            }

            card.setPublicSlug(newSlug);
        }

        return cardRepository.save(card);
    }

    public Card publishCard(UUID id) {

        Card card = getOwnedCard(id);

        card.setStatus(Status.PUBLISHED);

        return cardRepository.save(card);
    }

    public Card archiveCard(UUID id) {

        Card card = getOwnedCard(id);

        card.setStatus(Status.ARCHIVED);

        return cardRepository.save(card);
    }

    private Card getOwnedCard(UUID id) {

        User currentUser = getCurrentUser();

        Card card = cardRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card not found")
                );

        if (!card.getCreator()
                .getId()
                .equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You are not allowed to modify this card"
            );
        }

        return card;
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication.getName() == null) {

            throw new UnauthenticatedException(
                    "User is not authenticated"
            );
        }

        String email = authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    private void validateCustomOccasion(
            CreateCardRequest request
    ) {

        if (request.getOccasion().name().equals("CUSTOM")
                && (request.getCustomOccasion() == null
                || request.getCustomOccasion().isBlank())) {

            throw new BadRequestException(
                    "Custom occasion name is required"
            );
        }
    }

    private String generatePublicSlug() {

        String slug;

        do {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 12; i++) {
                builder.append(
                        BASE62.charAt(
                                random.nextInt(BASE62.length())
                        )
                );
            }

            slug = builder.toString();

        } while (
                cardRepository.existsByPublicSlug(slug)
        );

        return slug;
    }
}