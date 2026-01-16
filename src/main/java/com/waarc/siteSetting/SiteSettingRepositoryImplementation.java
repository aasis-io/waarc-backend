package com.waarc.siteSetting;

import com.waarc.config.DataBaseSourceClass;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import com.waarc.siteSetting.pojo.SiteSettingRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SiteSettingRepositoryImplementation implements SiteSettingRepository {
    private static final Log log = LogFactory.getLog(SiteSettingRepositoryImplementation.class);

    @Override
    public Optional<SiteSetting> getSiteSetting() {
        String sql = "SELECT * FROM site_setting where id = 1 ";
        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                log.info("Fetching Site Setting");
                return Optional.of(
                        new SiteSetting(
                                rs.getInt("id"),
                                rs.getString("phone"),
                                rs.getString("email"),
                                rs.getString("location"),
                                rs.getString("facebook"),
                                rs.getString("instagram"),
                                rs.getString("linkedin"),
                                rs.getString("youtube")
                        ));
            }

        } catch (Exception e) {
            log.error("Failed to fetch site setting: " + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return Optional.empty();
    }

    @Override
    public SiteSetting updateSiteSetting(SiteSettingRequest request) {

        String sql = "UPDATE site_setting SET phone = ?, email = ?, location = ?, facebook = ?, instagram = ?, linkedin = ?, youtube = ? WHERE id = 1";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getPhone());
            stmt.setString(2, request.getEmail());
            stmt.setString(3, request.getLocation());
            stmt.setString(4, request.getFacebook());
            stmt.setString(5, request.getInstagram());
            stmt.setString(6, request.getLinkedin());
            stmt.setString(7, request.getYoutube());


            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to update site setting");
                throw new OperationFailedException("Failed to update site setting", 500);
            }
            log.info("Site Setting updated successfully!");
            return new SiteSetting(
                    request.getPhone(),
                    request.getEmail(),
                    request.getLocation(),
                    request.getFacebook(),
                    request.getInstagram(),
                    request.getLinkedin(),
                    request.getYoutube()
            );

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }


}