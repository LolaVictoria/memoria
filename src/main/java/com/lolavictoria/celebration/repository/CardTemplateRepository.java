package com.lolavictoria.celebration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.CardTemplate;

public interface CardTemplateRepository extends JpaRepository<CardTemplate, UUID> {

    List<CardTemplate> findByOccasionAndActiveTrue(Occasion occasion);
}