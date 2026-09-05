package com.lolavictoria.celebration.service;

import com.cloudinary.Cloudinary;
import com.lolavictoria.celebration.dto.CloudinaryUploadSignatureResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    private static final String COVER_FOLDER = "celebration/covers";

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public CloudinaryUploadSignatureResponse generateUploadSignature() {

        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", COVER_FOLDER);

        String signature = cloudinary.apiSignRequest(
                paramsToSign,
                cloudinary.config.apiSecret
        );

        return new CloudinaryUploadSignatureResponse(
                signature,
                timestamp,
                apiKey,
                cloudName,
                COVER_FOLDER
        );
    }
}