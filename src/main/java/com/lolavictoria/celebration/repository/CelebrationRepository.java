package com.lolavictoria.celebration.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolavictoria.celebration.entity.Celebration;
import com.lolavictoria.celebration.entity.User;

public interface CelebrationRepository extends JpaRepository<Celebration, UUID> {

    List<Celebration> findByCreator(User creator);

    Optional<Celebration> findByPublicSlug(String publicSlug);
    boolean existsByPublicSlug(String publicSlug);
}