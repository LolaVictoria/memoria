package com.lolavictoria.celebration.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolavictoria.celebration.entity.WebsiteVersion;

public interface WebsiteVersionRepository
        extends JpaRepository<WebsiteVersion, UUID> {

    Optional<WebsiteVersion> findTopByWebsiteIdOrderByVersionNumberDesc(
            UUID websiteId
    );
}