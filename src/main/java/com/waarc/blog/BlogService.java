package com.waarc.blog;

import com.waarc.blog.pojo.BlogRequest;

import java.util.List;

public interface BlogService {

    Blog getBlog(int BlogId);
    List<Blog> getAllBlogs();
    Blog save(BlogRequest request);
    Blog updateBlog(BlogRequest request, int BlogId);
    Blog deleteBlog(int BlogId);


}
