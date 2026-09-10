package com.lolavictoria.celebration.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "website_templates")
public class WebsiteTemplate {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WebsiteTemplateStyle style;

    @Column(nullable = false)
    private String previewImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Occasion occasion;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(
        mappedBy = "websiteTemplate",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<WebsiteSection> websiteSections = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public WebsiteTemplate() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WebsiteTemplateStyle getStyle() {
		return style;
	}

	public void setStyle(WebsiteTemplateStyle style) {
		this.style = style;
	}

	public String getPreviewImageUrl() {
		return previewImageUrl;
	}

	public void setPreviewImageUrl(String previewImageUrl) {
		this.previewImageUrl = previewImageUrl;
	}

	public Occasion getOccasion() {
		return occasion;
	}

	public void setOccasion(Occasion occasion) {
		this.occasion = occasion;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<WebsiteSection> getWebsiteSections() {
		return websiteSections;
	}

	public void setWebsiteSections(List<WebsiteSection> websiteSections) {
		this.websiteSections = websiteSections;
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

    // getters and setters
}