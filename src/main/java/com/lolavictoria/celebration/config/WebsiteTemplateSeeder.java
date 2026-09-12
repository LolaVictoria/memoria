package com.lolavictoria.celebration.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    public WebsiteTemplateSeeder(
            WebsiteTemplateRepository templateRepository
    ) {
        this.templateRepository = templateRepository;
    }

    @Override
    public void run(String... args) {

        WebsiteTemplate template;

        /*
         * If the template already exists, update it.
         * This is important because the template may already
         * exist in the database from an earlier seed.
         */
        List<WebsiteTemplate> existingTemplates =
                templateRepository.findAll();

        if (!existingTemplates.isEmpty()) {
            template = existingTemplates.get(0);

            updateSections(template);

            templateRepository.save(template);

            System.out.println(
                    "✅ Updated website template: "
                            + template.getName()
            );

            return;
        }

        /*
         * Create the template for the first time.
         */
        template = new WebsiteTemplate();

        template.setName("Birthday Memory Lane");
        template.setStyle(WebsiteTemplateStyle.PHOTO);

        template.setPreviewImageUrl(
                "https://placehold.co/1200x800/png?text=Birthday+Memory+Lane"
        );

        template.setOccasion(Occasion.BIRTHDAY);
        template.setActive(true);

        updateSections(template);

        templateRepository.save(template);

        System.out.println(
                "✅ Seeded website template: Birthday Memory Lane"
        );
    }

    private void updateSections(WebsiteTemplate template) {

        List<WebsiteSection> sections = new ArrayList<>();

        /*
         * HERO
         */
        sections.add(createSection(
                "Hero",
                "hero",
                1,
                createSchema(
                        "Start the celebration with a warm welcome.",
                        Map.of(
                                "heading",
                                field(
                                        "text",
                                        "Main heading",
                                        "Happy Birthday, Sarah!",
                                        "The main greeting visitors will see at the top of the website.",
                                        true
                                ),

                                "subtitle",
                                field(
                                        "textarea",
                                        "Intro message",
                                        "A few words to welcome them to this celebration...",
                                        "Add a short message beneath the main heading.",
                                        false
                                ),

                                "image",
                                field(
                                        "image",
                                        "Main photo",
                                        null,
                                        "Choose a photo that represents the person or celebration.",
                                        false
                                ),

                                "buttonText",
                                field(
                                        "text",
                                        "Button text",
                                        "Let's celebrate!",
                                        "Optional text displayed on the button.",
                                        false
                                )
                        )
                ),
                template
        ));

        /*
         * VIDEO
         */
        sections.add(createSection(
                "Video",
                "video",
                2,
                createSchema(
                        "Add a special video message or memory.",
                        Map.of(
                                "heading",
                                field(
                                        "text",
                                        "Section heading",
                                        "A message just for you",
                                        "Introduce the video to your visitor.",
                                        false
                                ),

                                "description",
                                field(
                                        "textarea",
                                        "Video description",
                                        "There's something special I wanted you to see...",
                                        "Add a short message explaining the video.",
                                        false
                                ),

                                "video",
                                field(
                                        "video",
                                        "Celebration video",
                                        null,
                                        "Upload a video you'd like to share with the recipient.",
                                        true
                                )
                        )
                ),
                template
        ));

        /*
         * GALLERY
         */
        sections.add(createSection(
                "Gallery",
                "gallery",
                3,
                createSchema(
                        "Bring the celebration to life with photos.",
                        Map.of(
                                "heading",
                                field(
                                        "text",
                                        "Section heading",
                                        "Our favorite memories",
                                        "Give your photo collection a meaningful title.",
                                        false
                                ),

                                "description",
                                field(
                                        "textarea",
                                        "Gallery introduction",
                                        "A few moments worth remembering...",
                                        "Add a short message above the photos.",
                                        false
                                ),

                                "photos",
                                field(
                                        "gallery",
                                        "Photos",
                                        null,
                                        "Upload the photos you want to include in this memory gallery.",
                                        true
                                )
                        )
                ),
                template
        ));

        /*
         * APPRECIATION
         */
        sections.add(createSection(
                "Appreciation",
                "appreciation",
                4,
                createSchema(
                        "Share the qualities, memories or little things that make this person special.",
                        Map.of(
                                "heading",
                                field(
                                        "text",
                                        "Section heading",
                                        "Things worth celebrating",
                                        "Introduce the things you appreciate about this person.",
                                        false
                                ),

                                "description",
                                field(
                                        "textarea",
                                        "Introduction",
                                        "Here are a few things that make you truly special...",
                                        "Add a short introduction before the appreciation cards.",
                                        false
                                ),

                                "items",
                                field(
                                        "cards",
                                        "Appreciation cards",
                                        null,
                                        "Create cards for the qualities, memories or reasons you appreciate them.",
                                        true
                                )
                        )
                ),
                template
        ));

        /*
         * LETTER
         */
        sections.add(createSection(
                "Letter",
                "letter",
                5,
                createSchema(
                        "Write a personal message from the heart.",
                        Map.of(
                                "heading",
                                field(
                                        "text",
                                        "Letter heading",
                                        "A little something from me",
                                        "Give your personal message a title.",
                                        false
                                ),

                                "body",
                                field(
                                        "richtext",
                                        "Your message",
                                        "Write your message here...",
                                        "Take your time and write something personal and meaningful.",
                                        true
                                )
                        )
                ),
                template
        ));

        /*
         * CLOSING
         */
        sections.add(createSection(
                "Closing",
                "closing",
                6,
                createSchema(
                        "End the celebration with one final message.",
                        Map.of(
                                "heading",
                                field(
                                        "text",
                                        "Closing heading",
                                        "Here's to many more beautiful moments!",
                                        "The final heading visitors will see.",
                                        true
                                ),

                                "message",
                                field(
                                        "textarea",
                                        "Closing message",
                                        "Wishing you more joy, laughter and beautiful memories...",
                                        "Add a final message to close the celebration.",
                                        false
                                ),

                                "image",
                                field(
                                        "image",
                                        "Closing photo",
                                        null,
                                        "Optional: add one final photo to end the celebration.",
                                        false
                                )
                        )
                ),
                template
        ));

        template.setWebsiteSections(sections);
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

        section.setWebsiteTemplate(template);

        return section;
    }

    private Map<String, Object> createSchema(
            String description,
            Map<String, Map<String, Object>> fields
    ) {
        Map<String, Object> schema = new LinkedHashMap<>();

        schema.put("description", description);
        schema.put("fields", fields);

        return schema;
    }

    private Map<String, Object> field(
            String type,
            String label,
            String placeholder,
            String description,
            boolean required
    ) {
        Map<String, Object> field = new LinkedHashMap<>();

        field.put("type", type);
        field.put("label", label);

        if (placeholder != null) {
            field.put("placeholder", placeholder);
        }

        field.put("description", description);
        field.put("required", required);

        return field;
    }
}