package com.waarc.home;

import com.google.gson.Gson;

import com.waarc.exception.ResourceNotFoundException;
import com.waarc.home.pojo.HomeRequest;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class HomeServiceImpl implements HomeService {
    private final HomeRepository homerepository = new HomeRepositoryImpl();
    private final Logger LOG = LogManager.getLogger(HomeServiceImpl.class);

    @Override
    public String getHome(Context ctx) {

        ctx.contentType("application/json");
        try {
            Home home = homerepository.getHome();
            if (home == null) {
                ctx.status(404);
                return "Home not Found";
            }

            // ✅ Convert local banner image path to URL for client
            String bannerImagePath = home.getBannerImage();
            if (bannerImagePath != null && !bannerImagePath.isBlank()) {
                // Get only the file name
                String fileName = Paths.get(bannerImagePath).getFileName().toString();
                // Prepend hosted path
                home.setBannerImage("/uploads/" + fileName);
            }

            ctx.status(200);
            return new Gson().toJson(home);

        } catch (ResourceNotFoundException e) {
            ctx.status(404);
            return "Home not Found";
        } catch (Exception e) {
            ctx.status(500);
            return e.getMessage();
        }
    }

    @Override
    public String save(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Creating Home!");

        try {
            // 1️⃣ Read form fields
            String title = ctx.formParam("title");
            String description = ctx.formParam("description");
            String metaTitle = ctx.formParam("metaTitle");
            String metaKeywords = ctx.formParam("metaKeywords");
            String metaDescription = ctx.formParam("metaDescription");

            // 2️⃣ Validate all required fields
            if (title == null || title.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request: title is required";
            }
            if (description == null || description.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request: description is required";
            }
            if (metaTitle == null || metaTitle.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request:  metaTitle is required";
            }
            if (metaKeywords == null || metaKeywords.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request: metaKeywords is required";
            }
            if (metaDescription == null || metaDescription.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request:  metaDescription is required";
            }

            // 3️⃣ Create request object
            HomeRequest request = new HomeRequest();
            request.setTitle(title);
            request.setDescription(description);
            request.setMetaTitle(metaTitle);
            request.setMetaKeywords(metaKeywords);
            request.setMetaDescription(metaDescription);

            // 4️⃣ Handle file upload
            UploadedFile file = ctx.uploadedFile("image");
            if (file == null) {
                ctx.status(400);
                return "Bad Request: Image is required";
            }

            // 5️⃣ Validate file type
            String contentType = file.contentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                ctx.status(400);
                return "Invalid file type, only images allowed";
            }

            // 6️⃣ Validate file size (10MB max)
            if (file.size() > 10 * 1024 * 1024) {
                ctx.status(400);
                return "Maximum image size is 10MB";
            }

            // 7️⃣ Generate safe filename
            String extension = file.filename().substring(file.filename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + extension;

            // 8️⃣ Create uploads directory if not exists
            Path uploadDir = Paths.get("uploads");
            Files.createDirectories(uploadDir);

            // 9️⃣ Save the file
            Path filePath = uploadDir.resolve(fileName);
            try (InputStream is = file.content()) {
                Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 10️⃣ Set banner image path
            request.setBannerImage(filePath.toString());

            // 11️⃣ Save to repository
            return homerepository.save(request);

        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error creating home: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String updateHome(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Upserting Home!");

        try {
            // 1️⃣ Fetch existing home data
            Optional<Home> existingHome = homerepository.getHomeById("1");

            // 2️⃣ Read form fields
            String title = ctx.formParam("title");
            String description = ctx.formParam("description");
            String metaTitle = ctx.formParam("metaTitle");
            String metaKeywords = ctx.formParam("metaKeywords");
            String metaDescription = ctx.formParam("metaDescription");

            // 3️⃣ Validate all required fields
            Map<String, String> fields = Map.of(
                    "title", title,
                    "description", description,
                    "metaTitle", metaTitle,
                    "metaKeywords", metaKeywords,
                    "metaDescription", metaDescription
            );

            for (Map.Entry<String, String> entry : fields.entrySet()) {
                if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                    ctx.status(400);
                    return "Bad Request: " + entry.getKey() + " is required";
                }
            }
            // 4️⃣ Prepare request object
            HomeRequest request = new HomeRequest();
            request.setTitle(title);
            request.setDescription(description);
            request.setMetaTitle(metaTitle);
            request.setMetaKeywords(metaKeywords);
            request.setMetaDescription(metaDescription);

            // 5️⃣ Handle file upload (optional)
            UploadedFile file = ctx.uploadedFile("image");
            if (file != null) {
                String contentType = file.contentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    ctx.status(400);
                    return "Invalid file type, only images allowed";
                }

                if (file.size() > 10 * 1024 * 1024) {
                    ctx.status(400);
                    return "Maximum image size is 10MB";
                }

                String extension = file.filename().substring(file.filename().lastIndexOf("."));
                String fileName = UUID.randomUUID() + extension;

                Path uploadDir = Paths.get("uploads");
                Files.createDirectories(uploadDir);

                // Delete old image if exists
                if (existingHome.isPresent() && existingHome.get().getBannerImage() != null
                        && !existingHome.get().getBannerImage().isBlank()) {
                    Path oldFile = Paths.get(existingHome.get().getBannerImage());
                    try {
                        Files.deleteIfExists(oldFile);
                        LOG.info("Deleted old image: " + existingHome.get().getBannerImage());
                    } catch (IOException e) {
                        LOG.warn("Failed to delete old image: " + existingHome.get().getBannerImage(), e);
                    }
                }

                // Save new image
                Path filePath = uploadDir.resolve(fileName);
                try (InputStream is = file.content()) {
                    Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                request.setBannerImage(filePath.toString());
            } else if (existingHome != null) {
                // Keep old image if exists
                request.setBannerImage(existingHome.get().getBannerImage());
            }

            // 6️⃣ Upsert: Create if not exists, else update
            if (existingHome.isEmpty()) {
                LOG.info("Home not found, creating new home...");
                return homerepository.save(request);
            } else {
                LOG.info("Updating existing home...");
                return homerepository.updateHome(request);
            }

        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error upserting home: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }
}
