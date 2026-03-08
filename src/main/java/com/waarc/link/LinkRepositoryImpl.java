package com.waarc.link;


import com.waarc.config.DbConnection;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LinkRepositoryImpl implements LinkRepository {

    private static final Logger LOG = LogManager.getLogger(LinkRepositoryImpl.class);


    @Override
    public List<Link> getlink() {
        String sql = "Select * from links";

        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {


            ResultSet rs = stmt.executeQuery();
            List<Link> links = new ArrayList<>();

            while (rs.next()) {
                Link link = new Link();
                link.setId(rs.getInt("id"));
                link.setLink(rs.getString("link"));
                link.setTitle(rs.getString("title"));
                links.add(link);
            }
            return links;

        } catch (Exception e) {
            LOG.error("Failed to get link! " + e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public String save(LinkRequest request) {
        String insertlink = "INSERT INTO links (title, link ) VALUES (?,?)";

        try (Connection conn = DbConnection.getCon();
        PreparedStatement stmt = conn.prepareStatement(insertlink)) {
            stmt.setString(1, request.getTitle());
            stmt.setString(2, request.getLink());
           int affectedRow = stmt.executeUpdate();
           if (affectedRow < 0) {
                throw new OperationFailedException("Failed to save link!", 500);
           }
           return "Link saved successfully!";

            } catch (Exception e) {
                LOG.error("link insertion failed " + e.getMessage(), e);
                throw new OperationFailedException(e.getMessage(), 500);
            }

    }

    @Override
    public String updatelink(LinkRequest request,int id) {
        String updatelink = "UPDATE links SET title=?, link=? WHERE id=?";

        try (Connection conn = DbConnection.getCon();
        PreparedStatement stmt = conn.prepareStatement(updatelink)) {
            stmt.setString(1, request.getTitle());
            stmt.setString(2, request.getLink());
            stmt.setInt(3, id);

            int affectedRow = stmt.executeUpdate();
            if (affectedRow < 0) {
                throw new OperationFailedException("Failed to update link!", 500);
            }
            return "Link updated successfully!";

        } catch (Exception e) {
            LOG.error("Error during update! " + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public Link getlinkById(String number) {
        String sql = "SELECT * from links WHERE id = ?";

        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(number));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Link link = new Link();
                link.setId(rs.getInt("id"));
                link.setLink(rs.getString("link"));
                link.setTitle(rs.getString("title"));
                return link;
            } else {
                throw new ResourceNotFoundException("Link not Found");
            }

        }catch (ResourceNotFoundException e) {
            LOG.error("Failed to get link! " + e.getMessage());
            throw e;
        }catch (Exception e) {
            LOG.error("Failed to get link! " + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public String deletelinkById(int number) {

        String sql = "DELETE FROM links WHERE id = ?";
        try( Connection connection = DbConnection.getCon();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, number);
            int affectedRow = stmt.executeUpdate();
            if (affectedRow < 0) {
                throw new OperationFailedException("Failed to delete link!", 500);
            }
            return "Link deleted successfully!";

        }catch (Exception e) {
            LOG.error("Failed to delete link! " + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }
}