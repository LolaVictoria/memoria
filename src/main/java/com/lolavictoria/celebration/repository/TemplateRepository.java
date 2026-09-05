package com.lolavictoria.celebration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.Template;

public interface TemplateRepository extends JpaRepository<Template, UUID> {

    List<Template> findByOccasionAndActiveTrue(Occasion occasion);
}