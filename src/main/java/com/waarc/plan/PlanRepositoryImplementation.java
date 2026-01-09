package com.waarc.plan;

import com.waarc.config.DataBaseSourceClass;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import com.waarc.plan.pojo.PlanRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlanRepositoryImplementation implements PlanRepository {
    private static final Log log = LogFactory.getLog(PlanRepositoryImplementation.class);

    @Override
    public Optional<Plan> getplan(int id) {
        String sql = "SELECT * FROM plan WHERE id=?";
        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                log.info("Getting plan with id :" + id);
                return Optional.of(
                        new Plan(
                                rs.getInt("id"),
                                rs.getString("tag"),
                                rs.getString("image"),
                                rs.getString("title"),
                                rs.getString("description")
                        ));
            }

        } catch (Exception e) {
            log.error("Failed to Get Plan with Id :" + id);
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return Optional.empty();
    }

    @Override
    public List<Plan> getAllplans() {

        String sql = "SELECT * FROM plan ";
        List<Plan> plans = new ArrayList<>();
        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Plan plan = new Plan(
                        rs.getInt("id"),
                        rs.getString("tag"),
                        rs.getString("image"),
                        rs.getString("title"),
                        rs.getString("description")
                );
                plans.add(plan);
            }
            log.info("Getting all plans");
            return plans;

        } catch (Exception e) {
            log.error("Failed to get Plans");
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public Plan save(PlanRequest request) {

        String sql = "INSERT INTO plan (tag,image,title,description) values (?,?,?,?)";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, request.getTag());
            stmt.setString(2, request.getImage());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to insert plan");
                throw new OperationFailedException("Failed to insert plan", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {
                    log.info("Created a new plan");

                    return new Plan(
                            keySet.getInt(1),
                            request.getTag(),
                            request.getImage(),
                            request.getTitle(),
                            request.getDescription()
                    );
                }
            } catch (Exception e) {
                log.error("Failed to insert plan :" + e.getMessage());
                throw new ResourceNotFoundException(e.getMessage());
            }
        } catch (Exception e) {
            log.error("Failed to insert plan :" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }

    @Override
    public Plan updateplan(PlanRequest request, int planId) {

        String sql = "UPDATE plan SET tag = ?, image = ? , title = ? , description = ? where id = ?";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, request.getTag());
            stmt.setString(2, request.getImage());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());
            stmt.setInt(5, planId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to update plan");
                throw new OperationFailedException("Failed to update plan", 500);
            }
            log.info("Plan update successFul !");
            return new Plan(planId, request.getTag(), request.getImage(), request.getTitle(), request.getDescription());

        } catch (Exception e) {
            log.error("Failed to update plan :"+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public Plan deleteplan(int planId) {
        String sql = "DELETE FROM plan WHERE  id = ?";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, planId);

            int affectedRow = stmt.executeUpdate();

            if (!(affectedRow > 0)) {
                log.error("Failed to delete plan");
                throw new OperationFailedException("Failed to delete plan", 500);
            }
            log.info("Plan deleted with id : "+ planId);

        } catch (Exception e) {
            log.error("Failed to delete plan with id :"+planId);
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }
}
