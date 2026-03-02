package com.waarc.team;

import com.waarc.config.DbConnection;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamRepositoryImpl implements TeamRepository {
    private static final Log LOG = LogFactory.getLog(TeamRepositoryImpl.class);

    @Override
    public List<Team> getTeam() {
        String sql = "SELECT * FROM Team";
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            List<Team> teams = new ArrayList<>();
            while (rs.next()) {

                Team team = Team.builder(

                        ).id(rs.getInt("id"))
                        .image(rs.getString("image"))
                        .name(rs.getString("name"))
                        .position(rs.getString("position"))
                        .location(rs.getString("location"))
                        .build();

                teams.add(team);
            }
            return teams;

        } catch (Exception e) {
            LOG.error("Failed to get Team !" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public String save(TeamRequest request) {

        String sql = "INSERT INTO Team (image,name,position,location) values (?,?,?,?)";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, request.getImage());
            stmt.setString(2, request.getName());
            stmt.setString(3, request.getPosition());
            stmt.setString(4, request.getLocation());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                LOG.error("Failed to save Team !");
                throw new OperationFailedException("Failed to insert Team", 500);
            }
            return "Team inserted successfully";
        } catch (Exception e) {
            LOG.error("Team insertion Failed " + e.getMessage());
            throw new OperationFailedException("Failed to insert Team", 500);
        }
    }

    @Override
    public String updateTeam(TeamRequest request) {

        String sql = "UPDATE Team SET image = ?, name = ? , position = ? , location = ? where id = ?";
        try (Connection conn = DbConnection.getCon(); PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, request.getImage());
            stmt.setString(2, request.getName());
            stmt.setString(3, request.getPosition());
            stmt.setString(4, request.getLocation());
            stmt.setInt(5,request.getId());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                LOG.error("Failed to update Team");
                throw new OperationFailedException("Failed to update Team", 500);
            }
            LOG.info("Team updated SuccessFul !");
            return "Team Updated Successfully";

        } catch (Exception e) {
            LOG.error("Failed to update Team" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public Team getTeamById(String id) {
        String sql = "SELECT * FROM Team where id = ? ";
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Team.builder(
                        ).image(rs.getString("image"))
                        .position(rs.getString("position"))
                        .location(rs.getString("location"))
                        .name(rs.getString("name"))
                        .id(rs.getInt("id"))
                        .build();
            } else {
                 throw  new ResourceNotFoundException("Team not found");
            }
        } catch (Exception e) {
            LOG.error("Failed to get Team !" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public String deleteTeam(String id) {
        String sql = "delete from Team where id = ? ";
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                LOG.error("Failed to delete Team");
            }
            return "Team deleted Successfully";

        } catch (Exception e) {
            LOG.error("Failed to get Team !" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }
}
