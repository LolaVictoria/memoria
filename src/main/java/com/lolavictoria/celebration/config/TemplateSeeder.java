package com.lolavictoria.celebration.config;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.Template;
import com.lolavictoria.celebration.repository.TemplateRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemplateSeeder {

    @Bean
    CommandLineRunner seedTemplates(
            TemplateRepository templateRepository
    ) {
        return args -> {

            if (templateRepository.count() > 0) {
                return;
            }

            Template birthdayTemplate = new Template();

            birthdayTemplate.setName("Birthday Celebration");
            birthdayTemplate.setPreviewImageUrl(
                    "https://example.com/birthday-template.jpg"
            );
            birthdayTemplate.setOccasion(Occasion.BIRTHDAY);
            birthdayTemplate.setActive(true);

            templateRepository.save(birthdayTemplate);


            Template weddingTemplate = new Template();

            weddingTemplate.setName("Elegant Wedding");
            weddingTemplate.setPreviewImageUrl(
                    "https://example.com/wedding-template.jpg"
            );
            weddingTemplate.setOccasion(Occasion.WEDDING);
            weddingTemplate.setActive(true);

            templateRepository.save(weddingTemplate);


            Template graduationTemplate = new Template();

            graduationTemplate.setName("Graduation Celebration");
            graduationTemplate.setPreviewImageUrl(
                    "https://example.com/graduation-template.jpg"
            );
            graduationTemplate.setOccasion(Occasion.GRADUATION);
            graduationTemplate.setActive(true);

            templateRepository.save(graduationTemplate);
        };
    }
}