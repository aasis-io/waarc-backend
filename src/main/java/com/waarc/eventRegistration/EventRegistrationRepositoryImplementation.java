package com.waarc.eventRegistration;

import com.waarc.config.DataBaseSourceClass;
import com.waarc.event.Event;
import com.waarc.eventRegistration.pojo.EventRegistrationRequest;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import com.waarc.service.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventRegistrationRepositoryImplementation implements EventRegistrationRepository {
    private static final Log log = LogFactory.getLog(EventRegistrationRepositoryImplementation.class);

    //event and registered user linking code
//    @Override
//    public Optional<EventRegistration> getRegistrationById(int eventRegistrationId) {
//        String sql = "SELECT * FROM registerd_user WHERE id = ?";
//        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
//             PreparedStatement stmt = connection.prepareStatement(sql)) {
//
//            stmt.setInt(1, eventRegistrationId);
//
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                return Optional.of(
//                        new EventRegistration(
//                                rs.getInt("id"),
//                                rs.getString("full_name"),
//                                rs.getString("email"),
//                                rs.getString("phone")
//                        ));
//            }
//
//        } catch (Exception e) {
//            throw new OperationFailedException(e.getMessage(), 500);
//        }
//        return null;
//    }
//
//    @Override
//    public List<EventRegistration> getEventRegistration(int eventId) {
//        String sql = "SELECT ru.id AS id, ru.full_name AS fullName,ru.email AS email,ru.phone AS phone FROM registerd_user" +
//                " ru inner join event_has_registereduser eru on ru.id = eru.registeredUser_id inner join " +
//                " event e on eru.event_Id = e.id  where e.id = ?";
//        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
//             PreparedStatement stmt = connection.prepareStatement(sql)) {
//
//            stmt.setInt(1,eventId);
//
//            ResultSet rs = stmt.executeQuery();
//            List<EventRegistration> eventRegistrations = new ArrayList<>();
//            while (rs.next()) {
//                EventRegistration registration =  new EventRegistration(
//                                rs.getInt("id"),
//                                rs.getString("fullName"),
//                                rs.getString("email"),
//                                rs.getString("phone")
//                        );
//                eventRegistrations.add(registration);
//            }
//
//        } catch (Exception e) {
//            throw new OperationFailedException(e.getMessage(), 500);
//        }
//        return null;
//    }
//
//    @Override
//    public EventRegistration save(EventRegistrationRequest request,int eventId) {
//
//        String sql = "INSERT INTO eventRegistration (fullname,email,phone) values (?,?,?)";
//        String sql2 = " INSERT INTO " +
//                " event_has_registereduser(event_id,registereduser_id) " +
//                " values (?,?)";
//        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
//
//            stmt.setString(1, request.getFullName());
//            stmt.setString(2, request.getEmail());
//            stmt.setString(3, request.getPhone());
//
//            int affectedRow = stmt.executeUpdate();
//            if (!(affectedRow > 0)) {
//                log.error("Failed to insert EventRegistration");
//                throw new OperationFailedException("Failed to insert EventRegistration", 500);
//            }
//
//            try (ResultSet keySet = stmt.getGeneratedKeys()) {
//
//                if (keySet.next()) {
//                    int generatedId  = keySet.getInt(1);
//
//                    try(PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
//                        int rowAffected = stmt.executeUpdate();
//                        if(!(rowAffected>0)) {
//                            log.error("Failed to insert Event_has_registeredUser");
//                            throw new OperationFailedException("Failed to insert Event_has_registeredUser", 500);
//                        }
//                    } catch (Exception e) {
//                        throw new OperationFailedException(e.getMessage(), 500);
//                    }
//
//                    return new EventRegistration(
//                            keySet.getInt(1),
//                            request.getFullName(),
//                            request.getEmail(),
//                            request.getPhone()
//                    );
//                }
//            } catch (Exception e) {
//                throw new ResourceNotFoundException(e.getMessage());
//            }
//
//
//        } catch (Exception e) {
//            throw new OperationFailedException(e.getMessage(), 500);
//        }
//        return null;
//    }
//
//
//    @Override
//    public EventRegistration deleteEventRegistration(int eventId,int eventRegistrationId) {
//        String sql = "DELETE FROM event_has_registered WHERE  registerduser_id = ? AND event_id = ?";
//        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);) {
//
//            stmt.setInt(1, eventRegistrationId);
//            stmt.setInt(2,eventId);
//
//            int affectedRow = stmt.executeUpdate();
//            if (!(affectedRow > 0)) {
//                log.error("Failed to delete EventRegistration");
//                throw new OperationFailedException("Failed to delete EventRegistration", 500);
//            }
//
//        } catch (Exception e) {
//            throw new OperationFailedException(e.getMessage(), 500);
//        }
//        return null;
//    }

    //Simple registration code
    @Override
    public List<EventRegistration> getEventRegistrations() {
        String sql = "SELECT ru.id AS id, ru.full_name AS fullName,ru.email AS email,ru.phone AS phone FROM registered_user ru";

        List<EventRegistration> eventRegistrations = new ArrayList<>();
        try (Connection connection = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                EventRegistration registration =  new EventRegistration(
                                rs.getInt("id"),
                                rs.getString("fullName"),
                                rs.getString("email"),
                                rs.getString("phone")
                        );
                eventRegistrations.add(registration);
            }

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return eventRegistrations;
    }

    @Override
    public EventRegistration save(EventRegistrationRequest request) {

        String sql = "INSERT INTO registered_user (full_name,email,phone) values (?,?,?)";

        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, request.getFullName());
            stmt.setString(2, request.getEmail());
            stmt.setString(3, request.getPhone());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to insert EventRegistration");
                throw new OperationFailedException("Failed to insert EventRegistration", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {
                    return new EventRegistration(
                            keySet.getInt(1),
                            request.getFullName(),
                            request.getEmail(),
                            request.getPhone()
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

}
