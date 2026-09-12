package com.lolavictoria.celebration.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lolavictoria.celebration.entity.Status;
import com.lolavictoria.celebration.entity.Website;

public interface WebsiteRepository extends JpaRepository<Website, UUID> {
    List<Website> findByCreatorId(UUID creatorId);
    Optional<Website> findByPublicSlug(String publicSlug);

    Optional<Website> findByPublicSlugAndStatus(
            String publicSlug,
            Status status
    );

    Optional<Website> findByPublicSlugAndStatusAndExpiresAtAfter(
        String publicSlug,
        Status status,
        LocalDateTime now
    );
}