package com.waarc.about;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waarc.about.pojo.AboutRequest;
import com.waarc.exception.ResourceNotFoundException;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class AboutServiceImpl implements AboutService {

    private final AboutRepository aboutRepository = new AboutRepositoryImpl();
    private final Logger LOG = LogManager.getLogger(AboutServiceImpl.class);

    @Override
    public String getAbout(Context ctx) {
        ctx.contentType("application/json");
        try {
            About about = aboutRepository.getAbout();
            if (about == null) {
                ctx.status(404);
                return "About not Found";
            }


            // ✅ Convert local banner image path to URL for client
            String image = about.getImage();
            if (image != null && !image.isBlank()) {
                // Get only the file name
                String fileName = Paths.get(image).getFileName().toString();
                // Prepend hosted path
                about.setImage("/uploads/" + fileName);
            }

            ctx.status(200);
            return new Gson().toJson(about);

        } catch (ResourceNotFoundException e) {
            ctx.status(404);
            return "About not Found";
        } catch (Exception e) {
            ctx.status(500);
            return e.getMessage();
        }
    }

    @Override
    public String save(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Creating About!");
        try {
            AboutRequest request = parseFormData(ctx);
            return new Gson().toJson(aboutRepository.save(request));
        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error creating About: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String updateAbout(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Upserting About!");
        try {
            Optional<About> existingAbout = aboutRepository.getAboutById("1");

            AboutRequest request = parseFormData(ctx);

            // Preserve old image if none uploaded
            if ((request.getImage() == null || request.getImage().isBlank()) && existingAbout.isPresent()) {
                request.setImage(existingAbout.get().getImage());
            }

            if (existingAbout.isEmpty()) {
                LOG.info("About not found, creating new About...");
                return new Gson().toJson(aboutRepository.save(request));
            } else {
                LOG.info("Updating existing About...");
                return new Gson().toJson(aboutRepository.updateAbout(request));
            }

        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error upserting About: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Helper method to parse form fields, file, and JSON whyUs array
     */
    private AboutRequest parseFormData(Context ctx) throws Exception {
        String title = ctx.formParam("title");
        String description = ctx.formParam("description");
        String metaTitle = ctx.formParam("metaTitle");
        String metaKeywords = ctx.formParam("metaKeywords");
        String metaDescription = ctx.formParam("metaDescription");

        Map<String, String> fields = new HashMap<>();
        fields.put("title", title);
        fields.put("description", description);
        fields.put("metaTitle", metaTitle);
        fields.put("metaKeywords", metaKeywords);
        fields.put("metaDescription", metaDescription);

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                throw new IllegalArgumentException("Bad Request: " + entry.getKey() + " is required");
            }
        }

        AboutRequest request = new AboutRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setMetaTitle(metaTitle);
        request.setMetaKeywords(metaKeywords);
        request.setMetaDescription(metaDescription);

        // Handle image upload
        UploadedFile file = ctx.uploadedFile("image");
        if (file != null) {
            if (file.contentType() == null || !file.contentType().startsWith("image/")) {
                throw new IllegalArgumentException("Invalid file type, only images allowed");
            }
            if (file.size() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Maximum image size is 10MB");
            }

            String extension = file.filename().substring(file.filename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + extension;

            Path uploadDir = Paths.get("uploads");
            Files.createDirectories(uploadDir);

            Path filePath = uploadDir.resolve(fileName);
            try (InputStream is = file.content()) {
                Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            request.setImage(filePath.toString());
        }

        // Parse whyUs JSON array
        String whyUsJson = ctx.formParam("whyUs");
        if (whyUsJson != null && !whyUsJson.isBlank()) {
            Type listType = new TypeToken<List<WhyUs>>() {}.getType();
            List<WhyUs> whyUsList = new Gson().fromJson(whyUsJson, listType);
            request.setWhyUs(whyUsList);
        }

        return request;
    }
}