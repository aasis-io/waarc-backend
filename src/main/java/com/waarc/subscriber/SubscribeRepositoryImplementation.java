package com.waarc.subscriber;

import com.waarc.config.DataBaseSourceClass;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import com.waarc.subscriber.pojo.SubscribeRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubscribeRepositoryImplementation implements SubscribeRepository {
    private static final Log log = LogFactory.getLog(SubscribeRepositoryImplementation.class);


    @Override
    public List<Subscribe> getAllSubscriber() {
        String sql = "SELECT * FROM subscribed_user ";
        try(Connection connection = DataBaseSourceClass.getDataSource().getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs  = stmt.executeQuery();
            List<Subscribe> subscriber = new ArrayList<>();

            while(rs.next()) {
                Subscribe subscribe = new Subscribe(rs.getInt("id"),rs.getString("email"));
                subscriber.add(subscribe);
            }
            return subscriber;

        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage(),500);
        }
    }

    @Override
    public Subscribe save(SubscribeRequest request) {

        String sql = "INSERT INTO subscribed_user (email) values (?)";
        try (Connection conn = DataBaseSourceClass.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, request.getEmail());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to insert subscriber");
                throw new OperationFailedException("Failed to insert subscriber", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {

                    return new Subscribe(
                            keySet.getInt(1),
                            request.getEmail()
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
