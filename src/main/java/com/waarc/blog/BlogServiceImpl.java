package com.waarc.blog;

import com.waarc.blog.pojo.BlogRequest;
import com.waarc.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository = new BlogRepositoryImplementation();
    private final Logger log = LogManager.getLogger(BlogServiceImpl.class);

    @Override
    public Blog getBlog(int BlogId) {
        Blog blog = blogRepository.getBlog(BlogId).orElseThrow(()-> new ResourceNotFoundException("Blog Not Found with id :" + BlogId));
        return new Blog(blog.getId(),blog.getDate(), blog.getImage(), blog.getTitle(), blog.getDescription());
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.getAllBlogs();
    }

    @Override
    public Blog save(BlogRequest request) {
        return blogRepository.save(request);
    }

    @Override
    public Blog updateBlog(BlogRequest request, int BlogId) {
        Blog blog = blogRepository.getBlog(BlogId).orElseThrow(()-> new ResourceNotFoundException("Blog not found with the id : "+ BlogId));

         return blogRepository.updateBlog(request,BlogId);
    }

    @Override
    public Blog deleteBlog(int BlogId) {
        Blog blog = blogRepository.getBlog(BlogId).orElseThrow(()-> new ResourceNotFoundException("Blog not found with the id : "+ BlogId));
        blogRepository.deleteBlog(BlogId);
        return blog;
    }
}
