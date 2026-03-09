package com.waarc.home;

import com.waarc.config.DbConnection;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import com.waarc.home.pojo.HomeRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class HomeRepositoryImpl implements HomeRepository {
    private static final Log LOG = LogFactory.getLog(HomeRepositoryImpl.class);

    @Override
    public Home getHome() {
        String sql = "SELECT * FROM home where id=1 ";
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LOG.info("Getting Home !");
                  return Home.builder(
                 ).bannerImage(rs.getString("banner_image"))
                          .title(rs.getString("page_title"))
                         .description(rs.getString("page_sub_title"))
                         .metaTitle(rs.getString("seo_title"))
                         .metaKeywords(rs.getString("seo_keywords"))
                         .metaDescription(rs.getString("seo_description"))
                         .build();
            } else {
                throw new ResourceNotFoundException("Home not found");
            }
        }  catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Failed to get Home !" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }

    @Override
    public String save(HomeRequest request) {

        String sql = "INSERT INTO home (banner_image,page_title,page_sub_title,seo_title,seo_keywords,seo_description,id) values (?,?,?,?,?,?,?)";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, request.getBannerImage());
            stmt.setString(2, request.getTitle());
            stmt.setString(3, request.getDescription());
            stmt.setString(4, request.getMetaTitle());
            stmt.setString(5, request.getMetaKeywords());
            stmt.setString(6, request.getMetaDescription());
            stmt.setInt(7, 1);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                LOG.error("Failed to save Home !");
                throw new OperationFailedException("Failed to insert Home", 500);
            }
            return "Home inserted successfully";
        } catch (Exception e) {
            LOG.error("Home insertion Failed " + e.getMessage());
            throw new OperationFailedException("Failed to insert home", 500);
        }
    }

    @Override
    public String updateHome(HomeRequest request) {

        String sql = "UPDATE home SET banner_image = ?, page_title = ?, page_sub_title = ?, seo_title = ?, seo_keywords = ?, seo_description = ? WHERE id = ?";
        try (Connection conn = DbConnection.getCon(); PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, request.getBannerImage());
            stmt.setString(2, request.getTitle());
            stmt.setString(3, request.getDescription());
            stmt.setString(4, request.getMetaTitle());
            stmt.setString(5, request.getMetaKeywords());
            stmt.setString(6, request.getMetaDescription());
            stmt.setInt(7,1);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                LOG.error("Failed to update Home");
                throw new OperationFailedException("Failed to update Home", 500);
            }
            LOG.info("Home updated SuccessFul !");
            return "Home Updated Successfully";

        } catch (Exception e) {
            LOG.error("Failed to update Home" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public Optional<Home> getHomeById(String id) {
        String sql = "SELECT * FROM home ";
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(Home.builder(
                        ).bannerImage(rs.getString("banner_image"))
                        .description(rs.getString("page_sub_title"))
                        .metaTitle(rs.getString("seo_title"))
                        .metaKeywords(rs.getString("seo_keywords"))
                        .metaDescription(rs.getString("seo_description"))
                        .build());
            } else {
                 return Optional.empty();
            }
        } catch (Exception e) {
            LOG.error("Failed to get Home !" + e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
    }
}
