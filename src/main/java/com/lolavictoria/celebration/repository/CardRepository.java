package com.lolavictoria.celebration.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolavictoria.celebration.entity.Card;
import com.lolavictoria.celebration.entity.User;

public interface CardRepository extends JpaRepository<Card, UUID> {

    List<Card> findByCreator(User creator);

    Optional<Card> findByPublicSlug(String publicSlug);
    boolean existsByPublicSlug(String publicSlug);
}