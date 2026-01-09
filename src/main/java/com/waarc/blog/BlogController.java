package com.waarc.blog;

import com.waarc.MiddleWare.AuthMiddleWare;
import com.waarc.blog.pojo.BlogRequest;
import io.javalin.Javalin;

public class BlogController {
    private final BlogService blogService = new BlogServiceImpl();

    public BlogController(Javalin app){
        app.before("/blog", AuthMiddleWare.requireLogin);
        app.before("/blog/*",AuthMiddleWare.requireLogin);

        app.post("/blog",ctx -> {
            BlogRequest request = ctx.bodyAsClass(BlogRequest.class);
            ctx.json(blogService.save(request)).status(200);
        });

        app.get("/blog/{id}", ctx -> {
            int BlogId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(blogService.getBlog(BlogId)).status(200);
        });

        app.get("/getBlogs",ctx -> {
           ctx.json(blogService.getAllBlogs()).status(200);
        });

        app.put("/blog/{id}", ctx -> {
            BlogRequest request = ctx.bodyAsClass(BlogRequest.class);
            int BlogId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(blogService.updateBlog(request,BlogId)).status(200);
        });

        app.delete("/blog/{id}", ctx -> {
            int BlogId = Integer.parseInt(ctx.pathParam("id"));
            ctx.json(blogService.deleteBlog(BlogId)).status(200);
        });


    }
}
