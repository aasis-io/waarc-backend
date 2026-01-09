package com.waarc.service;

import com.cloudinary.api.exceptions.ApiException;
import com.waarc.config.DataBaseSourceClass;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import com.waarc.service.pojo.ServiceRequest;
import com.waarc.user.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceRepositoryImplementation implements ServiceRepository {
    private static final Log log = LogFactory.getLog(ServiceRepositoryImplementation.class);

    @Override
    public Optional<Service> getService(int id) {
        String sql = "SELECT * FROM service WHERE id=?";
        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                log.info("Fetching Services");
                return Optional.of(
                        new Service(
                                rs.getInt("id"),
                                rs.getString("image"),
                                rs.getString("title"),
                                rs.getString("description")
                        ));
            }

        } catch (Exception e) {
            log.error("Failed to fetch service : "+e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return Optional.empty();
    }

    @Override
    public List<Service> getAllServices() {
        String sql = "SELECT * FROM service";
        List<Service> services = new ArrayList<>();
        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                log.info("Fetching Services");
                 Service service = new Service(
                                rs.getInt("id"),
                                rs.getString("image"),
                                rs.getString("title"),
                                rs.getString("description"));
                 services.add(service);
            }
            return services;

        } catch (Exception e) {
            log.error("Failed to fetch service : "+e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public Service save(ServiceRequest request) {

        String sql = "INSERT INTO service (image,title,description) values (?,?,?)";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, request.getImage());
            stmt.setString(2, request.getTitle());
            stmt.setString(3, request.getDescription());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to insert service");
                throw new OperationFailedException("Failed to insert service", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {
log.info("Creating a new Service");
                    return new Service(
                            keySet.getInt(1),
                            request.getImage(),
                            request.getTitle(),
                            request.getDescription()
                    );
                }
            } catch (Exception e) {
                log.error("Failed to insert Service :"+e.getMessage());
                throw new ResourceNotFoundException(e.getMessage());
            }


        } catch (Exception e) {
            log.error("Failed to insert Service :"+e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }

    @Override
    public Service updateService(ServiceRequest request, int serviceId) {

        String sql = "UPDATE Service SET image = ? , title = ? , description = ? where id = ?";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, request.getImage());
            stmt.setString(2, request.getTitle());
            stmt.setString(3, request.getDescription());
            stmt.setInt(4, serviceId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to update service");
                throw new OperationFailedException("Failed to update service", 500);
            }
            log.info("Service updated successfully !");
            return new Service(serviceId, request.getImage(), request.getTitle(), request.getDescription());

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public Service deleteService(int serviceId) {
        String sql = "DELETE FROM service WHERE  id = ?";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, serviceId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to delete service !");
                throw new OperationFailedException("Failed to delete service", 500);
            }

        } catch (Exception e) {
            log.error("Failed to delete Service !");
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }
}
