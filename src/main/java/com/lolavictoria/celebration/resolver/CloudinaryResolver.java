package com.lolavictoria.celebration.resolver;

import com.lolavictoria.celebration.dto.CloudinaryUploadSignatureResponse;
import com.lolavictoria.celebration.service.CloudinaryService;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class CloudinaryResolver {

    private final CloudinaryService cloudinaryService;

    public CloudinaryResolver(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public CloudinaryUploadSignatureResponse generateCloudinaryUploadSignature() {
        return cloudinaryService.generateUploadSignature();
    }
}