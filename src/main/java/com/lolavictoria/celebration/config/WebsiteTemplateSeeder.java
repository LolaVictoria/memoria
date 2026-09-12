package com.lolavictoria.celebration.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lolavictoria.celebration.entity.Occasion;
import com.lolavictoria.celebration.entity.WebsiteSection;
import com.lolavictoria.celebration.entity.WebsiteTemplate;
import com.lolavictoria.celebration.entity.WebsiteTemplateStyle;
import com.lolavictoria.celebration.repository.WebsiteTemplateRepository;

@Component
public class WebsiteTemplateSeeder implements CommandLineRunner {

    private final WebsiteTemplateRepository templateRepository;

    public WebsiteTemplateSeeder(WebsiteTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public void run(String... args) {

        // Don't create duplicates every time the application starts.
        if (templateRepository.count() > 0) {
            return;
        }

        WebsiteTemplate template = new WebsiteTemplate();

        template.setName("Birthday Memory Lane");
        template.setStyle(WebsiteTemplateStyle.PHOTO);

        // Temporary placeholder.
        // We'll replace this with a real preview image later.
        template.setPreviewImageUrl(
                "https://placehold.co/1200x800/png?text=Birthday+Memory+Lane"
        );

        template.setOccasion(Occasion.BIRTHDAY);
        template.setActive(true);

        List<WebsiteSection> sections = new ArrayList<>();

        sections.add(createSection(
                "Hero",
                "hero",
                1,
                createSchema(
                        "Hero section",
                        List.of(
                                field("heading", "text", true),
                                field("subtitle", "textarea", false),
                                field("image", "image", false),
                                field("buttonText", "text", false)
                        )
                ),
                template
        ));

        sections.add(createSection(
                "Video",
                "video",
                2,
                createSchema(
                        "Video introduction",
                        List.of(
                                field("heading", "text", false),
                                field("description", "textarea", false),
                                field("video", "video", true)
                        )
                ),
                template
        ));

        sections.add(createSection(
                "Gallery",
                "gallery",
                3,
                createSchema(
                        "Photo memories",
                        List.of(
                                field("heading", "text", false),
                                field("description", "textarea", false),
                                field("photos", "gallery", true)
                        )
                ),
                template
        ));

        sections.add(createSection(
                "Appreciation",
                "appreciation",
                4,
                createSchema(
                        "Things worth celebrating",
                        List.of(
                                field("heading", "text", false),
                                field("description", "textarea", false),
                                field("items", "cards", true)
                        )
                ),
                template
        ));

        sections.add(createSection(
                "Letter",
                "letter",
                5,
                createSchema(
                        "Personal letter",
                        List.of(
                                field("heading", "text", false),
                                field("body", "richtext", true)
                        )
                ),
                template
        ));

        sections.add(createSection(
                "Closing",
                "closing",
                6,
                createSchema(
                        "Closing message",
                        List.of(
                                field("heading", "text", true),
                                field("message", "textarea", false),
                                field("image", "image", false)
                        )
                ),
                template
        ));

        template.setWebsiteSections(sections);

        templateRepository.save(template);

        System.out.println(
                "✅ Seeded website template: Birthday Memory Lane"
        );
    }

    private WebsiteSection createSection(
            String name,
            String sectionKey,
            int displayOrder,
            Map<String, Object> fieldSchema,
            WebsiteTemplate template
    ) {
        WebsiteSection section = new WebsiteSection();

        section.setName(name);
        section.setSectionKey(sectionKey);
        section.setDisplayOrder(displayOrder);
        section.setFieldSchema(fieldSchema);

        // Important because WebsiteSection owns the relationship.
        section.setWebsiteTemplate(template);

        return section;
    }

    private Map<String, Object> createSchema(
            String description,
            List<Map<String, Object>> fields
    ) {
        Map<String, Object> schema = new HashMap<>();

        schema.put("description", description);
        schema.put("fields", fields);

        return schema;
    }

    private Map<String, Object> field(
            String name,
            String type,
            boolean required
    ) {
        Map<String, Object> field = new HashMap<>();

        field.put("name", name);
        field.put("type", type);
        field.put("required", required);

        return field;
    }
}