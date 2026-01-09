package com.waarc.event;

import com.waarc.config.DataBaseSourceClass;
import com.waarc.event.pojo.EventRequest;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class EventRepositoryImplementation implements EventRepository {
    private static final Log log = LogFactory.getLog(EventRepositoryImplementation.class);

    @Override
    public Optional<Event> getEvent(int id) {
        String sql = "SELECT * FROM event WHERE id=?";
        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return Optional.of(
                        new Event(
                                rs.getInt("id"),
                                rs.getString("name")
                        ));
            }

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }

    @Override
    public List<Event> getAllEvents() {
        return List.of();
    }

    @Override
    public Event save(EventRequest request) {

        String sql = "INSERT INTO event (name) values (?)";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, request.getName());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to insert event");
                throw new OperationFailedException("Failed to insert event", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {

                    return new Event(
                            keySet.getInt(1),
                            request.getName()
                    );
                }
            } catch (Exception e) {
                throw new ResourceNotFoundException(e.getMessage());
            }


        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }

    @Override
    public Event updateEvent(EventRequest request, int serviceId) {

        String sql = "UPDATE Service SET name = ? where id = ?";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, request.getName());
            stmt.setInt(2, serviceId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to update service");
                throw new OperationFailedException("Failed to update service", 500);
            }

            return new Event(serviceId, request.getName());

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public Event deleteEvent(int eventId) {
        String sql = "DELETE FROM event WHERE  id = ?";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, eventId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to delete event");
                throw new OperationFailedException("Failed to delete event", 500);
            }

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }
}
