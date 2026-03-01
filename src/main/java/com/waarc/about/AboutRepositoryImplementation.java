package com.waarc.about;


import com.waarc.about.pojo.AboutRequest;
import com.waarc.config.DbConnection;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class AboutRepositoryImplementation implements AboutRepository {
    private static final Log log = LogFactory.getLog(AboutRepositoryImplementation.class);

    @Override
    public Optional<About> getAbout() {
        String sql = "SELECT * FROM about ";
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                log.info("Getting About !");
                return Optional.of(
                        new About(
                                rs.getInt("id"),
                                rs.getString("description")
                        ));
            }

        } catch (Exception e) {
            log.error("Failed to get About !"+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return Optional.empty();
    }

    @Override
    public About save(AboutRequest request) {

        String sql = "INSERT INTO about (description) values (?)";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, request.getDescription());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to insert about");
                throw new OperationFailedException("Failed to insert about", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {
log.info("About inserted successfully !");
                    return new About(
                            keySet.getInt(1),
                            request.getDescription()
                    );
                }
            } catch (Exception e) {
                log.error("About insertion Failed " + e.getMessage());
                throw new ResourceNotFoundException(e.getMessage());
            }


        } catch (Exception e) {
            log.error("About insertion Failed " + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }

    @Override
    public About updateAbout(AboutRequest request) {

        String sql = "UPDATE about SET description = ? ";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, request.getDescription());


            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to update about");
                throw new OperationFailedException("Failed to update about", 500);
            }
 log.info("About updated SuccessFul !");
            return new About(request.getDescription());

        } catch (Exception e) {
            log.error("Failed to update about"+e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }
}
