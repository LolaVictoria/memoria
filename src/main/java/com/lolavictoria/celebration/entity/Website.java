package com.lolavictoria.celebration.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "websites")
public class Website {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String recipientName;

    private String recipientEmail;

    private String recipientPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Occasion occasion;

    // Only populated when occasion = CUSTOM
    private String customOccasion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // Person creating the website
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    // Website design selected by creator
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private WebsiteTemplate template;

    /*
     * Current draft content.
     *
     * For a new website:
     * - This contains what the creator is currently editing.
     *
     * For a published website:
     * - This can contain newer edits that are NOT live yet.
     *
     * The public website does NOT use this directly.
     * It uses the latest WebsiteVersion.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> content;

    // Public/shareable URL identifier
    @Column(nullable = false, unique = true, length = 50)
    private String publicSlug;

    /*
     * How long the website is available after publication.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WebsiteDuration duration;

    /*
     * When the website was first published.
     */
    private LocalDateTime publishedAt;

    /*
     * When the website stops being publicly accessible.
     */
    private LocalDateTime expiresAt;

    /*
     * The currently live published version.
     *
     * The draft can continue changing without affecting this version.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_version_id")
    private WebsiteVersion publishedVersion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Website() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public WebsiteTemplate getTemplate() {
        return template;
    }

    public void setTemplate(WebsiteTemplate template) {
        this.template = template;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public String getPublicSlug() {
        return publicSlug;
    }

    public void setPublicSlug(String publicSlug) {
        this.publicSlug = publicSlug;
    }

    public WebsiteDuration getDuration() {
        return duration;
    }

    public void setDuration(WebsiteDuration duration) {
        this.duration = duration;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public WebsiteVersion getPublishedVersion() {
        return publishedVersion;
    }

    public void setPublishedVersion(WebsiteVersion publishedVersion) {
        this.publishedVersion = publishedVersion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}