package com.waarc.about;

import com.waarc.about.pojo.AboutRequest;
import com.waarc.config.DbConnection;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AboutRepositoryImpl implements AboutRepository {

    private static final Logger LOG = LogManager.getLogger(AboutRepositoryImpl.class);
    private static final int ABOUT_ID = 1;

    @Override
    public About getAbout() {
        String sql = "SELECT a.*, wu.title as wu_title, wu.description as wu_description " +
                "FROM about a LEFT JOIN why_us wu ON a.id = wu.about_id WHERE a.id = ?";
        About about = null;
        List<WhyUs> whyUsList = new ArrayList<>();

        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, ABOUT_ID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (about == null) {
                    about = new About(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("image"),
                            rs.getString("meta_title"),
                            rs.getString("meta_description"),
                            rs.getString("meta_keywords"),
                            new ArrayList<>()
                    );
                }
                if (rs.getString("wu_title") != null) {
                    whyUsList.add(new WhyUs(rs.getString("wu_title"), rs.getString("wu_description")));
                }
            }

            if (about == null) throw new ResourceNotFoundException("About not found");
            about.setWhyUs(whyUsList);
            return about;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to get About! " + e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public About save(AboutRequest request) {
        String insertAbout = "INSERT INTO about (id, title, description, image, meta_title, meta_description, meta_keywords) VALUES (?,?,?,?,?,?,?)";
        String insertWhyUs = "INSERT INTO why_us (title, description, about_id) VALUES (?,?,?)";

        try (Connection conn = DbConnection.getCon()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement aboutStmt = conn.prepareStatement(insertAbout)) {
                    aboutStmt.setInt(1, ABOUT_ID);
                    aboutStmt.setString(2, request.getTitle());
                    aboutStmt.setString(3, request.getDescription());
                    aboutStmt.setString(4, request.getImage());
                    aboutStmt.setString(5, request.getMetaTitle());
                    aboutStmt.setString(6, request.getMetaDescription());
                    aboutStmt.setString(7, request.getMetaKeywords());
                    if (aboutStmt.executeUpdate() == 0)
                        throw new OperationFailedException("Failed to insert About", 500);
                }

                if (request.getWhyUs() != null && !request.getWhyUs().isEmpty()) {
                    LOG.info("Inserting {} whyUs entries for about_id={}", request.getWhyUs().size(), ABOUT_ID);
                    try (PreparedStatement whyUsStmt = conn.prepareStatement(insertWhyUs)) {
                        for (WhyUs whyUs : request.getWhyUs()) {
                            whyUsStmt.setString(1, whyUs.getTitle());
                            whyUsStmt.setString(2, whyUs.getDescription());
                            whyUsStmt.setInt(3, ABOUT_ID);
                            whyUsStmt.addBatch();
                        }
                        whyUsStmt.executeBatch();
                    }
                } else {
                    LOG.warn("No whyUs entries provided during save for about_id={}", ABOUT_ID);
                }

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                LOG.error("About insertion failed, rolling back! " + e.getMessage(), e);
                throw new OperationFailedException(e.getMessage(), 500);
            }

            return new About(
                    ABOUT_ID,
                    request.getTitle(),
                    request.getDescription(),
                    request.getImage(),
                    request.getMetaTitle(),
                    request.getMetaDescription(),
                    request.getMetaKeywords(),
                    request.getWhyUs() != null ? request.getWhyUs() : new ArrayList<>()
            );

        } catch (OperationFailedException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Connection error during save! " + e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public About updateAbout(AboutRequest request) {
        String updateAbout = "UPDATE about SET title=?, description=?, image=?, meta_title=?, meta_description=?, meta_keywords=? WHERE id=?";
        String deleteWhyUs = "DELETE FROM why_us WHERE about_id=?";
        String insertWhyUs = "INSERT INTO why_us (title, description, about_id) VALUES (?,?,?)";

        try (Connection conn = DbConnection.getCon()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(updateAbout)) {
                    stmt.setString(1, request.getTitle());
                    stmt.setString(2, request.getDescription());
                    stmt.setString(3, request.getImage());
                    stmt.setString(4, request.getMetaTitle());
                    stmt.setString(5, request.getMetaDescription());
                    stmt.setString(6, request.getMetaKeywords());
                    stmt.setInt(7, ABOUT_ID);
                    if (stmt.executeUpdate() == 0)
                        throw new ResourceNotFoundException("About not found");
                }

                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteWhyUs)) {
                    deleteStmt.setInt(1, ABOUT_ID);
                    deleteStmt.executeUpdate();
                }

                if (request.getWhyUs() != null && !request.getWhyUs().isEmpty()) {
                    LOG.info("Re-inserting {} whyUs entries for about_id={}", request.getWhyUs().size(), ABOUT_ID);
                    try (PreparedStatement whyUsStmt = conn.prepareStatement(insertWhyUs)) {
                        for (WhyUs whyUs : request.getWhyUs()) {
                            whyUsStmt.setString(1, whyUs.getTitle());
                            whyUsStmt.setString(2, whyUs.getDescription());
                            whyUsStmt.setInt(3, ABOUT_ID);
                            whyUsStmt.addBatch();
                        }
                        whyUsStmt.executeBatch();
                    }
                } else {
                    LOG.warn("No whyUs entries provided during update for about_id={}", ABOUT_ID);
                }

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                LOG.error("Failed to update About, rolling back! " + e.getMessage(), e);
                throw new OperationFailedException(e.getMessage(), 500);
            }

            return new About(
                    ABOUT_ID,
                    request.getTitle(),
                    request.getDescription(),
                    request.getImage(),
                    request.getMetaTitle(),
                    request.getMetaDescription(),
                    request.getMetaKeywords(),
                    request.getWhyUs() != null ? request.getWhyUs() : new ArrayList<>()
            );

        } catch (OperationFailedException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Connection error during update! " + e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public Optional<About> getAboutById(String number) {
        String sql = "SELECT a.*, wu.id as wu_id, wu.title as wu_title, wu.description as wu_description " +
                "FROM about a LEFT JOIN why_us wu ON a.id = wu.about_id WHERE a.id = ?";

        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(number));
            ResultSet rs = stmt.executeQuery();

            About about = null;
            List<WhyUs> whyUsList = new ArrayList<>();

            while (rs.next()) {
                if (about == null) {
                    about = new About();
                    about.setTitle(rs.getString("title"));
                    about.setDescription(rs.getString("description"));
                    about.setImage(rs.getString("image"));
                    about.setMetaTitle(rs.getString("meta_title"));
                    about.setMetaDescription(rs.getString("meta_description"));
                    about.setMetaKeywords(rs.getString("meta_keywords"));
                }

                int wuId = rs.getInt("wu_id");
                if (wuId != 0) {
                    whyUsList.add(new WhyUs(rs.getString("wu_title"), rs.getString("wu_description")));
                }
            }

            if (about != null) {
                about.setWhyUs(whyUsList);
                return Optional.of(about);
            }

        } catch (Exception e) {
            LOG.error("Failed to get About by ID! " + e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), 500);
        }

        return Optional.empty();
    }
}