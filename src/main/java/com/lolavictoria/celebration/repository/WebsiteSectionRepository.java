package com.lolavictoria.celebration.repository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lolavictoria.celebration.entity.WebsiteSection;

public interface WebsiteSectionRepository
        extends JpaRepository<WebsiteSection, UUID> {

    List<WebsiteSection> findByWebsiteTemplateIdOrderByDisplayOrderAsc(UUID templateId);
}