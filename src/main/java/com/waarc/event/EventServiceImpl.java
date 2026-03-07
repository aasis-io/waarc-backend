package com.waarc.event;

import com.google.gson.Gson;
import com.waarc.event.pojo.EventRequest;
import com.waarc.exception.ResourceNotFoundException;
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

import java.util.UUID;

public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository = new EventRepositoryImpl();
    private static final Logger LOG = LogManager.getLogger(EventServiceImpl.class);

    @Override
    public String getEvent(Context ctx) {
        try {
            Event event = eventRepository.getEvent();

            // ✅ Convert local banner image path to URL for client
            String bannerImagePath = event.getImage();
            if (bannerImagePath != null && !bannerImagePath.isBlank()) {
                // Get only the file name
                String fileName = Paths.get(bannerImagePath).getFileName().toString();
                // Prepend hosted path
                event.setImage("/uploads/" + fileName);
            }
            return new Gson().toJson(event);
        } catch (ResourceNotFoundException e) {
           LOG.error("Error getting event", e.getMessage());
           ctx.status(404);
           return "Error getting event : "+ e.getMessage();
        } catch (Exception e) {
            LOG.error("Error getting event", e.getMessage());
            ctx.status(500);
            return "Error getting event : "+ e.getMessage();
        }

    }

    @Override
    public String save(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Creating Event!");

        try {
            // 1️⃣ Read form fields
            String title = ctx.formParam("title");
            String date = ctx.formParam("date");
            String link = ctx.formParam("link");

            // 2️⃣ Validate all required fields
            if (title == null || title.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request: title is required";
            }
            if (date == null || date.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request: date is required";
            }
            if (link == null || link.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request:  link is required";
            }


            // 3️⃣ Create request object
            EventRequest request = new EventRequest();
            request.setTitle(title);
            request.setDate(date);
            request.setLink(link);

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
            request.setImage(filePath.toString());

            // 11️⃣ Save to repository
            return eventRepository.save(request);

        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error creating Event: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    

    @Override
    public String deleteEvent(Context ctx) {
        try {
            Event event = eventRepository.getEvent();

            String response = eventRepository.deleteEvent();
            // Delete old image if exists
            if (event.getImage() != null
                    && !event.getImage().isBlank()) {
                Path oldFile = Paths.get(event.getImage());
                try {
                    Files.deleteIfExists(oldFile);
                    LOG.info("Deleted team image: " + event.getImage());
                } catch (IOException e) {
                    LOG.warn("Failed to delete team image: "+ e.getMessage());
                }
            }
            return response;
        } catch (ResourceNotFoundException e) {
            LOG.error("Error getting event", e);
            ctx.status(404);
            return "Error getting event : "+ e.getMessage();
        } catch (Exception e) {
            LOG.error("Error getting event", e);
            ctx.status(500);
            return "Error getting event : "+ e.getMessage();
        }
    }
}
