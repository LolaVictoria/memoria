package com.lolavictoria.celebration.service;

import com.lolavictoria.celebration.dto.CreateCelebrationRequest;
import com.lolavictoria.celebration.dto.UpdateCelebrationRequest;
import com.lolavictoria.celebration.entity.Celebration;
import com.lolavictoria.celebration.entity.Status;
import com.lolavictoria.celebration.entity.Template;
import com.lolavictoria.celebration.entity.User;
import com.lolavictoria.celebration.exception.BadRequestException;
import com.lolavictoria.celebration.exception.ForbiddenException;
import com.lolavictoria.celebration.exception.ResourceNotFoundException;
import com.lolavictoria.celebration.exception.UnauthenticatedException;
import com.lolavictoria.celebration.repository.CelebrationRepository;
import com.lolavictoria.celebration.repository.TemplateRepository;
import com.lolavictoria.celebration.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
public class CelebrationService {

    private final CelebrationRepository celebrationRepository;
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;

    private static final String BASE62 =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final SecureRandom random = new SecureRandom();

    public CelebrationService(
            CelebrationRepository celebrationRepository,
            TemplateRepository templateRepository,
            UserRepository userRepository
    ) {
        this.celebrationRepository = celebrationRepository;
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
    }

    public Celebration createCelebration(
            CreateCelebrationRequest request
    ) {

        User creator = getCurrentUser();

        validateCustomOccasion(request);

        Template template = null;

        if (request.getTemplateId() != null) {
            template = templateRepository
                    .findById(request.getTemplateId())
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Template not found")
                    );
        }

        String publicSlug = generatePublicSlug();

        if (request.getCustomSlug() != null
                && !request.getCustomSlug().isBlank()) {

            publicSlug = request.getCustomSlug().trim().toLowerCase();

            if (celebrationRepository.existsByPublicSlug(publicSlug)) {
                throw new BadRequestException(
                        "That custom slug is already in use"
                );
            }
        }

        Celebration celebration = new Celebration();

        celebration.setTitle(request.getTitle());
        celebration.setRecipientName(request.getRecipientName());
        celebration.setRecipientEmail(request.getRecipientEmail());
        celebration.setRecipientPhone(request.getRecipientPhone());
        celebration.setOccasion(request.getOccasion());
        celebration.setCustomOccasion(request.getCustomOccasion());
        celebration.setMessage(request.getMessage());
        celebration.setCoverImageUrl(request.getCoverImageUrl());
        celebration.setStatus(
                request.isSaveAsDraft()
                        ? Status.DRAFT
                        : Status.PUBLISHED
                );
        celebration.setCreator(creator);
        celebration.setTemplate(template);
        celebration.setPublicSlug(publicSlug);

        return celebrationRepository.save(celebration);
    }

    public List<Celebration> getMyCelebrations() {

        User creator = getCurrentUser();

        return celebrationRepository.findByCreator(creator);
    }

    public Celebration getCelebrationBySlug(String slug) {

        return celebrationRepository
                .findByPublicSlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Celebration not found")
                );
    }

    public Celebration updateCelebration(
            UUID id,
            UpdateCelebrationRequest request
    ) {

        User currentUser = getCurrentUser();

        Celebration celebration = celebrationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Celebration not found")
                );

        if (!celebration.getCreator()
                .getId()
                .equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You are not allowed to update this celebration"
            );
        }


        if (request.getTitle() != null) {
            celebration.setTitle(request.getTitle());
        }

        if (request.getRecipientName() != null) {
            celebration.setRecipientName(
                    request.getRecipientName()
            );
        }

        if (request.getRecipientEmail() != null) {
            celebration.setRecipientEmail(
                    request.getRecipientEmail()
            );
        }

        if (request.getRecipientPhone() != null) {
            celebration.setRecipientPhone(
                    request.getRecipientPhone()
            );
        }

        if (request.getOccasion() != null) {
            celebration.setOccasion(
                    request.getOccasion()
            );
        }

        if (request.getCustomOccasion() != null) {
            celebration.setCustomOccasion(
                    request.getCustomOccasion()
            );
        }

        if (request.getMessage() != null) {
            celebration.setMessage(request.getMessage());
        }

        if (request.getCoverImageUrl() != null) {
            celebration.setCoverImageUrl(
                request.getCoverImageUrl()
            );
        }

        if (request.getTemplateId() != null) {

            Template template = templateRepository
                    .findById(request.getTemplateId())
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Template not found")
                    );

            celebration.setTemplate(template);
        }

        if (request.getCustomSlug() != null
                && !request.getCustomSlug().isBlank()) {

            String newSlug = request
                    .getCustomSlug()
                    .trim()
                    .toLowerCase();

            if (!newSlug.equals(
                    celebration.getPublicSlug()
            ) && celebrationRepository
                    .existsByPublicSlug(newSlug)) {

                throw new BadRequestException(
                        "That custom slug is already in use"
                );
            }

            celebration.setPublicSlug(newSlug);
        }

        return celebrationRepository.save(celebration);
    }

    public Celebration publishCelebration(UUID id) {

        Celebration celebration = getOwnedCelebration(id);

        celebration.setStatus(Status.PUBLISHED);

        return celebrationRepository.save(celebration);
    }

    public Celebration archiveCelebration(UUID id) {

        Celebration celebration = getOwnedCelebration(id);

        celebration.setStatus(Status.ARCHIVED);

        return celebrationRepository.save(celebration);
    }

    private Celebration getOwnedCelebration(UUID id) {

        User currentUser = getCurrentUser();

        Celebration celebration = celebrationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Celebration not found")
                );

        if (!celebration.getCreator()
                .getId()
                .equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You are not allowed to modify this celebration"
            );
        }

        return celebration;
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
            CreateCelebrationRequest request
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
                celebrationRepository.existsByPublicSlug(slug)
        );

        return slug;
    }
}