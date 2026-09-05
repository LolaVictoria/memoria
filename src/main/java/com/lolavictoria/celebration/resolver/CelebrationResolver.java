package com.lolavictoria.celebration.resolver;

import com.lolavictoria.celebration.entity.Celebration;
import com.lolavictoria.celebration.dto.CreateCelebrationRequest;
import com.lolavictoria.celebration.dto.UpdateCelebrationRequest;
import com.lolavictoria.celebration.service.CelebrationService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class CelebrationResolver {

    private final CelebrationService celebrationService;

    public CelebrationResolver(CelebrationService celebrationService) {
        this.celebrationService = celebrationService;
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Celebration createCelebration(
            @Argument CreateCelebrationRequest input) {

        return celebrationService.createCelebration(input);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<Celebration> celebrations() {

        return celebrationService.getMyCelebrations();
    }

    // Public — recipient does not need an account
    @QueryMapping
    public Celebration celebrationBySlug(@Argument String slug) {

        return celebrationService.getCelebrationBySlug(slug);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Celebration updateCelebration(
            @Argument UUID id,
            @Argument UpdateCelebrationRequest input) {

        return celebrationService.updateCelebration(id, input);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Celebration publishCelebration(@Argument UUID id) {

        return celebrationService.publishCelebration(id);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Celebration archiveCelebration(@Argument UUID id) {

        return celebrationService.archiveCelebration(id);
    }
}