package com.waarc.work;

import com.waarc.config.DataBaseSourceClass;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import com.waarc.work.pojo.WorkRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkRepositoryImplementation implements WorkRepository {
    private static final Log log = LogFactory.getLog(WorkRepositoryImplementation.class);

    @Override
    public Optional<Work> getWork(int id) {
        String sql = "SELECT * FROM Work WHERE id=?";
        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                log.info("Returning work with id :"+id);
                return Optional.of(
                        new Work(
                                rs.getInt("id"),
                                rs.getString("tag"),
                                rs.getString("image"),
                                rs.getString("title"),
                                rs.getString("description")
                        ));
            }

        } catch (Exception e) {
            log.info("Error : "+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return Optional.empty();
    }

    @Override
    public List<Work> getAllWorks() {
        String sql = "SELECT * FROM Work";

        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            List<Work> works = new ArrayList<>();
            while (rs.next()) {
                log.info("Returning All work");

                     Work work =   new Work(
                                rs.getInt("id"),
                                rs.getString("tag"),
                                rs.getString("image"),
                                rs.getString("title"),
                                rs.getString("description")
                        );
                     works.add(work);
            }
            return works;

        } catch (Exception e) {
            log.info("Error : "+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public Work save(WorkRequest request) {

        String sql = "INSERT INTO Work (tag,image,title,description) values (?,?,?,?)";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1,request.getTag());
            stmt.setString(2, request.getImage());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to insert Work");
                throw new OperationFailedException("Failed to insert Work", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {
                    log.info("Work inserted SuccessFully !");
                    return new Work(
                            keySet.getInt(1),
                            request.getTag(),
                            request.getImage(),
                            request.getTitle(),
                            request.getDescription()
                    );
                }
            } catch (Exception e) {
                log.error("Work insertion Failed : "+ e.getMessage());
                throw new ResourceNotFoundException(e.getMessage());
            }


        } catch (Exception e) {
            log.error("Work insertion Failed : "+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }

    @Override
    public Work updateWork(WorkRequest request, int WorkId) {

        String sql = "UPDATE Work SET tag = ?, image = ? , title = ? , description = ? where id = ?";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, request.getTag());
            stmt.setString(2, request.getImage());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());
            stmt.setInt(5, WorkId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to update Work");
                throw new OperationFailedException("Failed to update Work", 500);
            }
            log.info("Work updated with id : "+ WorkId);
            return new Work(WorkId, request.getTag(), request.getImage(), request.getTitle(), request.getDescription());

        } catch (Exception e) {
            log.error("Failed to update Work : "+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public Work deleteWork(int WorkId) {

        String sql = "DELETE FROM Work WHERE  id = ?";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, WorkId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to delete Work");
                throw new OperationFailedException("Failed to delete Work", 500);
            }

        } catch (Exception e) {
            log.error("Failed to delete Work : "+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }
}
