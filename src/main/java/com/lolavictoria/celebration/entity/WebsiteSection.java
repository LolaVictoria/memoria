package com.lolavictoria.celebration.entity;

import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "website_sections")
public class WebsiteSection {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sectionKey;

    @Column(nullable = false)
    private Integer displayOrder;

    @JdbcTypeCode (SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> fieldSchema;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private WebsiteTemplate websiteTemplate;

    public WebsiteSection() {
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

    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Map<String, Object> getFieldSchema() {
        return fieldSchema;
    }

    public void setFieldSchema(Map<String, Object> fieldSchema) {
        this.fieldSchema = fieldSchema;
    }

    public WebsiteTemplate getWebsiteTemplate() {
        return websiteTemplate;
    }

    public void setWebsiteTemplate(WebsiteTemplate websiteTemplate) {
        this.websiteTemplate = websiteTemplate;
    }
}