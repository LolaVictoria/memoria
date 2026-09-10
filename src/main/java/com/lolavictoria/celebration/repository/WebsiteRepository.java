package com.lolavictoria.celebration.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolavictoria.celebration.entity.Website;

public interface WebsiteRepository extends JpaRepository<Website, UUID> {

    Optional<Website> findByPublicSlug(String publicSlug);
}