package com.waarc.blog;

import com.waarc.blog.pojo.BlogRequest;

import java.util.List;
import java.util.Optional;

public interface BlogRepository {

    Optional<Blog> getBlog(int id);
    List<Blog> getAllBlogs();
    Blog save(BlogRequest request);
    Blog updateBlog(BlogRequest request, int BlogId);
    Blog deleteBlog(int BlogId);

}
