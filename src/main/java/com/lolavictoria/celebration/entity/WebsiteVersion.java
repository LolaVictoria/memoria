package com.lolavictoria.celebration.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

@Entity
@Table(
    name = "website_versions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_website_version_number",
            columnNames = {"website_id", "version_number"}
        )
    }
)
public class WebsiteVersion {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "website_id", nullable = false)
    private Website website;

    /*
     * Version number within this website:
     *
     * 1
     * 2
     * 3
     * ...
     */
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    /*
     * The content that was actually published.
     *
     * This is a snapshot and must never be modified after publication.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> content;

    /*
     * The template used by this published version.
     *
     * This allows a creator to change templates later while
     * keeping older published versions intact.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private WebsiteTemplate template;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public WebsiteVersion() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public WebsiteTemplate getTemplate() {
        return template;
    }

    public void setTemplate(WebsiteTemplate template) {
        this.template = template;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}