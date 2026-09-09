package com.lolavictoria.celebration.dto;

import java.util.UUID;

import com.lolavictoria.celebration.entity.Occasion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCelebrationRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String recipientName;

    private String recipientEmail;

    private String recipientPhone;

    private String message;

    private boolean saveAsDraft;

    private String coverImageUrl;

    @NotNull
    private Occasion occasion;

    private String customOccasion;

    private String customSlug;

    private UUID templateId;

    public CreateCelebrationRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public Occasion getOccasion() {
        return occasion;
    }

    public void setOccasion(Occasion occasion) {
        this.occasion = occasion;
    }

    public String getCustomOccasion() {
        return customOccasion;
    }

    public void setCustomOccasion(String customOccasion) {
        this.customOccasion = customOccasion;
    }

    public String getCustomSlug() {
        return customSlug;
    }

    public void setCustomSlug(String customSlug) {
        this.customSlug = customSlug;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public boolean isSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(boolean saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
    }

    // getters and setters
}