package com.waarc.blog;

import com.waarc.blog.pojo.BlogRequest;
import com.waarc.config.DbConnection;
import com.waarc.exception.OperationFailedException;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlogRepositoryImplementation implements BlogRepository {
    private static final Log log = LogFactory.getLog(BlogRepositoryImplementation.class);

    @Override
    public Optional<Blog> getBlog(int id) {
        String sql = "SELECT * FROM Blog WHERE id=?";
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                log.info("Getting blog with id :" + id);
                return Optional.of(
                        new Blog(
                                rs.getInt("id"),
                                rs.getObject("date",LocalDateTime.class),
                                rs.getString("image"),
                                rs.getString("title"),
                                rs.getString("description")
                        ));
            }

        } catch (Exception e) {
            log.error("Failed to get blog with id : " + id);
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return Optional.empty();
    }

    @Override
    public List<Blog> getAllBlogs() {
        String sql = "SELECT * FROM Blog";
        List<Blog> blogs = new ArrayList<>();
        try (Connection connection = DbConnection.getCon();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                Blog blog = new Blog(
                        rs.getInt("id"),
                        rs.getObject("date", LocalDateTime.class),
                        rs.getString("image"),
                        rs.getString("title"),
                        rs.getString("description")
                );
                blogs.add(blog);
            }
            return blogs;
        } catch (Exception e) {
            log.error("Failed to get blogs");
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public Blog save(BlogRequest request) {

        String sql = "INSERT INTO Blog (date,image,title,description) values (?,?,?,?)";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, request.getImage());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to insert Blog");
                throw new OperationFailedException("Failed to insert Blog", 500);
            }

            try (ResultSet keySet = stmt.getGeneratedKeys()) {

                if (keySet.next()) {
log.info("Created a new Blog !");
                    return new Blog(
                            keySet.getInt(1),
                            LocalDateTime.now(),
                            request.getImage(),
                            request.getTitle(),
                            request.getDescription()
                    );
                }
            } catch (Exception e) {
                log.error("Failed to create a blog : "+ e.getMessage());
                throw new ResourceNotFoundException(e.getMessage());
            }

        } catch (Exception e) {
            log.error("Failed to create a Blog :"+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }

    @Override
    public Blog updateBlog(BlogRequest request, int BlogId) {

        String sql = "UPDATE Blog SET  date = ?,image = ? , title = ? , description = ? where id = ?";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, request.getImage());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());
            stmt.setInt(5, BlogId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to update Blog");
                throw new OperationFailedException("Failed to update Blog", 500);
            }

            log.info("Blog updated SuccessFully !");
            return new Blog(BlogId,LocalDateTime.now(),request.getImage(), request.getTitle(), request.getDescription());

        } catch (Exception e) {
            log.error("Failed to update Blog : "+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }

    }

    @Override
    public Blog deleteBlog(int BlogId) {
        String sql = "DELETE FROM blog WHERE  id = ?";
        try (Connection conn = DbConnection.getCon();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, BlogId);

            int affectedRow = stmt.executeUpdate();
            if (!(affectedRow > 0)) {
                log.error("Failed to delete Blog");
                throw new OperationFailedException("Failed to delete Blog", 500);
            }

        } catch (Exception e) {
            log.info("Failed to delete Blog : "+ e.getMessage());
            throw new OperationFailedException(e.getMessage(), 500);
        }
        return null;
    }
}
