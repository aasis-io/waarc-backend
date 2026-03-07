package com.waarc.team;

import com.google.gson.Gson;
import com.waarc.exception.ResourceNotFoundException;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository = new TeamRepositoryImpl();
    private final Logger LOG = LogManager.getLogger(TeamServiceImpl.class);

    @Override
    public String getTeam(Context ctx) {

        ctx.contentType("application/json");
        try {
           List<Team> teams = teamRepository.getTeam();
            if (teams == null) {
                ctx.status(404);
                return "Team not Found";
            }

            for (Team team : teams) {
                // ✅ Convert local banner image path to URL for client
                String bannerImagePath = team.getImage();
                if (bannerImagePath != null && !bannerImagePath.isBlank()) {
                    // Get only the file name
                    String fileName = Paths.get(bannerImagePath).getFileName().toString();
                    // Prepend hosted path
                    team.setImage("/uploads/" + fileName);
                }
            }
            
            ctx.status(200);
            return new Gson().toJson(teams);

        } catch (ResourceNotFoundException e) {
            ctx.status(404);
            return "Team not Found";
        } catch (Exception e) {
            ctx.status(500);
            return e.getMessage();
        }
    }

    @Override
    public String save(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Creating Team!");

        try {
            // 1️⃣ Read form fields
            String name = ctx.formParam("name");
            String position = ctx.formParam("position");
            String location = ctx.formParam("location");
             

            // 2️⃣ Validate all required fields
            if (name == null || name.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request: name is required";
            }
            if (position == null || position.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request: position is required";
            }
            if (location == null || location.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request:  location is required";
            }

            // 3️⃣ Create request object
            TeamRequest request = new TeamRequest();
            request.setName(name);
            request.setPosition(position);
            request.setLocation(location);

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
            return teamRepository.save(request);

        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error creating Team: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String updateTeam(Context ctx) {
        ctx.contentType("application/json");
        LOG.info("Upserting Team!");

        String id = ctx.queryParam("id");
        if (id == null || id.trim().isEmpty()) {
            ctx.status(400);
            return "Bad Request: id is required";
        }

        try {
            // 1️⃣ Fetch existing Team data
            Team existingTeam = teamRepository.getTeamById(id);

            // 2️⃣ Read form fields
            String name = ctx.formParam("name");
            String position = ctx.formParam("position");
            String location = ctx.formParam("location");

            // 3️⃣ Validate all required fields
            Map<String, String> fields = Map.of(
                    "name", name,
                    "position", position,
                    "location", location
            );

            for (Map.Entry<String, String> entry : fields.entrySet()) {
                if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                    ctx.status(400);
                    return "Bad Request: " + entry.getKey() + " is required";
                }
            }
            // 4️⃣ Prepare request object
            TeamRequest request = new TeamRequest();
            request.setId(Integer.parseInt(id));
            request.setName(name);
            request.setPosition(position);
            request.setLocation(location);


            // 5️⃣ Handle file upload (optional)
            UploadedFile file = ctx.uploadedFile("image");
            if( file == null) {
                ctx.status(400);
                return "Bad Request: Image is required";
            }
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
                if (existingTeam.getImage() != null
                        && !existingTeam.getImage().isBlank()) {
                    Path oldFile = Paths.get(existingTeam.getImage());
                    try {
                        Files.deleteIfExists(oldFile);
                        LOG.info("Deleted old image: " + existingTeam.getImage());
                    } catch (IOException e) {
                        LOG.warn("Failed to delete old image: " + existingTeam.getImage(), e);
                    }
                }

                // Save new image
                Path filePath = uploadDir.resolve(fileName);
                try (InputStream is = file.content()) {
                    Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                request.setImage(filePath.toString());
            }
            LOG.info("Updating existing Team...");
            return teamRepository.updateTeam(request);

        }catch (ResourceNotFoundException e) {
            ctx.status(404);
            LOG.error("Error updating Team: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error updating Team: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String deleteTeam(Context ctx) {

        try {
            String id = ctx.queryParam("id");
            if (id == null || id.trim().isEmpty()) {
                ctx.status(400);
                return "Bad Request: id is required";
            }
            Team team = teamRepository.getTeamById(id);
            //checking if team is present
            String response = teamRepository.deleteTeam(String.valueOf(team.getId()));

            // Delete old image if exists
            if (team.getImage() != null
                    && !team.getImage().isBlank()) {
                Path oldFile = Paths.get(team.getImage());
                try {
                    Files.deleteIfExists(oldFile);
                    LOG.info("Deleted team image: " + team.getImage());
                } catch (IOException e) {
                    LOG.warn("Failed to delete team image: " + team.getImage(), e);
                }
            }
            return response;
        }catch ( ResourceNotFoundException e) {
            ctx.status(404);
            LOG.error("Error deleting Team: " + e.getMessage());
            return "Team not Found";
        }catch (Exception e) {
            ctx.status(500);
            LOG.error("Error deleting Team: " + e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String getTeamMember(@NotNull Context ctx) {
        ctx.contentType("application/json");

        try {
            Team team = teamRepository.getTeamById(ctx.queryParam("id"));

            // ✅ Convert local banner image path to URL for client
            String bannerImagePath = team.getImage();
            if (bannerImagePath != null && !bannerImagePath.isBlank()) {
                // Get only the file name
                String fileName = Paths.get(bannerImagePath).getFileName().toString();
                // Prepend hosted path
                team.setImage("/uploads/" + fileName);
            }
            return new Gson().toJson(team);
        }catch (ResourceNotFoundException e) {
            ctx.status(404);
            LOG.error("Error updating Team: " + e.getMessage());
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            ctx.status(500);
            LOG.error("Error updating Team: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
