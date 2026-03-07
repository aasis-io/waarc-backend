package com.waarc.event;

import com.waarc.config.DbConnection;
import com.waarc.event.pojo.EventRequest;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class EventRepositoryImpl implements EventRepository {
    private static final Logger LOG = LogManager.getLogger(EventRepositoryImpl.class);

    @Override
    public Event getEvent() {
        String sql = "SELECT * FROM event WHERE id=?";
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1,"1");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Event(
                                rs.getInt("id"),
                                rs.getString("title"),
                        rs.getString("date"),
                                rs.getString("link"),
                        rs.getString("image")
                        );
            } else {
                LOG.error("No event found");
                throw new ResourceNotFoundException("Event not found");
            }

        } catch (ResourceNotFoundException e) {
             throw e;
        }catch (Exception e) {
            LOG.error("Error :" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public String save(EventRequest request) {

        String sql = "INSERT INTO event (id,title,date,link,image) values (?,?,?,?,?)";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1,"1");
            stmt.setString(2,request.getTitle());
            stmt.setString(3,request.getDate());
            stmt.setString(4,request.getLink());
            stmt.setString(5,request.getImage());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                LOG.error("Failed to insert event");
                throw new OperationFailedException("Failed to insert event", 500);
            }
            return "Event inserted successfully";

        } catch (Exception e) {
            LOG.error("Error :" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public String deleteEvent() {
        String sql = "DELETE FROM event WHERE  id = ?";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1,1);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                LOG.error("Failed to delete event");
                throw new OperationFailedException("Failed to delete event", 500);
            }

            LOG.info("Event deleted successfully");
            return "Event deleted successfully";
        } catch (Exception e) {
            LOG.error("Error :" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }
}
